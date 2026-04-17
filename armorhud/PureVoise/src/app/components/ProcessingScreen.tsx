import { useEffect, useRef, useState } from "react";
import { motion, AnimatePresence } from "motion/react";
import { Film } from "lucide-react";

interface ProcessingScreenProps {
  fileName: string;
  isVideo: boolean;
  progress: number;   // 0-100 — driven by real processor
  stage: string;      // current stage label from processor
}

export function ProcessingScreen({ fileName, isVideo, progress, stage }: ProcessingScreenProps) {
  const done = progress >= 100;

  return (
    <motion.div
      className="w-full flex flex-col items-center"
      initial={{ opacity: 0, scale: 0.97 }}
      animate={{ opacity: 1, scale: 1 }}
      exit={{ opacity: 0, scale: 0.97 }}
      transition={{ duration: 0.4 }}
      style={{ maxWidth: "520px" }}
    >
      {/* Header */}
      <div className="text-center mb-10">
        <div className="flex items-center justify-center gap-2 mb-2">
          {isVideo && <Film size={14} style={{ color: "#03DAC6" }} />}
          <motion.h1
            style={{ fontFamily:"Space Grotesk,sans-serif", fontSize:"36px", fontWeight:700, color:"rgba(255,255,255,0.95)", letterSpacing:"-0.02em" }}
            animate={{ opacity: done ? 1 : [0.85, 1, 0.85] }}
            transition={{ duration: 2, repeat: done ? 0 : Infinity }}
          >
            {done ? "Complete ✓" : isVideo ? "Enhancing Video…" : "Enhancing Audio…"}
          </motion.h1>
        </div>
        <p className="truncate" style={{ fontFamily:"Inter,sans-serif", fontSize:"13px", color:"rgba(255,255,255,0.3)", maxWidth:"400px" }}>
          {fileName}
        </p>
        {isVideo && !done && (
          <p style={{ fontFamily:"Inter,sans-serif", fontSize:"11px", color:"rgba(3,218,198,0.6)", marginTop:"4px" }}>
            Real-time processing · please keep this window open
          </p>
        )}
      </div>

      {/* AI Waveform Viz */}
      <WaveformViz progress={progress} done={done} isVideo={isVideo} />

      {/* Progress bar */}
      <div className="w-full mt-8">
        <div className="w-full rounded-full overflow-hidden" style={{ height:"3px", background:"rgba(255,255,255,0.06)" }}>
          <motion.div
            className="h-full rounded-full"
            style={{ background:"linear-gradient(90deg,#BB86FC,#03DAC6)", boxShadow:"0 0 8px rgba(187,134,252,0.6)" }}
            animate={{ width:`${progress}%` }}
            transition={{ duration: 0.15, ease:"linear" }}
          />
        </div>

        <div className="flex justify-between items-center mt-3">
          <AnimatePresence mode="wait">
            <motion.p
              key={stage}
              style={{ fontFamily:"Inter,sans-serif", fontSize:"13px", color:"rgba(255,255,255,0.5)" }}
              initial={{ opacity:0, y:5 }}
              animate={{ opacity:1, y:0 }}
              exit={{ opacity:0, y:-5 }}
              transition={{ duration:0.25 }}
            >
              {done ? "✓ Enhancement complete" : stage}
            </motion.p>
          </AnimatePresence>
          <p style={{ fontFamily:"Space Grotesk,sans-serif", fontSize:"13px", fontWeight:600, color:"#BB86FC" }}>
            {Math.round(progress)}%
          </p>
        </div>
      </div>

      {/* Stage dots */}
      <StageDots progress={progress} done={done} />
    </motion.div>
  );
}

// ── Waveform visualisation ────────────────────────────────────

