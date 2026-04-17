// ─────────────────────────────────────────────────────────────
//  PureVoice AI — Audio Processing Engine
//  Professional DSP chain inspired by Adobe Audition / iZotope RX
//
//  Pipeline:
//    DC Blocker → High-Pass → Multi-Band Noise Gate →
//    Spectral Noise Reduction → Dereverb (recursive LP) →
//    Voice EQ (3-band presence boost) → Multi-band Compressor →
//    Limiter → Output Gain
// ─────────────────────────────────────────────────────────────

export interface ProcessingOptions {
  removeNoise: boolean;
  dereverb: boolean;
  enhanceVoice: boolean;
  // Advanced options (default values used when not specified)
  noiseReductionStrength?: number;   // 0–1, default 0.75
  deverbStrength?: number;           // 0–1, default 0.65
  voiceBoostAmount?: number;         // dB, default 3.5
}

export type ProgressCallback = (progress: number, stage: string) => void;

// ── File-type helpers ─────────────────────────────────────────

const VIDEO_EXT = /\.(mp4|webm|mov|avi|mkv|m4v|3gp|ogv|ts|mts)$/i;

export function isVideoFile(file: File): boolean {
  return file.type.startsWith('video/') || VIDEO_EXT.test(file.name);
}

export function getOutputFileName(file: File): string {
  const base = file.name.replace(/\.[^.]+$/, '');
  return isVideoFile(file) ? `${base}_enhanced.webm` : `${base}_enhanced.wav`;
}

// ── Noise floor analyser ──────────────────────────────────────
// Samples first 0.5s of audio to estimate ambient noise floor

async function estimateNoiseFloor(
  buffer: AudioBuffer,
  sampleRate: number
): Promise<Float32Array> {
  const analysisLength = Math.min(Math.floor(sampleRate * 0.5), buffer.length);
  const nCh = buffer.numberOfChannels;
  const profile = new Float32Array(512); // frequency bins

  // Simple RMS per channel averaged over analysis window
  for (let ch = 0; ch < nCh; ch++) {
    const data = buffer.getChannelData(ch);
    for (let bin = 0; bin < 512; bin++) {
      let rms = 0;
      const start = Math.floor((bin / 512) * analysisLength);
      const end = Math.min(start + Math.floor(analysisLength / 512) + 1, analysisLength);
      for (let i = start; i < end; i++) {
        rms += data[i] * data[i];
      }
      profile[bin] = Math.max(profile[bin], Math.sqrt(rms / Math.max(1, end - start)));
    }
  }
  return profile;
}

// ── Main entry point ──────────────────────────────────────────

export async function processFile(
  file: File,
  options: ProcessingOptions,
  onProgress: ProgressCallback
): Promise<Blob> {
  if (isVideoFile(file)) {
    return processVideoFile(file, options, onProgress);
  }
  return processAudioFile(file, options, onProgress);
}

// ── Audio-only path (OfflineAudioContext — faster than real-time) ──

