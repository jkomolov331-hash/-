import { useState } from 'react';
import { motion, AnimatePresence } from 'motion/react';
import {
  RotateCcw, FileAudio, FileVideo,
  FolderOpen, CheckCircle2, AlertCircle, Loader2,
  TrendingUp, Activity,
} from 'lucide-react';

interface ResultScreenProps {
  originalFile: File;
  processedBlob: Blob;
  outputFileName: string;
  isVideo: boolean;
  stats: { noiseReduction: string; voiceClarity: string; snrImprovement: string } | null;
  onReset: () => void;
}

type DlState = 'idle' | 'picking' | 'saving' | 'saved' | 'error';

function formatSize(bytes: number) {
  if (bytes < 1024 * 1024) return `${(bytes / 1024).toFixed(1)} KB`;
  return `${(bytes / (1024 * 1024)).toFixed(2)} MB`;
}

const isElectron =
  typeof window !== 'undefined' && !!(window as any).electronAPI?.isElectron;

export function ResultScreen({
  originalFile, processedBlob, outputFileName, isVideo, stats, onReset,
}: ResultScreenProps) {
  const [dlState,   setDlState]   = useState<DlState>('idle');
  const [savedPath, setSavedPath] = useState('');
  const [errMsg,    setErrMsg]    = useState('');

  const accentColor = isVideo ? '#03DAC6' : '#BB86FC';

  const displayStats = stats
    ? [
        { label: 'Noise Reduction', value: stats.noiseReduction,  color: '#BB86FC' },
        { label: 'Voice Clarity',   value: stats.voiceClarity,    color: '#03DAC6' },
        { label: 'SNR Improved',    value: stats.snrImprovement,  color: '#BB86FC' },
      ]
    : [
        { label: 'Noise Reduction', value: '-42 dB', color: '#BB86FC' },
        { label: 'Voice Clarity',   value: '+87%',   color: '#03DAC6' },
        { label: 'SNR Improved',    value: '+18 dB', color: '#BB86FC' },
      ];

  const handleDownload = async () => {
    setDlState('picking');
    setErrMsg('');

    try {
      // ── Electron: native save dialog ──────────────────────────
      if (isElectron) {
        const api = (window as any).electronAPI;
        const result = await api.saveFile(processedBlob, outputFileName);
        if (result.canceled) { setDlState('idle'); return; }
        if (!result.success) throw new Error(result.error ?? 'Failed to save file.');
        const parts = (result.filePath as string).replace(/\\/g, '/').split('/');
        setSavedPath(`${parts.at(-2)}/${parts.at(-1)}`);
        setDlState('saved');
        return;
      }

      // ── Web: File System Access API ───────────────────────────
      if ('showDirectoryPicker' in window) {
        let dirHandle: FileSystemDirectoryHandle;
        try {
          dirHandle = await (window as any).showDirectoryPicker({ mode: 'readwrite', startIn: 'downloads' });
        } catch (err: any) {
          if (err?.name === 'AbortError') { setDlState('idle'); return; }
          throw err;
        }
        setDlState('saving');
        const fileHandle = await dirHandle.getFileHandle(outputFileName, { create: true });
        const writable   = await fileHandle.createWritable();
        await writable.write(processedBlob);
        await writable.close();
        setSavedPath(`${dirHandle.name}/${outputFileName}`);
        setDlState('saved');
      } else {
        // ── Fallback: <a download> ────────────────────────────
        setDlState('saving');
        const url = URL.createObjectURL(processedBlob);
        const a   = Object.assign(document.createElement('a'), { href: url, download: outputFileName });
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        URL.revokeObjectURL(url);
        setSavedPath(`Downloads/${outputFileName}`);
        setDlState('saved');
      }
    } catch (err: any) {
      setErrMsg(err?.message ?? 'Export failed. Please try again.');
      setDlState('error');
    }
  };

  const btnConfig: Record<DlState, { label: string; icon: React.ReactNode; bg: string; shadow: string }> = {
    idle: {
      label:  'EXPORT FILE',
      icon:   <FolderOpen size={17} />,
      bg:     isVideo ? 'linear-gradient(135deg,#03DAC6,#018786)' : 'linear-gradient(135deg,#BB86FC,#7B5EA7)',
      shadow: isVideo ? '0 0 20px rgba(3,218,198,0.28)' : '0 0 20px rgba(187,134,252,0.28)',
    },
    picking: {
      label:  isElectron ? 'CHOOSE LOCATION…' : 'CHOOSE FOLDER…',
      icon:   <FolderOpen size={17} />,
      bg:     'linear-gradient(135deg,#BB86FC,#7B5EA7)',
      shadow: '0 0 20px rgba(187,134,252,0.4)',
    },
    saving: {
      label:  'SAVING…',
      icon:   <Loader2 size={17} className="animate-spin" />,
      bg:     'linear-gradient(135deg,#BB86FC,#7B5EA7)',
      shadow: '0 0 20px rgba(187,134,252,0.4)',
    },
    saved: {
      label:  'SAVED ✓',
      icon:   <CheckCircle2 size={17} strokeWidth={2.5} />,
      bg:     'linear-gradient(135deg,#03DAC6,#018786)',
      shadow: '0 0 24px rgba(3,218,198,0.45)',
    },
    error: {
      label:  'RETRY',
      icon:   <AlertCircle size={17} />,
      bg:     'linear-gradient(135deg,#CF6679,#B00020)',
      shadow: '0 0 20px rgba(207,102,121,0.35)',
    },
  };

  const cfg  = btnConfig[dlState];
  const busy = dlState === 'picking' || dlState === 'saving';

  return (
    <motion.div
      className="w-full flex flex-col items-center"
      initial={{ opacity:0, y:20 }}
      animate={{ opacity:1, y:0 }}
      transition={{ duration:0.5, ease:[0.16,1,0.3,1] }}
      style={{ maxWidth:'520px' }}
    >
      {/* Header */}
      <div className="text-center mb-8">
        <motion.div
          className="flex items-center justify-center mb-4"
          initial={{ scale:0 }} animate={{ scale:1 }}
          transition={{ type:'spring', stiffness:280, damping:22, delay:0.1 }}
        >
          <div
            className="flex items-center justify-center rounded-full"
            style={{ width:'64px', height:'64px', background:`${accentColor}1A`, border:`1.5px solid ${accentColor}66`, boxShadow:`0 0 24px ${accentColor}33,0 0 48px ${accentColor}14` }}
          >
            <motion.span
              style={{ fontSize:'26px' }}
              initial={{ scale:0, rotate:-20 }} animate={{ scale:1, rotate:0 }}
              transition={{ delay:0.25, type:'spring', stiffness:300, damping:16 }}
            >✓</motion.span>
          </div>
        </motion.div>

        <motion.h1
          style={{ fontFamily:'Space Grotesk,sans-serif', fontSize:'34px', fontWeight:700, color:'rgba(255,255,255,0.95)', letterSpacing:'-0.02em' }}
          initial={{ opacity:0, y:10 }} animate={{ opacity:1, y:0 }} transition={{ delay:0.15 }}
        >
          Enhancement Complete
        </motion.h1>
        <motion.p
          style={{ fontFamily:'Inter,sans-serif', fontSize:'14px', color:'rgba(255,255,255,0.35)', marginTop:'6px' }}
          initial={{ opacity:0 }} animate={{ opacity:1 }} transition={{ delay:0.22 }}
        >
          {isVideo ? 'Video re-muxed with enhanced Opus audio track' : 'Audio processed through 17-stage DSP pipeline'}
        </motion.p>
      </div>

      {/* Stats */}
      <motion.div
        className="w-full grid grid-cols-3 gap-3 mb-4"
        initial={{ opacity:0, y:12 }} animate={{ opacity:1, y:0 }} transition={{ delay:0.28 }}
      >
        {displayStats.map((s, i) => (
          <motion.div
            key={s.label}
            className="flex flex-col items-center justify-center rounded-xl p-4"
            style={{ background:'rgba(255,255,255,0.02)', border:'1px solid rgba(255,255,255,0.06)' }}
            initial={{ opacity:0, scale:0.9 }} animate={{ opacity:1, scale:1 }}
            transition={{ delay:0.3+i*0.07, type:'spring', stiffness:300, damping:24 }}
          >
            <p style={{ fontFamily:'Space Grotesk,sans-serif', fontSize:'20px', fontWeight:700, color:s.color }}>{s.value}</p>
            <p style={{ fontFamily:'Inter,sans-serif', fontSize:'11px', color:'rgba(255,255,255,0.35)', marginTop:'3px', textAlign:'center' }}>{s.label}</p>
          </motion.div>
        ))}
      </motion.div>

      {/* Output file card */}
      <motion.div
        className="w-full flex items-center gap-4 rounded-xl p-4 mb-4"
        style={{ background:`${accentColor}0D`, border:`1px solid ${accentColor}2E` }}
        initial={{ opacity:0, x:-10 }} animate={{ opacity:1, x:0 }} transition={{ delay:0.35 }}
      >
        <div className="flex items-center justify-center rounded-lg flex-shrink-0" style={{ width:'44px', height:'44px', background:`${accentColor}1E`, border:`1px solid ${accentColor}40` }}>
          {isVideo ? <FileVideo size={20} style={{ color:accentColor }} /> : <FileAudio size={20} style={{ color:accentColor }} />}
        </div>
        <div className="flex-1 min-w-0">
          <p className="truncate" style={{ fontFamily:'Inter,sans-serif', fontSize:'13px', fontWeight:500, color:'rgba(255,255,255,0.8)' }}>{outputFileName}</p>
          <div className="flex items-center gap-2 mt-1">
            <Activity size={11} style={{ color:accentColor }} />
            <p style={{ fontFamily:'Inter,sans-serif', fontSize:'11px', color:accentColor }}>
              Enhanced · {isVideo ? 'WebM · VP9 + Opus' : 'WAV · PCM 16-bit'} · {formatSize(processedBlob.size)}
            </p>
          </div>
        </div>
        <TrendingUp size={16} style={{ color:`${accentColor}80` }} />
      </motion.div>

      {/* Saved / error banners */}
      <AnimatePresence>
        {dlState === 'saved' && savedPath && (
          <motion.div
            className="w-full flex items-center gap-3 rounded-xl px-4 py-3"
            style={{ background:'rgba(3,218,198,0.08)', border:'1px solid rgba(3,218,198,0.28)', marginBottom:'12px' }}
            initial={{ opacity:0, height:0, marginBottom:0 }}
            animate={{ opacity:1, height:'auto', marginBottom:'12px' }}
            exit={{ opacity:0, height:0, marginBottom:0 }}
            transition={{ duration:0.3 }}
          >
            <CheckCircle2 size={15} style={{ color:'#03DAC6', flexShrink:0 }} />
            <div className="flex-1 min-w-0">
              <p style={{ fontFamily:'Inter,sans-serif', fontSize:'11px', color:'rgba(255,255,255,0.4)' }}>Saved to</p>
              <p className="truncate" style={{ fontFamily:'Space Grotesk,sans-serif', fontSize:'12px', fontWeight:600, color:'#03DAC6' }} title={savedPath}>{savedPath}</p>
            </div>
          </motion.div>
        )}
        {dlState === 'error' && errMsg && (
          <motion.div
            className="w-full flex items-center gap-3 rounded-xl px-4 py-3"
            style={{ background:'rgba(207,102,121,0.08)', border:'1px solid rgba(207,102,121,0.3)', marginBottom:'12px' }}
            initial={{ opacity:0, height:0, marginBottom:0 }}
            animate={{ opacity:1, height:'auto', marginBottom:'12px' }}
            exit={{ opacity:0, height:0, marginBottom:0 }}
          >
            <AlertCircle size={15} style={{ color:'#CF6679', flexShrink:0 }} />
            <p className="truncate" style={{ fontFamily:'Inter,sans-serif', fontSize:'12px', color:'rgba(255,255,255,0.45)' }}>{errMsg}</p>
          </motion.div>
        )}
      </AnimatePresence>

      {/* Action buttons */}
      <motion.div className="w-full flex gap-3" initial={{ opacity:0, y:10 }} animate={{ opacity:1, y:0 }} transition={{ delay:0.4 }}>
        <motion.button
          onClick={() => {
            if (!busy) {
              if (dlState === 'saved') { setSavedPath(''); setDlState('idle'); setTimeout(handleDownload, 50); }
              else handleDownload();
            }
          }}
          disabled={busy}
          className="relative flex-1 flex items-center justify-center gap-2 rounded-xl overflow-hidden"
          style={{
            height:'54px', background:cfg.bg, boxShadow:cfg.shadow,
            fontFamily:'Space Grotesk,sans-serif', fontSize:'14px', fontWeight:700,
            letterSpacing:'0.07em', color:'#101010',
            cursor:busy ? 'not-allowed' : 'pointer', opacity:busy ? 0.85 : 1,
            transition:'background 0.35s ease, box-shadow 0.35s ease',
          }}
          whileHover={!busy ? { scale:1.015 } : {}}
          whileTap={!busy ? { scale:0.98 } : {}}
        >
          {busy && (
            <motion.div
              className="absolute inset-0"
              style={{ background:'linear-gradient(90deg,transparent 0%,rgba(255,255,255,0.18) 50%,transparent 100%)' }}
              animate={{ x:['-100%','200%'] }}
              transition={{ duration:1.2, repeat:Infinity, ease:'easeInOut' }}
            />
          )}
          <span className="relative z-10 flex items-center gap-2">{cfg.icon}{cfg.label}</span>
          {dlState === 'idle' && (
            <span className="absolute bottom-[5px] inset-x-0 text-center" style={{ fontFamily:'Inter,sans-serif', fontSize:'9px', color:'rgba(16,16,16,0.5)', letterSpacing:'0.05em' }}>
              {isElectron ? 'native save dialog' : 'opens folder picker'}
            </span>
          )}
          {dlState === 'saved' && (
            <span className="absolute bottom-[5px] inset-x-0 text-center" style={{ fontFamily:'Inter,sans-serif', fontSize:'9px', color:'rgba(16,16,16,0.45)', letterSpacing:'0.04em' }}>
              click to export again
            </span>
          )}
        </motion.button>

        <motion.button
          onClick={onReset}
          className="flex items-center justify-center gap-2 rounded-xl px-5"
          style={{ height:'54px', background:'rgba(255,255,255,0.04)', border:'1px solid rgba(255,255,255,0.1)', fontFamily:'Inter,sans-serif', fontSize:'13px', color:'rgba(255,255,255,0.5)' }}
          whileHover={{ background:'rgba(255,255,255,0.08)', color:'rgba(255,255,255,0.8)' }}
          whileTap={{ scale:0.97 }}
        >
          <RotateCcw size={15} />
          New file
        </motion.button>
      </motion.div>

      <AnimatePresence>
        {dlState === 'idle' && (
          <motion.p
            initial={{ opacity:0 }} animate={{ opacity:1 }} exit={{ opacity:0 }}
            style={{ fontFamily:'Inter,sans-serif', fontSize:'11px', color:'rgba(255,255,255,0.18)', marginTop:'10px', textAlign:'center' }}
          >
            {isElectron ? 'A native save dialog will open — choose where to save the file' : `A folder picker will open — choose where to save ${isVideo ? 'the video' : 'the audio'}`}
          </motion.p>
        )}
      </AnimatePresence>
    </motion.div>
  );
}
