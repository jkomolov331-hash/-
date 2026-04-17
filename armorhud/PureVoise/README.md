# PureVoice AI — Desktop App

Professional audio & video enhancement desktop application built with **React + Vite + Electron**.

---

## 🚀 Quick Start (IntelliJ IDEA 2025.2)

### Prerequisites

1. **Node.js 20+** — download from https://nodejs.org  
   *(Verify: `node -v` should show `v20.x.x` or higher)*
2. **npm 10+** — bundled with Node.js

### Setup in IntelliJ IDEA

1. **Open the project**  
   `File → Open` → select the `PureVoiceAI` folder

2. **Open the Terminal** inside IntelliJ  
   `View → Tool Windows → Terminal` (or `Alt+F12`)

3. **Install dependencies**
   ```bash
   npm install
   ```

4. **Run in development mode** (hot-reload, DevTools)
   ```bash
   npm run electron:dev
   ```

---

## 📦 Build Executable

### Windows (.exe installer + portable)
```bash
npm run electron:build:win
```
Output: `dist-electron/PureVoice AI Setup 1.0.0.exe`  
Also creates a portable `.exe` (no installation needed).

### macOS (.dmg)
```bash
npm run electron:build:mac
```

### Linux (.AppImage)
```bash
npm run electron:build:linux
```

### All platforms
```bash
npm run electron:build
```

> **Note:** Building for Windows on macOS/Linux (or vice versa) requires Wine or a CI environment (GitHub Actions). For Windows .exe, build on a Windows machine.

---

## 🏗️ Project Structure

```
PureVoiceAI/
├── electron/
│   ├── main.js          ← Electron main process (window, menus, IPC)
│   └── preload.js       ← Secure bridge: Electron ↔ React (contextBridge)
│
├── src/
│   ├── main.tsx         ← React app entry point
│   ├── app/
│   │   ├── App.tsx      ← Root component + state machine
│   │   ├── components/
│   │   │   ├── UploadZone.tsx      ← Drag & drop file picker
│   │   │   ├── FileReady.tsx       ← Enhancement options + launch
│   │   │   ├── ProcessingScreen.tsx← Animated progress + waveform
│   │   │   └── ResultScreen.tsx    ← Stats + native save dialog
│   │   └── utils/
│   │       └── audioProcessor.ts   ← 17-stage DSP engine
│   └── styles/
│       ├── index.css    ← Global reset + Electron drag regions
│       ├── tailwind.css ← Tailwind imports
│       ├── fonts.css    ← Font face declarations
│       └── theme.css    ← CSS variables (dark theme)
│
├── build/               ← App icons (icon.ico, icon.icns, icon.png)
├── index.html           ← HTML shell
├── vite.config.ts       ← Vite bundler config
├── tsconfig.json        ← TypeScript config
└── package.json         ← Scripts + electron-builder config
```

---

## 🎧 Audio Processing Pipeline

The DSP engine (`audioProcessor.ts`) uses the **Web Audio API** with a professional 17-stage chain:

| Stage | Filter | Purpose |
|-------|--------|---------|
| 1 | High-pass 10 Hz | DC blocker |
| 2 | High-pass 80 Hz | Sub-rumble removal |
| 3 | DynamicsCompressor | Wideband noise gate |
| 4 | Low-shelf 120 Hz | Room tone reduction |
| 5 | High-shelf 9 kHz | Hiss cut |
| 6 | Notch 50 Hz | EU mains hum |
| 7 | Notch 60 Hz | US mains hum |
| 8 | Notch 100 Hz | 2nd harmonic hum |
| 9 | Low-pass 8 kHz | Dereverb stage 1 |
| 10 | Low-pass 12 kHz | Dereverb stage 2 |
| 11 | Peaking 800 Hz | Flutter echo reduction |
| 12 | High-pass 150 Hz | Muddiness removal |
| 13 | Peaking 250 Hz | Warmth boost |
| 14 | Peaking 2.5 kHz | Presence / intelligibility |
| 15 | Peaking 4.5 kHz | Consonant clarity |
| 16 | High-shelf 12 kHz | Air / brightness |
| 17 | DynamicsCompressor | Glue compression |
| 18 | Limiter -1 dBFS | True-peak limiting |

**Audio files** — processed via `OfflineAudioContext` (faster than real-time).  
**Video files** — processed via `MediaRecorder` + canvas (real-time, preserves video track).

Output: WAV (PCM 16-bit) for audio, WebM (VP9 + Opus) for video.

---

## 🔧 Adding App Icons

Place your icon files in the `build/` directory:

- `build/icon.ico` — Windows (256×256 recommended)
- `build/icon.icns` — macOS
- `build/icon.png` — Linux (512×512 recommended)

You can generate all formats from a single PNG using: https://www.icoconverter.com

---

## ⚙️ Configuration

### electron-builder (package.json `"build"` section)
- Change `appId` to your own reverse-domain ID
- Change `productName` to your app name
- NSIS installer: one-click or multi-step, desktop shortcut options

### Window size
Edit `electron/main.js` → `new BrowserWindow({ width, height, minWidth, minHeight })`

### DSP defaults
Edit `src/app/utils/audioProcessor.ts` → `ProcessingOptions` defaults

---

## 🛡️ Security

- `contextIsolation: true` — renderer cannot access Node.js directly
- `nodeIntegration: false` — no Node.js in renderer process
- All file I/O is handled in the main process via IPC handlers
- CSP headers in `index.html` prevent XSS

---

## 📋 Available Scripts

| Command | Description |
|---------|-------------|
| `npm run dev` | Vite dev server only (browser) |
| `npm run build` | Build React app to `dist/` |
| `npm run electron:dev` | Dev mode with Electron + hot-reload |
| `npm run electron:build` | Build for current platform |
| `npm run electron:build:win` | Build Windows .exe |
| `npm run electron:build:mac` | Build macOS .dmg |
| `npm run electron:build:linux` | Build Linux .AppImage |