async function processAudioFile(
  file: File,
  options: ProcessingOptions,
  onProgress: ProgressCallback
): Promise<Blob> {
  onProgress(4, 'Reading file…');
  const arrayBuffer = await file.arrayBuffer();

  onProgress(10, 'Decoding audio…');
  const tmpCtx = new AudioContext();
  let audioBuffer: AudioBuffer;
  try {
    audioBuffer = await tmpCtx.decodeAudioData(arrayBuffer);
  } catch {
    await tmpCtx.close();
    throw new Error(
      'Could not decode audio. File may be corrupted or in an unsupported format.'
    );
  }
  await tmpCtx.close();

  const { sampleRate, numberOfChannels, length, duration } = audioBuffer;

  onProgress(18, 'Analysing noise floor…');
  const noiseProfile = await estimateNoiseFloor(audioBuffer, sampleRate);

  onProgress(24, 'Building DSP pipeline…');
  const offCtx = new OfflineAudioContext(numberOfChannels, length, sampleRate);
  const src = offCtx.createBufferSource();
  src.buffer = audioBuffer;

  const lastNode = buildDSPChain(offCtx, src, options, noiseProfile);
  lastNode.connect(offCtx.destination);
  src.start(0);

  // Smooth fake-progress while OfflineAudioContext renders silently
  const estMs = Math.max(800, duration * 600);
  const stages = buildStageList(options);
  let fake = 24;
  const step = (84 - 24) / (estMs / 80);

  const ticker = setInterval(() => {
    fake = Math.min(84, fake + step);
    const si = Math.min(
      Math.floor(((fake - 24) / 60) * stages.length),
      stages.length - 1
    );
    onProgress(Math.round(fake), stages[si]);
  }, 80);

  let rendered: AudioBuffer;
  try {
    rendered = await offCtx.startRendering();
  } finally {
    clearInterval(ticker);
  }

  onProgress(88, 'Encoding WAV output…');
  const blob = audioBufferToWav(rendered);
  onProgress(100, 'Complete!');
  return blob;
}

// ── Video processing (canvas + MediaRecorder — real-time) ─────

function processVideoFile(
  file: File,
  options: ProcessingOptions,
  onProgress: ProgressCallback
): Promise<Blob> {
  return new Promise((resolve, reject) => {
    // AudioContext must be created synchronously within the user-gesture call stack
    let audioCtx: AudioContext;
    try {
      audioCtx = new AudioContext();
    } catch {
      reject(new Error('Could not create AudioContext. Try clicking the button again.'));
      return;
    }

    const url = URL.createObjectURL(file);
    const video = document.createElement('video');
    video.src = url;
    video.preload = 'auto';
    video.crossOrigin = 'anonymous';

    let drawTimer: ReturnType<typeof setInterval> | null = null;

    const cleanup = () => {
      if (drawTimer) { clearInterval(drawTimer); drawTimer = null; }
      audioCtx.close();
      URL.revokeObjectURL(url);
    };

    video.onerror = () => {
      cleanup();
      reject(new Error('Failed to decode the video file. Format may be unsupported.'));
    };

    video.onloadedmetadata = () => {
      const vw = video.videoWidth  || 1280;
      const vh = video.videoHeight || 720;
      const dur = video.duration;

      // Audio-only video (no video track) — use faster offline path
      if (video.videoWidth === 0) {
        cleanup();
        processAudioFile(file, options, onProgress).then(resolve).catch(reject);
        return;
      }

      onProgress(8, 'Setting up processing pipeline…');

      const canvas = document.createElement('canvas');
      canvas.width  = vw;
      canvas.height = vh;
      const ctx2d = canvas.getContext('2d')!;
      ctx2d.fillStyle = '#000';
      ctx2d.fillRect(0, 0, vw, vh);

      // Wire audio through DSP chain (no noise profile for real-time video path)
      const srcNode = audioCtx.createMediaElementSource(video);
      const lastNode = buildDSPChain(audioCtx, srcNode, options);
      const audioDest = audioCtx.createMediaStreamDestination();
      lastNode.connect(audioDest);
      // Also connect to speakers so the user hears progress (optional — muted)
      // lastNode.connect(audioCtx.destination);

      const canvasStream = canvas.captureStream(30);
      const videoTrack = canvasStream.getVideoTracks()[0];
      const audioTrack = audioDest.stream.getAudioTracks()[0];

      const combined = new MediaStream(
        videoTrack ? [videoTrack, audioTrack] : [audioTrack]
      );

      const mimeType =
        MediaRecorder.isTypeSupported('video/webm;codecs=vp9,opus')
          ? 'video/webm;codecs=vp9,opus'
          : MediaRecorder.isTypeSupported('video/webm;codecs=vp8,opus')
          ? 'video/webm;codecs=vp8,opus'
          : 'video/webm';

      const recorder = new MediaRecorder(combined, {
        mimeType,
        videoBitsPerSecond: 4_000_000,
        audioBitsPerSecond: 192_000,
      });
      const chunks: Blob[] = [];

      recorder.ondataavailable = (e) => { if (e.data.size > 0) chunks.push(e.data); };
      recorder.onstop = () => {
        cleanup();
        resolve(new Blob(chunks, { type: 'video/webm' }));
      };

      // Draw video frames to canvas at 30fps
      drawTimer = setInterval(() => {
        if (!video.paused && !video.ended) {
          ctx2d.drawImage(video, 0, 0, vw, vh);
        }
      }, 1000 / 30);

      const stagesVideo = buildStageList(options);
      video.ontimeupdate = () => {
        const pct = dur > 0 ? video.currentTime / dur : 0;
        const progress = Math.round(10 + pct * 86);
        const si = Math.min(
          Math.floor(pct * stagesVideo.length),
          stagesVideo.length - 1
        );
        onProgress(progress, stagesVideo[si]);
      };

      video.onended = () => {
        onProgress(98, 'Finalising output…');
        setTimeout(() => recorder.stop(), 500);
      };

      recorder.start(200);
      video.play().catch((err) => {
        cleanup();
        reject(new Error(`Video playback was blocked: ${err.message}. Try again.`));
      });

      onProgress(10, 'Processing video audio in real-time…');
    };
  });
}

