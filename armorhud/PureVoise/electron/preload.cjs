// ─────────────────────────────────────────────────────────────
//  PureVoice AI — Preload Script
//  Secure contextBridge between Electron main and React renderer
// ─────────────────────────────────────────────────────────────

const { contextBridge, ipcRenderer } = require('electron');

// Expose a safe, limited API to the renderer (no full Node.js access)
contextBridge.exposeInMainWorld('electronAPI', {
  // ── File operations ──────────────────────────────────────

  /**
   * Open native Save dialog, then write blob to disk.
   * Returns { success, filePath } | { success: false, error }
   */
  saveFile: async (blob, defaultName) => {
    const ext = defaultName.split('.').pop().toLowerCase();
    const isAudio = ['wav','mp3','flac','ogg'].includes(ext);

    const { canceled, filePath } = await ipcRenderer.invoke('save-file-dialog', {
      defaultName,
      filters: isAudio
        ? [
            { name: 'WAV Audio', extensions: ['wav'] },
            { name: 'All Files', extensions: ['*'] },
          ]
        : [
            { name: 'WebM Video', extensions: ['webm'] },
            { name: 'All Files', extensions: ['*'] },
          ],
    });

    if (canceled || !filePath) return { success: false, canceled: true };

    // Convert Blob → ArrayBuffer → send to main process to write
    const arrayBuffer = await blob.arrayBuffer();
    return ipcRenderer.invoke('write-file', {
      filePath,
      buffer: arrayBuffer,
    });
  },

  /**
   * Open a file in the system's default application
   */
  openFileExternal: (filePath) =>
    ipcRenderer.invoke('open-file-external', filePath),

  /**
   * Reveal file in Finder / Explorer
   */
  showInFolder: (filePath) =>
    ipcRenderer.invoke('show-in-folder', filePath),

  /**
   * Get the installed app version
   */
  getAppVersion: () =>
    ipcRenderer.invoke('get-app-version'),

  // ── Event listeners ──────────────────────────────────────

  /**
   * Called when user opens a file via File → Open menu
   * Passes the file path to the renderer
   */
  onOpenFile: (callback) => {
    ipcRenderer.on('open-file', (_event, filePath) => callback(filePath));
  },

  removeOpenFileListener: () => {
    ipcRenderer.removeAllListeners('open-file');
  },

  // ── Platform info ────────────────────────────────────────
  platform: process.platform,
  isElectron: true,
});