function WaveformViz({ progress, done, isVideo }: { progress: number; done: boolean; isVideo: boolean }) {
  const BAR_COUNT = 52;
  const bars = Array.from({ length: BAR_COUNT }, (_, i) => i);
  const cleanness = progress / 100;

  return (
    <div className="relative flex items-center justify-center" style={{ width:"100%", height:"100px" }}>
      {/* Background glow */}
      <motion.div
        className="absolute"
        style={{ width:"320px", height:"80px", borderRadius:"50%", background:`radial-gradient(ellipse,rgba(187,134,252,${0.05+cleanness*0.09}) 0%,transparent 70%)`, filter:"blur(22px)" }}
        animate={{ opacity:[0.6,1,0.6] }}
        transition={{ duration:2.5, repeat:Infinity }}
      />

      <div className="relative flex items-center gap-[2px]" style={{ height:"80px" }}>
        {bars.map((i) => (
          <WaveBar key={i} index={i} total={BAR_COUNT} cleanness={cleanness} done={done} isVideo={isVideo} />
        ))}
      </div>

      {/* Done ring */}
      <AnimatePresence>
        {done && (
          <motion.div
            className="absolute flex items-center justify-center rounded-full"
            style={{ width:"80px", height:"80px", border:"2px solid #03DAC6", boxShadow:"0 0 20px rgba(3,218,198,0.5),0 0 40px rgba(3,218,198,0.2)" }}
            initial={{ scale:0, opacity:0 }}
            animate={{ scale:1, opacity:1 }}
            transition={{ type:"spring", stiffness:260, damping:20 }}
          >
            <motion.span
              style={{ fontSize:"28px" }}
              initial={{ scale:0 }}
              animate={{ scale:1 }}
              transition={{ delay:0.15, type:"spring", stiffness:300, damping:18 }}
            >
              ✓
            </motion.span>
          </motion.div>
        )}
      </AnimatePresence>
    </div>
  );
}

function WaveBar({
  index, total, cleanness, done, isVideo,
}: {
  index: number; total: number; cleanness: number; done: boolean; isVideo: boolean;
}) {
  const x = (index / (total - 1)) * Math.PI * 4;
  const cleanHeight = Math.abs(Math.sin(x)) * 50 + 4;

  const [noisyHeight, setNoisyHeight] = useState(() => 8 + Math.random() * 54);
  const timer = useRef<ReturnType<typeof setInterval> | null>(null);

  useEffect(() => {
    if (done) {
      if (timer.current) clearInterval(timer.current);
      return;
    }
    const speed = 70 + Math.random() * 130;
    timer.current = setInterval(() => setNoisyHeight(8 + Math.random() * 54), speed);
    return () => { if (timer.current) clearInterval(timer.current); };
  }, [done]);

  const height = done
    ? cleanHeight
    : noisyHeight * (1 - cleanness) + cleanHeight * cleanness;

  const ratio = index / total;
  const inCyan = ratio > 0.55;
  const baseColor = isVideo
    ? (inCyan ? "#03DAC6" : "#BB86FC")
    : (inCyan ? "#03DAC6" : "#BB86FC");

  const opacity = done ? 1 : 0.4 + cleanness * 0.55;

  return (
    <motion.div
      className="rounded-full"
      style={{
        width: "3px",
        background: baseColor,
        opacity,
        boxShadow: done ? `0 0 4px ${baseColor}90` : "none",
        transition: "background 0.5s ease, opacity 0.5s ease, box-shadow 0.5s ease",
      }}
      animate={{ height:`${height}px` }}
      transition={{ duration: done ? 0.6 : 0.1, ease:"easeInOut" }}
    />
  );
}

// ── Dots row ─────────────────────────────────────────────────

function StageDots({ progress, done }: { progress: number; done: boolean }) {
  const TOTAL = 5;
  const active = done ? TOTAL : Math.floor((progress / 100) * TOTAL);

  return (
    <div className="flex gap-2 mt-6">
      {Array.from({ length: TOTAL }, (_, i) => {
        const isPast   = i < active;
        const isCur    = i === active && !done;
        return (
          <motion.div
            key={i}
            className="rounded-full"
            style={{
              width:  isCur ? "20px" : "6px",
              height: "6px",
              background: done || isPast ? "#03DAC6" : isCur ? "#BB86FC" : "rgba(255,255,255,0.1)",
              boxShadow: isCur ? "0 0 8px rgba(187,134,252,0.7)" : isPast || done ? "0 0 5px rgba(3,218,198,0.4)" : "none",
              transition: "all 0.4s ease",
            }}
          />
        );
      })}
    </div>
  );
}