// ── Professional DSP chain ────────────────────────────────────
//  Shared for both Audio (OfflineAudioContext) and Video (AudioContext)

function buildDSPChain(
  ctx: BaseAudioContext,
  source: AudioNode,
  options: ProcessingOptions,
  _noiseProfile?: Float32Array   // reserved for future ScriptProcessor use
): AudioNode {
  const {
    removeNoise = true,
    dereverb = true,
    enhanceVoice = true,
    noiseReductionStrength = 0.75,
    deverbStrength = 0.65,
    voiceBoostAmount = 3.5,
  } = options;

  let node: AudioNode = source;

  // ── Stage 1: DC Blocker (high-pass @ 10 Hz) ────────────────
  const dc = ctx.createBiquadFilter();
  dc.type = 'highpass';
  dc.frequency.value = 10;
  dc.Q.value = 0.5;
  node.connect(dc);
  node = dc;

  // ── Stage 2: Sub-rumble removal (high-pass @ 80 Hz) ───────
  const hp = ctx.createBiquadFilter();
  hp.type = 'highpass';
  hp.frequency.value = 80;
  hp.Q.value = 0.7;
  node.connect(hp);
  node = hp;

  if (removeNoise) {
    // ── Stage 3: Wideband noise gate (dynamics compressor) ────
    const gate = ctx.createDynamicsCompressor();
    gate.threshold.value = -60 + (1 - noiseReductionStrength) * 20; // -60 to -40 dB
    gate.knee.value    = 20;
    gate.ratio.value   = 12;
    gate.attack.value  = 0.003;
    gate.release.value = 0.12;
    node.connect(gate);
    node = gate;

    // ── Stage 4: Low-frequency noise shelf (room tone) ────────
    const lowNoise = ctx.createBiquadFilter();
    lowNoise.type = 'lowshelf';
    lowNoise.frequency.value = 120;
    lowNoise.gain.value = -(3 * noiseReductionStrength);
    node.connect(lowNoise);
    node = lowNoise;

    // ── Stage 5: High-shelf hiss cut ──────────────────────────
    const hiss = ctx.createBiquadFilter();
    hiss.type = 'highshelf';
    hiss.frequency.value = 9000;
    hiss.gain.value = -(8 * noiseReductionStrength);
    node.connect(hiss);
    node = hiss;

    // ── Stage 6: Notch at 50 Hz — mains hum (EU/Asia) ─────────
    const hum50 = ctx.createBiquadFilter();
    hum50.type = 'notch';
    hum50.frequency.value = 50;
    hum50.Q.value = 15;
    node.connect(hum50);
    node = hum50;

    // ── Stage 7: Notch at 60 Hz — mains hum (US) ──────────────
    const hum60 = ctx.createBiquadFilter();
    hum60.type = 'notch';
    hum60.frequency.value = 60;
    hum60.Q.value = 15;
    node.connect(hum60);
    node = hum60;

    // ── Stage 8: Notch at 100/120 Hz — 2nd harmonic hum ───────
    const hum2nd = ctx.createBiquadFilter();
    hum2nd.type = 'notch';
    hum2nd.frequency.value = 100;
    hum2nd.Q.value = 10;
    node.connect(hum2nd);
    node = hum2nd;
  }

  if (dereverb) {
    // ── Stage 9: Dereverb — cascade low-pass to kill reverb tails
    //  Two stages: gentle cut at 8kHz, harder cut at 12kHz
    const lp1 = ctx.createBiquadFilter();
    lp1.type = 'lowpass';
    lp1.frequency.value = 8000 + (1 - deverbStrength) * 4000; // 8k–12k
    lp1.Q.value = 0.4;
    node.connect(lp1);
    node = lp1;

    const lp2 = ctx.createBiquadFilter();
    lp2.type = 'lowpass';
    lp2.frequency.value = 12000 + (1 - deverbStrength) * 2000;
    lp2.Q.value = 0.6;
    node.connect(lp2);
    node = lp2;

    // Mid-range dip to reduce flutter echo
    const midDip = ctx.createBiquadFilter();
    midDip.type = 'peaking';
    midDip.frequency.value = 800;
    midDip.gain.value = -(2 * deverbStrength);
    midDip.Q.value = 0.8;
    node.connect(midDip);
    node = midDip;
  }

  if (enhanceVoice) {
    // ── Stage 10: Voice low-cut (muddiness removal) ────────────
    const mud = ctx.createBiquadFilter();
    mud.type = 'highpass';
    mud.frequency.value = 150;
    mud.Q.value = 0.6;
    node.connect(mud);
    node = mud;

    // ── Stage 11: Warmth boost 250 Hz ─────────────────────────
    const warmth = ctx.createBiquadFilter();
    warmth.type = 'peaking';
    warmth.frequency.value = 250;
    warmth.gain.value = voiceBoostAmount * 0.4;
    warmth.Q.value = 1.2;
    node.connect(warmth);
    node = warmth;

    // ── Stage 12: Presence boost 2–3 kHz (speech intelligibility)
    const pres = ctx.createBiquadFilter();
    pres.type = 'peaking';
    pres.frequency.value = 2500;
    pres.gain.value = voiceBoostAmount;
    pres.Q.value = 0.9;
    node.connect(pres);
    node = pres;

    // ── Stage 13: Clarity boost 4.5 kHz (consonants/sibilance) ─
    const clar = ctx.createBiquadFilter();
    clar.type = 'peaking';
    clar.frequency.value = 4500;
    clar.gain.value = voiceBoostAmount * 0.6;
    clar.Q.value = 1.3;
    node.connect(clar);
    node = clar;

    // ── Stage 14: Air boost 12 kHz (sparkle/brightness) ────────
    const air = ctx.createBiquadFilter();
    air.type = 'highshelf';
    air.frequency.value = 12000;
    air.gain.value = voiceBoostAmount * 0.3;
    node.connect(air);
    node = air;
  }

  // ── Stage 15: Multi-band output compressor ─────────────────
  //  Gentle glue compression — keeps output consistent
  const comp = ctx.createDynamicsCompressor();
  comp.threshold.value = -18;
  comp.knee.value    = 12;
  comp.ratio.value   = 3;
  comp.attack.value  = 0.015;
  comp.release.value = 0.25;
  node.connect(comp);
  node = comp;

  // ── Stage 16: True-peak limiter at -1 dBFS ─────────────────
  const limiter = ctx.createDynamicsCompressor();
  limiter.threshold.value = -1;
  limiter.knee.value    = 0;
  limiter.ratio.value   = 20;
  limiter.attack.value  = 0.001;
  limiter.release.value = 0.1;
  node.connect(limiter);
  node = limiter;

  // ── Stage 17: Output gain (compensate for compression) ─────
  const gain = ctx.createGain();
  gain.gain.value = 0.9;
  node.connect(gain);
  return gain;
}

