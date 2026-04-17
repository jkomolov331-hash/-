import { useState, useCallback, useEffect } from 'react';
import { AnimatePresence, motion } from 'motion/react';
import { UploadZone } from './components/UploadZone';
import { FileReady } from './components/FileReady';
import { ProcessingScreen } from './components/ProcessingScreen';
import { ResultScreen } from './components/ResultScreen';
import {
  processFile,
  isVideoFile,
  getOutputFileName,
  estimateStats,
  type ProcessingOptions,
} from './utils/audioProcessor';

type AppState = 'idle' | 'ready' | 'processing' | 'done' | 'error';

// Detect if we're running inside Electron
const isElectron = typeof window !== 'undefined' && !!(window as any).electronAPI?.isElectron;

export default function App() {
  const [appState,   setAppState]   = useState<AppState>('idle');
  const [file,       setFile]       = useState<File | null>(null);
  const [blob,       setBlob]       = useState<Blob | null>(null);
  const [progress,   setProgress]   = useState(0);
  const [stage,      setStage]      = useState('');
  const [errorMsg,   setErrorMsg]   = useState('');
  const [stats,      setStats]      = useState<{ noiseReduction: string; voiceClarity: string; snrImprovement: string } | null>(null);

  // ── Electron: listen for "File → Open" menu events ──────────
  useEffect(() => {
    if (!isElectron) return;
    const api = (window as any).electronAPI;

    api.onOpenFile(async (filePath: string) => {
      try {
        // Fetch the file from disk and convert to a browser File object
        const response = await fetch(`file://${filePath}`);
        const arrayBuffer = await response.arrayBuffer();
        const fileName = filePath.split(/[\\/]/).pop() ?? 'audio';
        const mimeType = guessMime(fileName);
        const f = new File([arrayBuffer], fileName, { type: mimeType });
        handleFileSelected(f);
      } catch {
        // If fetch fails (common on Windows paths), fallback: show error
        setErrorMsg('Could not open file from path: ' + filePath);
        setAppState('error');
      }
    });

    return () => api.removeOpenFileListener();
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  function guessMime(name: string): string {
    const ext = name.split('.').pop()?.toLowerCase() ?? '';
    const map: Record<string, string> = {
      mp3: 'audio/mpeg', wav: 'audio/wav', flac: 'audio/flac',
      ogg: 'audio/ogg', aac: 'audio/aac', m4a: 'audio/mp4',
      mp4: 'video/mp4', mov: 'video/quicktime', mkv: 'video/x-matroska',
      webm: 'video/webm', avi: 'video/x-msvideo',
    };
    return map[ext] ?? 'application/octet-stream';
  }

  const handleFileSelected = (f: File) => {
    setFile(f);
    setBlob(null);
    setStats(null);
    setErrorMsg('');
    setAppState('ready');
  };

  const handleEnhance = useCallback(
    async (options: ProcessingOptions) => {
      if (!file) return;
      setAppState('processing');
      setProgress(0);
      setStage('Starting…');
      setBlob(null);

      try {
        const result = await processFile(file, options, (pct, msg) => {
          setProgress(pct);
          setStage(msg);
        });
        setBlob(result);
        setStats(estimateStats(options));
        // Brief pause at 100% so user sees the completed waveform
        await new Promise((r) => setTimeout(r, 700));
        setAppState('done');
      } catch (err: unknown) {
        const msg = err instanceof Error ? err.message : 'Processing failed unexpectedly.';
        setErrorMsg(msg);
        setAppState('error');
      }
    },
    [file]
  );

  const handleReset = () => {
    setFile(null);
    setBlob(null);
    setProgress(0);
    setStage('');
    setErrorMsg('');
    setStats(null);
    setAppState('idle');
  };

  const outputName = file ? getOutputFileName(file) : '';
  const isVideo    = file ? isVideoFile(file) : false;

  return (
    <div
      className="relative min-h-screen w-full flex flex-col items-center justify-center overflow-hidden"
      style={{ background: '#101010', fontFamily: 'Inter, sans-serif' }}
    >
      {/* ── Background ambient glows ── */}
      <div className="absolute inset-0 pointer-events-none overflow-hidden">
        <div style={{ position:'absolute', top:'-120px', left:'-80px', width:'500px', height:'500px', borderRadius:'50%', background:'radial-gradient(circle,rgba(187,134,252,0.07) 0%,transparent 65%)', filter:'blur(40px)' }} />
        <div style={{ position:'absolute', bottom:'-100px', right:'-60px', width:'460px', height:'460px', borderRadius:'50%', background:'radial-gradient(circle,rgba(3,218,198,0.06) 0%,transparent 65%)', filter:'blur(40px)' }} />
        <div style={{ position:'absolute', top:'50%', left:'50%', transform:'translate(-50%,-50%)', width:'800px', height:'400px', borderRadius:'50%', background:'radial-gradient(ellipse,rgba(187,134,252,0.025) 0%,transparent 60%)', filter:'blur(60px)' }} />
        <svg className="absolute inset-0 w-full h-full" xmlns="http://www.w3.org/2000/svg" style={{ opacity:0.025 }}>
          <defs>
            <pattern id="grid" width="60" height="60" patternUnits="userSpaceOnUse">
              <path d="M 60 0 L 0 0 0 60" fill="none" stroke="white" strokeWidth="0.5" />
            </pattern>
          </defs>
          <rect width="100%" height="100%" fill="url(#grid)" />
        </svg>
      </div>

      {/* ── Top bar ── */}
      <div className="absolute top-0 left-0 right-0 flex items-center justify-between px-8 py-4" style={{ borderBottom:'1px solid rgba(255,255,255,0.04)' }}>
        <div className="flex items-center gap-2.5">
          <div className="flex items-center justify-center rounded-lg" style={{ width:'28px', height:'28px', background:'linear-gradient(135deg,#BB86FC,#03DAC6)', boxShadow:'0 0 12px rgba(187,134,252,0.35)' }}>
            <span style={{ fontSize:'14px' }}>◈</span>
          </div>
          <span style={{ fontFamily:'Space Grotesk,sans-serif', fontSize:'15px', fontWeight:700, color:'rgba(255,255,255,0.88)', letterSpacing:'-0.01em' }}>
            PureVoice AI
          </span>
          <span style={{ padding:'1px 6px', borderRadius:'4px', background:'rgba(187,134,252,0.12)', border:'1px solid rgba(187,134,252,0.2)', fontSize:'10px', fontFamily:'Inter,sans-serif', color:'#BB86FC', letterSpacing:'0.05em', fontWeight:600 }}>
            BETA
          </span>
          {isElectron && (
            <span style={{ padding:'1px 6px', borderRadius:'4px', background:'rgba(3,218,198,0.08)', border:'1px solid rgba(3,218,198,0.15)', fontSize:'10px', fontFamily:'Inter,sans-serif', color:'#03DAC6', letterSpacing:'0.05em', fontWeight:600 }}>
              DESKTOP
            </span>
          )}
        </div>
        <div className="flex items-center gap-2">
          <motion.div
            className="rounded-full"
            style={{ width:'6px', height:'6px', background: appState === 'processing' ? '#BB86FC' : '#03DAC6' }}
            animate={{ opacity:[1,0.4,1] }}
            transition={{ duration:2, repeat:Infinity }}
          />
          <span style={{ fontFamily:'Inter,sans-serif', fontSize:'12px', color:'rgba(255,255,255,0.3)' }}>
            {appState === 'processing' ? 'Processing…' : 'DeepFilterNet v3 · Ready'}
          </span>
        </div>
      </div>

      {/* ── Main content ── */}
      <div className="relative z-10 w-full max-w-2xl px-8 flex flex-col items-center">
        <AnimatePresence mode="wait">

          {appState === 'idle' && (
            <motion.div key="idle" className="w-full flex justify-center" exit={{ opacity:0, y:-16 }} transition={{ duration:0.3 }}>
              <UploadZone onFileSelected={handleFileSelected} />
            </motion.div>
          )}

          {appState === 'ready' && file && (
            <motion.div key="ready" className="w-full flex justify-center" exit={{ opacity:0, y:-16 }} transition={{ duration:0.3 }}>
              <FileReady
                file={file}
                isVideo={isVideoFile(file)}
                onReset={handleReset}
                onEnhance={handleEnhance}
              />
            </motion.div>
          )}

          {appState === 'processing' && file && (
            <motion.div key="processing" className="w-full flex justify-center" exit={{ opacity:0, scale:0.98 }} transition={{ duration:0.3 }}>
              <ProcessingScreen
                fileName={file.name}
                isVideo={isVideo}
                progress={progress}
                stage={stage}
              />
            </motion.div>
          )}

          {appState === 'done' && file && blob && (
            <motion.div key="done" className="w-full flex justify-center" exit={{ opacity:0, y:-16 }} transition={{ duration:0.3 }}>
              <ResultScreen
                originalFile={file}
                processedBlob={blob}
                outputFileName={outputName}
                isVideo={isVideo}
                stats={stats}
                onReset={handleReset}
              />
            </motion.div>
          )}

          {appState === 'error' && (
            <motion.div
              key="error"
              className="w-full flex flex-col items-center gap-6"
              initial={{ opacity:0, y:20 }}
              animate={{ opacity:1, y:0 }}
              exit={{ opacity:0 }}
            >
              <div className="flex flex-col items-center gap-3 text-center">
                <div style={{ fontSize:'48px' }}>⚠️</div>
                <h2 style={{ fontFamily:'Space Grotesk,sans-serif', fontSize:'24px', fontWeight:700, color:'rgba(255,255,255,0.9)' }}>
                  Processing Failed
                </h2>
                <p style={{ fontFamily:'Inter,sans-serif', fontSize:'13px', color:'rgba(255,255,255,0.4)', maxWidth:'380px' }}>
                  {errorMsg}
                </p>
              </div>
              <motion.button
                onClick={handleReset}
                className="px-8 py-3 rounded-xl"
                style={{ background:'rgba(187,134,252,0.12)', border:'1px solid rgba(187,134,252,0.3)', color:'#BB86FC', fontFamily:'Inter,sans-serif', fontSize:'14px' }}
                whileHover={{ background:'rgba(187,134,252,0.22)' }}
                whileTap={{ scale:0.97 }}
              >
                Try again
              </motion.button>
            </motion.div>
          )}

        </AnimatePresence>
      </div>

      {/* ── Bottom bar ── */}
      <div className="absolute bottom-0 left-0 right-0 flex items-center justify-between px-8 py-3" style={{ borderTop:'1px solid rgba(255,255,255,0.04)' }}>
        <span style={{ fontFamily:'Inter,sans-serif', fontSize:'11px', color:'rgba(255,255,255,0.2)' }}>
          All processing is local · No data leaves your device
        </span>
        <div className="flex items-center gap-4">
          {['Privacy Policy', 'v1.0.0'].map((t) => (
            <span key={t} style={{ fontFamily:'Inter,sans-serif', fontSize:'11px', color:'rgba(255,255,255,0.2)', cursor:'pointer' }}>
              {t}
            </span>
          ))}
        </div>
      </div>
    </div>
  );
}
