import { motion } from "motion/react";
import { useState, useEffect, useRef } from "react";
import { FileAudio, FileVideo, X, Zap, Radio, Layers, Film } from "lucide-react";
import type { ProcessingOptions } from "../utils/audioProcessor";

interface FileReadyProps {
  file: File;
  isVideo: boolean;
  onReset: () => void;
  onEnhance: (options: ProcessingOptions) => void;
}

function formatSize(bytes: number): string {
  if (bytes < 1024 * 1024) return `${(bytes / 1024).toFixed(1)} KB`;
  return `${(bytes / (1024 * 1024)).toFixed(2)} MB`;
}

export function FileReady({ file, isVideo, onReset, onEnhance }: FileReadyProps) {
  const [removeNoise,  setRemoveNoise]  = useState(true);
  const [dereverb,     setDereverb]     = useState(true);
  const [enhanceVoice, setEnhanceVoice] = useState(true);

  // Generate video thumbnail
  const [thumb, setThumb] = useState<string | null>(null);
  const [duration, setDuration] = useState<string>("");
  const thumbDone = useRef(false);

  useEffect(() => {
    if (!isVideo || thumbDone.current) return;
    thumbDone.current = true;
    const url = URL.createObjectURL(file);
    const vid = document.createElement("video");
    vid.src = url;
    vid.muted = true;
    vid.preload = "metadata";
    vid.onloadedmetadata = () => {
      const d = vid.duration;
      const m = Math.floor(d / 60);
      const s = Math.floor(d % 60);
      setDuration(`${m}:${s.toString().padStart(2, "0")}`);
      vid.currentTime = Math.min(1, d * 0.1);
    };
    vid.onseeked = () => {
      const canvas = document.createElement("canvas");
      canvas.width  = 120;
      canvas.height = 68;
      const ctx = canvas.getContext("2d")!;
      ctx.drawImage(vid, 0, 0, 120, 68);
      setThumb(canvas.toDataURL("image/jpeg", 0.85));
      URL.revokeObjectURL(url);
    };
    vid.onerror = () => URL.revokeObjectURL(url);
  }, [file, isVideo]);

  const ext = file.name.split(".").pop()?.toUpperCase() ?? "FILE";

  return (
    <motion.div
      className="w-full flex flex-col items-center"
      initial={{ opacity: 0, y: 24 }}
      animate={{ opacity: 1, y: 0 }}
      exit={{ opacity: 0, y: -16 }}
      transition={{ duration: 0.5, ease: [0.16, 1, 0.3, 1] }}
      style={{ maxWidth: "520px" }}
    >
      {/* Header */}
      <div className="text-center mb-8">
        <h1 style={{ fontFamily:"Space Grotesk,sans-serif", fontSize:"36px", fontWeight:700, color:"rgba(255,255,255,0.95)", letterSpacing:"-0.02em", lineHeight:1.1 }}>
          Ready to Enhance
        </h1>
        <p style={{ fontFamily:"Inter,sans-serif", fontSize:"14px", color:"rgba(255,255,255,0.4)", marginTop:"6px" }}>
          {isVideo ? "Video audio will be enhanced and re-muxed" : "Configure your enhancement settings below"}
        </p>
      </div>

      {/* File card */}
      <motion.div
        className="w-full flex items-center gap-4 rounded-xl p-4 mb-4"
        style={{ background:"rgba(255,255,255,0.03)", border:"1px solid rgba(255,255,255,0.08)" }}
        initial={{ opacity:0, x:-10 }}
        animate={{ opacity:1, x:0 }}
        transition={{ delay:0.1 }}
      >
        {/* Thumbnail or icon */}
        {isVideo && thumb ? (
          <div className="relative flex-shrink-0 rounded-lg overflow-hidden" style={{ width:"60px", height:"34px" }}>
            <img src={thumb} alt="" className="w-full h-full object-cover" />
            <div className="absolute inset-0 flex items-center justify-center" style={{ background:"rgba(0,0,0,0.35)" }}>
              <Film size={12} style={{ color:"#fff" }} />
            </div>
          </div>
        ) : (
          <div className="flex items-center justify-center rounded-lg flex-shrink-0" style={{ width:"48px", height:"48px", background: isVideo ? "rgba(3,218,198,0.1)" : "rgba(187,134,252,0.1)", border:`1px solid ${isVideo ? "rgba(3,218,198,0.2)" : "rgba(187,134,252,0.2)"}` }}>
            {isVideo
              ? <FileVideo size={22} style={{ color:"#03DAC6" }} />
              : <FileAudio size={22} style={{ color:"#BB86FC" }} />
            }
          </div>
        )}

        <div className="flex-1 min-w-0">
          <p className="truncate" style={{ fontFamily:"Inter,sans-serif", fontSize:"14px", fontWeight:500, color:"rgba(255,255,255,0.85)" }}>
            {file.name}
          </p>
          <p style={{ fontFamily:"Inter,sans-serif", fontSize:"12px", color:"rgba(255,255,255,0.35)", marginTop:"2px" }}>
            {ext} · {formatSize(file.size)}{duration ? ` · ${duration}` : ""}
          </p>
        </div>

        {/* Waveform or video bars */}
        <div className="flex items-center gap-[3px] flex-shrink-0">
          {[4,8,14,10,18,12,6,16,9,5,13,7].map((h, i) => (
            <motion.div
              key={i}
              className="rounded-full"
              style={{ width:"2px", height:`${h}px`, background: isVideo ? "rgba(3,218,198,0.5)" : "rgba(187,134,252,0.5)" }}
              animate={{ scaleY:[1, 0.5+Math.random()*0.8, 1] }}
              transition={{ duration:1.2+Math.random()*0.5, repeat:Infinity, delay:i*0.08, ease:"easeInOut" }}
            />
          ))}
        </div>

        <button
          onClick={onReset}
          className="flex-shrink-0 p-1.5 rounded-lg"
          style={{ color:"rgba(255,255,255,0.25)", transition:"color 0.2s" }}
          onMouseEnter={e => (e.currentTarget.style.color="rgba(255,255,255,0.6)")}
          onMouseLeave={e => (e.currentTarget.style.color="rgba(255,255,255,0.25)")}
        >
          <X size={16} />
        </button>
      </motion.div>

      {/* Video notice */}
      {isVideo && (
        <motion.div
          className="w-full flex items-center gap-3 rounded-xl px-4 py-3 mb-4"
          style={{ background:"rgba(3,218,198,0.05)", border:"1px solid rgba(3,218,198,0.15)" }}
          initial={{ opacity:0 }} animate={{ opacity:1 }} transition={{ delay:0.12 }}
        >
          <Film size={14} style={{ color:"#03DAC6", flexShrink:0 }} />
          <p style={{ fontFamily:"Inter,sans-serif", fontSize:"12px", color:"rgba(255,255,255,0.45)", lineHeight:1.5 }}>
            Video processing runs in real-time — a 2-min video takes ~2 min.
            Output will be <strong style={{ color:"rgba(255,255,255,0.65)" }}>WebM</strong> format with enhanced Opus audio.
          </p>
        </motion.div>
      )}

      {/* Options */}
      <motion.div
        className="w-full rounded-xl p-5 mb-5"
        style={{ background:"rgba(255,255,255,0.02)", border:"1px solid rgba(255,255,255,0.06)" }}
        initial={{ opacity:0, y:10 }} animate={{ opacity:1, y:0 }} transition={{ delay:0.15 }}
      >
        <p style={{ fontFamily:"Inter,sans-serif", fontSize:"11px", letterSpacing:"0.12em", color:"rgba(255,255,255,0.3)", marginBottom:"14px", fontWeight:600 }}>
          ENHANCEMENT OPTIONS
        </p>
        <div className="flex flex-col gap-4">
          <ToggleRow
            icon={<Zap size={15} style={{ color:"#BB86FC" }} />}
            label="Remove Background Noise"
            description="Noise gate · hiss cut · hum removal"
            enabled={removeNoise}
            onChange={setRemoveNoise}
            accentColor="#BB86FC"
          />
          <div style={{ height:"1px", background:"rgba(255,255,255,0.05)" }} />
          <ToggleRow
            icon={<Radio size={15} style={{ color:"#BB86FC" }} />}
            label="Dereverb — Remove Echo"
            description="Suppress room reflections & reverb tails"
            enabled={dereverb}
            onChange={setDereverb}
            accentColor="#BB86FC"
          />
          <div style={{ height:"1px", background:"rgba(255,255,255,0.05)" }} />
          <ToggleRow
            icon={<Layers size={15} style={{ color:"#03DAC6" }} />}
            label="Enhance Voice Clarity"
            description="Presence & consonant intelligibility boost"
            enabled={enhanceVoice}
            onChange={setEnhanceVoice}
            accentColor="#03DAC6"
          />
        </div>
      </motion.div>

      {/* ENHANCE button */}
      <motion.div
        className="w-full"
        initial={{ opacity:0, y:10 }} animate={{ opacity:1, y:0 }} transition={{ delay:0.2 }}
      >
        <motion.button
          onClick={() => onEnhance({ removeNoise, dereverb, enhanceVoice })}
          className="relative w-full rounded-xl overflow-hidden"
          style={{ height:"58px" }}
          whileHover={{ scale:1.015 }}
          whileTap={{ scale:0.98 }}
        >
          <div className="absolute inset-0" style={{ background:"linear-gradient(135deg,#BB86FC 0%,#7B5EA7 50%,#03DAC6 100%)", opacity:0.95 }} />
          <motion.div
            className="absolute inset-0"
            style={{ background:"linear-gradient(90deg,transparent 0%,rgba(255,255,255,0.15) 50%,transparent 100%)" }}
            animate={{ x:["-100%","200%"] }}
            transition={{ duration:2.5, repeat:Infinity, repeatDelay:1.5, ease:"easeInOut" }}
          />
          <span className="relative z-10 flex items-center justify-center gap-3" style={{ fontFamily:"Space Grotesk,sans-serif", fontSize:"16px", fontWeight:700, letterSpacing:"0.08em", color:"#fff" }}>
            <Zap size={18} fill="white" />
            ENHANCE {isVideo ? "VIDEO" : "AUDIO"}
          </span>
        </motion.button>
      </motion.div>
    </motion.div>
  );
}