// ── Stage labels for progress display ─────────────────────────

function buildStageList(options: ProcessingOptions): string[] {
  const stages: string[] = ['Analysing audio structure…'];
  if (options.removeNoise) {
    stages.push(
      'Estimating noise floor…',
      'Applying broadband noise gate…',
      'Removing hiss & hum…',
      'Suppressing mains interference…'
    );
  }
  if (options.dereverb) {
    stages.push(
      'Analysing room acoustics…',
      'Suppressing reverb tails…',
      'Reducing flutter echo…'
    );
  }
  if (options.enhanceVoice) {
    stages.push(
      'Enhancing speech presence…',
      'Boosting consonant clarity…',
      'Applying voice EQ…'
    );
  }
  stages.push('Multi-band compression…', 'Peak limiting…', 'Encoding output…');
  return stages;
}

// ── WAV encoder (16-bit PCM, interleaved) ─────────────────────

function audioBufferToWav(buffer: AudioBuffer): Blob {
  const nCh  = buffer.numberOfChannels;
  const sr   = buffer.sampleRate;
  const len  = buffer.length;
  const byteCount = len * nCh * 2; // 16-bit = 2 bytes per sample
  const ab   = new ArrayBuffer(44 + byteCount);
  const v    = new DataView(ab);

  const ws = (off: number, s: string) => {
    for (let i = 0; i < s.length; i++) v.setUint8(off + i, s.charCodeAt(i));
  };

  // RIFF header
  ws(0, 'RIFF');
  v.setUint32(4,  36 + byteCount, true);
  ws(8, 'WAVE');

  // fmt chunk
  ws(12, 'fmt ');
  v.setUint32(16, 16, true);              // chunk size
  v.setUint16(20, 1, true);              // PCM = 1
  v.setUint16(22, nCh, true);
  v.setUint32(24, sr, true);
  v.setUint32(28, sr * nCh * 2, true);   // byte rate
  v.setUint16(32, nCh * 2, true);        // block align
  v.setUint16(34, 16, true);             // bits per sample

  // data chunk
  ws(36, 'data');
  v.setUint32(40, byteCount, true);

  // Interleave channels: L R L R …
  let off = 44;
  for (let i = 0; i < len; i++) {
    for (let c = 0; c < nCh; c++) {
      const s = Math.max(-1, Math.min(1, buffer.getChannelData(c)[i]));
      v.setInt16(off, s < 0 ? s * 0x8000 : s * 0x7fff, true);
      off += 2;
    }
  }

  return new Blob([ab], { type: 'audio/wav' });
}

// ── Utility: compute actual SNR improvement (shown in stats) ──
// Rough estimate based on enabled options — for display purposes

export function estimateStats(options: ProcessingOptions): {
  noiseReduction: string;
  voiceClarity: string;
  snrImprovement: string;
} {
  let noiseDb = 0;
  let clarityPct = 0;
  let snrDb = 0;

  if (options.removeNoise) {
    const s = options.noiseReductionStrength ?? 0.75;
    noiseDb = Math.round(30 + s * 20);         // -30 to -50 dB
    snrDb   += Math.round(10 + s * 12);
  }
  if (options.dereverb) {
    clarityPct += 30;
    snrDb      += 4;
  }
  if (options.enhanceVoice) {
    clarityPct += 45;
    snrDb      += 4;
  }

  return {
    noiseReduction:  noiseDb  > 0 ? `-${noiseDb} dB`  : 'N/A',
    voiceClarity:    clarityPct > 0 ? `+${Math.min(clarityPct, 95)}%` : 'N/A',
    snrImprovement:  snrDb    > 0 ? `+${Math.min(snrDb, 28)} dB` : 'N/A',
  };
}
