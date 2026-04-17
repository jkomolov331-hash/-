import { useRef, useState, useCallback } from "react";
import { motion, AnimatePresence } from "motion/react";
import { Mic, Sparkles, Upload } from "lucide-react";

interface UploadZoneProps {
  onFileSelected: (file: File) => void;
}

export function UploadZone({ onFileSelected }: UploadZoneProps) {
  const inputRef = useRef<HTMLInputElement>(null);
  const [isDragOver, setIsDragOver] = useState(false);

  const handleDrop = useCallback(
    (e: React.DragEvent) => {
      e.preventDefault();
      setIsDragOver(false);
      const file = e.dataTransfer.files[0];
      if (file) onFileSelected(file);
    },
    [onFileSelected]
  );

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) onFileSelected(file);
  };

  return (
    <motion.div
      className="relative w-full flex flex-col items-center justify-center"
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.6, ease: [0.16, 1, 0.3, 1] }}
    >
      {/* Title area */}
      <div className="text-center mb-10">
        <motion.div
          className="flex items-center justify-center gap-2 mb-3"
          initial={{ opacity: 0, y: -10 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.1, duration: 0.5 }}
        >
          <span style={{ color: "#BB86FC", fontSize: "11px", letterSpacing: "0.2em", fontFamily: "Space Grotesk, sans-serif", fontWeight: 600 }}>
            POWERED BY DEEPFILTERNET AI
          </span>
        </motion.div>
        <motion.h1
          style={{
            fontFamily: "Space Grotesk, sans-serif",
            fontSize: "42px",
            fontWeight: 700,
            color: "rgba(255,255,255,0.95)",
            letterSpacing: "-0.02em",
            lineHeight: 1.1,
          }}
          initial={{ opacity: 0, y: -10 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.15, duration: 0.5 }}
        >
          Enhance Speech
        </motion.h1>
        <motion.p
          style={{
            fontFamily: "Inter, sans-serif",
            fontSize: "15px",
            color: "rgba(255,255,255,0.45)",
            marginTop: "8px",
          }}
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          transition={{ delay: 0.25, duration: 0.5 }}
        >
          Remove noise, echo, and artifacts from any audio recording
        </motion.p>
      </div>

      {/* Drop Zone */}
      <motion.div
        className="relative cursor-pointer w-full"
        style={{ maxWidth: "520px" }}
        onClick={() => inputRef.current?.click()}
        onDragOver={(e) => { e.preventDefault(); setIsDragOver(true); }}
        onDragLeave={() => setIsDragOver(false)}
        onDrop={handleDrop}
        whileHover={{ scale: 1.01 }}
        transition={{ type: "spring", stiffness: 300, damping: 30 }}
      >
        {/* Outer glow */}
        <AnimatePresence>
          {isDragOver && (
            <motion.div
              className="absolute inset-0 rounded-2xl pointer-events-none"
              style={{
                background: "radial-gradient(ellipse at center, rgba(187,134,252,0.15) 0%, transparent 70%)",
                boxShadow: "0 0 60px 20px rgba(187,134,252,0.12)",
              }}
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              exit={{ opacity: 0 }}
            />
          )}
        </AnimatePresence>

        {/* Main drop area */}
        <motion.div
          className="relative flex flex-col items-center justify-center rounded-2xl"
          style={{
            height: "260px",
            border: isDragOver
              ? "1.5px dashed rgba(187,134,252,0.8)"
              : "1.5px dashed rgba(255,255,255,0.12)",
            background: isDragOver
              ? "rgba(187,134,252,0.06)"
              : "rgba(255,255,255,0.02)",
            transition: "all 0.25s ease",
          }}
          animate={isDragOver ? { borderColor: "rgba(187,134,252,0.8)" } : {}}
        >
          {/* Mic icon with sparkles */}
          <div className="relative mb-6">
            <motion.div
              className="flex items-center justify-center rounded-full"
              style={{
                width: "72px",
                height: "72px",
                background: "rgba(187,134,252,0.1)",
                border: "1px solid rgba(187,134,252,0.2)",
              }}
              animate={
                isDragOver
                  ? { scale: [1, 1.08, 1], boxShadow: ["0 0 0px rgba(187,134,252,0)", "0 0 30px rgba(187,134,252,0.4)", "0 0 20px rgba(187,134,252,0.25)"] }
                  : {}
              }
              transition={{ duration: 1, repeat: isDragOver ? Infinity : 0 }}
            >
              <Mic size={32} style={{ color: "#BB86FC" }} />
            </motion.div>

            {/* Sparkle 1 */}
            <motion.div
              className="absolute -top-1 -right-1"
              animate={{ scale: [1, 1.4, 1], opacity: [0.6, 1, 0.6] }}
              transition={{ duration: 2, repeat: Infinity, delay: 0 }}
            >
              <Sparkles size={14} style={{ color: "#03DAC6" }} />
            </motion.div>

            {/* Sparkle 2 */}
            <motion.div
              className="absolute -bottom-0 -left-2"
              animate={{ scale: [1, 1.3, 1], opacity: [0.4, 0.9, 0.4] }}
              transition={{ duration: 2.5, repeat: Infinity, delay: 0.8 }}
            >
              <Sparkles size={10} style={{ color: "#BB86FC" }} />
            </motion.div>
          </div>

          {/* Text */}
          <p style={{ fontFamily: "Inter, sans-serif", fontSize: "15px", color: "rgba(255,255,255,0.6)", marginBottom: "4px" }}>
            Drag & drop your audio or video file here
          </p>
          <p style={{ fontFamily: "Inter, sans-serif", fontSize: "13px", color: "rgba(255,255,255,0.25)" }}>
            MP3, WAV, FLAC, OGG, MP4, MOV, MKV · up to 2 GB
          </p>

          {/* Upload button */}
          <motion.button
            className="flex items-center gap-2 mt-6 px-5 py-2 rounded-lg"
            style={{
              background: "rgba(187,134,252,0.12)",
              border: "1px solid rgba(187,134,252,0.25)",
              color: "#BB86FC",
              fontFamily: "Inter, sans-serif",
              fontSize: "13px",
              fontWeight: 500,
              letterSpacing: "0.02em",
            }}
            whileHover={{
              background: "rgba(187,134,252,0.2)",
              borderColor: "rgba(187,134,252,0.5)",
            }}
            whileTap={{ scale: 0.97 }}
            onClick={(e) => { e.stopPropagation(); inputRef.current?.click(); }}
          >
            <Upload size={14} />
            Browse files
          </motion.button>
        </motion.div>
      </motion.div>

      {/* Format tags */}
      <motion.div
        className="flex gap-2 mt-5 flex-wrap justify-center"
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        transition={{ delay: 0.4 }}
      >
        {["MP3", "WAV", "FLAC", "OGG", "MP4", "MOV", "MKV", "WebM", "AAC", "M4A"].map((fmt) => (
          <span
            key={fmt}
            style={{
              padding: "3px 8px",
              borderRadius: "4px",
              background: "rgba(255,255,255,0.04)",
              border: "1px solid rgba(255,255,255,0.07)",
              fontSize: "11px",
              fontFamily: "Inter, sans-serif",
              color: "rgba(255,255,255,0.3)",
              letterSpacing: "0.05em",
            }}
          >
            {fmt}
          </span>
        ))}
      </motion.div>

      <input
        ref={inputRef}
        type="file"
        accept="audio/*,video/*"
        className="hidden"
        onChange={handleChange}
      />
    </motion.div>
  );
}