interface ToggleRowProps {
  icon: React.ReactNode;
  label: string;
  description: string;
  enabled: boolean;
  onChange: (v: boolean) => void;
  accentColor: string;
}

function ToggleRow({ icon, label, description, enabled, onChange, accentColor }: ToggleRowProps) {
  return (
    <div className="flex items-center justify-between gap-4">
      <div className="flex items-center gap-3">
        <div className="flex items-center justify-center rounded-lg flex-shrink-0" style={{ width:"30px", height:"30px", background:`${accentColor}15`, border:`1px solid ${accentColor}20` }}>
          {icon}
        </div>
        <div>
          <p style={{ fontFamily:"Inter,sans-serif", fontSize:"13px", fontWeight:500, color:"rgba(255,255,255,0.8)" }}>
            {label}
          </p>
          <p style={{ fontFamily:"Inter,sans-serif", fontSize:"11px", color:"rgba(255,255,255,0.3)" }}>
            {description}
          </p>
        </div>
      </div>
      <button
        onClick={() => onChange(!enabled)}
        className="relative flex-shrink-0 rounded-full"
        style={{ width:"40px", height:"22px", background: enabled ? accentColor : "rgba(255,255,255,0.1)", border:`1px solid ${enabled ? accentColor : "rgba(255,255,255,0.15)"}`, boxShadow: enabled ? `0 0 12px ${accentColor}50` : "none", transition:"all 0.25s ease" }}
      >
        <div
          className="absolute top-[3px] rounded-full"
          style={{ width:"14px", height:"14px", background:"#fff", left: enabled ? "22px" : "3px", transition:"left 0.2s ease" }}
        />
      </button>
    </div>
  );
}
