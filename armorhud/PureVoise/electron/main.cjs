// ─────────────────────────────────────────────────────────────
//  PureVoice AI — Electron Main Process
//  Controls: window, native menus, file dialogs, IPC
// ─────────────────────────────────────────────────────────────

const { app, BrowserWindow, ipcMain, dialog, Menu, shell, nativeTheme } = require('electron');
const path = require('path');
const fs = require('fs');

const isDev = process.env.NODE_ENV === 'development';

let mainWindow = null;

// ── Create main window ────────────────────────────────────────
function createWindow() {
  mainWindow = new BrowserWindow({
    width: 1100,
    height: 750,
    minWidth: 800,
    minHeight: 600,
    title: 'PureVoice AI',
    backgroundColor: '#101010',
    titleBarStyle: process.platform === 'darwin' ? 'hiddenInset' : 'default',
    frame: true,
    show: false, // show after ready-to-show for smooth startup
    icon: path.join(__dirname, '../build/icon.png'),
    webPreferences: {
      preload: path.join(__dirname, 'preload.cjs'),
      contextIsolation: true,
      nodeIntegration: false,
      sandbox: false,
      webSecurity: true,
      // Allow Web Audio API and MediaRecorder (needed for audio processing)
      experimentalFeatures: true,
    },
  });

  // Force dark color scheme for Web Audio API context
  nativeTheme.themeSource = 'dark';

  // Show window smoothly once loaded
  mainWindow.once('ready-to-show', () => {
    mainWindow.show();
    if (isDev) mainWindow.webContents.openDevTools({ mode: 'detach' });
  });

  // Load the app
  if (isDev) {
    mainWindow.loadURL('http://localhost:5173');
  } else {
    mainWindow.loadFile(path.join(__dirname, '../dist/index.html'));
  }

  // Handle external links in system browser
  mainWindow.webContents.setWindowOpenHandler(({ url }) => {
    if (url.startsWith('https://') || url.startsWith('http://')) {
      shell.openExternal(url);
    }
    return { action: 'deny' };
  });

  mainWindow.on('closed', () => { mainWindow = null; });

  buildMenu();
}

// ── Native application menu ───────────────────────────────────
function buildMenu() {
  const isMac = process.platform === 'darwin';

  const template = [
    // macOS app menu
    ...(isMac ? [{
      label: app.name,
      submenu: [
        { role: 'about' },
        { type: 'separator' },
        { role: 'hide' },
        { role: 'hideOthers' },
        { role: 'unhide' },
        { type: 'separator' },
        { role: 'quit' },
      ],
    }] : []),

    // File menu
    {
      label: 'File',
      submenu: [
        {
          label: 'Open Audio / Video…',
          accelerator: 'CmdOrCtrl+O',
          click: async () => {
            if (!mainWindow) return;
            const result = await dialog.showOpenDialog(mainWindow, {
              title: 'Open Audio or Video File',
              filters: [
                { name: 'Audio & Video', extensions: ['mp3','wav','flac','ogg','aac','m4a','mp4','mov','mkv','webm','avi','3gp'] },
                { name: 'Audio Files', extensions: ['mp3','wav','flac','ogg','aac','m4a','opus','wma'] },
                { name: 'Video Files', extensions: ['mp4','mov','mkv','webm','avi','3gp','m4v','ts'] },
                { name: 'All Files', extensions: ['*'] },
              ],
              properties: ['openFile'],
            });
            if (!result.canceled && result.filePaths.length > 0) {
              mainWindow.webContents.send('open-file', result.filePaths[0]);
            }
          },
        },
        { type: 'separator' },
        isMac ? { role: 'close' } : { role: 'quit' },
      ],
    },

    // Edit menu
    {
      label: 'Edit',
      submenu: [
        { role: 'undo' },
        { role: 'redo' },
        { type: 'separator' },
        { role: 'cut' },
        { role: 'copy' },
        { role: 'paste' },
        { role: 'selectAll' },
      ],
    },

    // View menu
    {
      label: 'View',
      submenu: [
        { role: 'reload' },
        { role: 'forceReload' },
        ...(isDev ? [{ role: 'toggleDevTools' }] : []),
        { type: 'separator' },
        { role: 'resetZoom' },
        { role: 'zoomIn' },
        { role: 'zoomOut' },
        { type: 'separator' },
        { role: 'togglefullscreen' },
      ],
    },

    // Window menu
    {
      label: 'Window',
      submenu: [
        { role: 'minimize' },
        { role: 'zoom' },
        ...(isMac ? [
          { type: 'separator' },
          { role: 'front' },
        ] : [{ role: 'close' }]),
      ],
    },

    // Help menu
    {
      role: 'help',
      submenu: [
        {
          label: 'About PureVoice AI',
          click: async () => {
            await dialog.showMessageBox(mainWindow, {
              type: 'info',
              title: 'About PureVoice AI',
              message: 'PureVoice AI v1.0.0',
              detail: 'Professional audio enhancement powered by Web Audio API DSP.\n\nAll processing is done locally on your device.\nNo audio data ever leaves your computer.',
              buttons: ['OK'],
            });
          },
        },
      ],
    },
  ];

  const menu = Menu.buildFromTemplate(template);
  Menu.setApplicationMenu(menu);
}

// ── IPC Handlers ──────────────────────────────────────────────

// Native save dialog (called from renderer via preload)
ipcMain.handle('save-file-dialog', async (event, { defaultName, filters }) => {
  if (!mainWindow) return { canceled: true };
  const result = await dialog.showSaveDialog(mainWindow, {
    title: 'Save Enhanced File',
    defaultPath: path.join(app.getPath('downloads'), defaultName),
    filters: filters || [
      { name: 'Audio Files', extensions: ['wav', 'mp3'] },
      { name: 'Video Files', extensions: ['webm', 'mp4'] },
      { name: 'All Files', extensions: ['*'] },
    ],
  });
  return result;
});

// Write file to disk from renderer (receives ArrayBuffer via IPC)
ipcMain.handle('write-file', async (event, { filePath, buffer }) => {
  try {
    const uint8 = new Uint8Array(buffer);
    await fs.promises.writeFile(filePath, uint8);
    return { success: true, filePath };
  } catch (err) {
    return { success: false, error: err.message };
  }
});

// Open file in system default app
ipcMain.handle('open-file-external', async (event, filePath) => {
  try {
    await shell.openPath(filePath);
    return { success: true };
  } catch (err) {
    return { success: false, error: err.message };
  }
});

// Show file in file explorer
ipcMain.handle('show-in-folder', async (event, filePath) => {
  shell.showItemInFolder(filePath);
  return { success: true };
});

// Get app version
ipcMain.handle('get-app-version', () => {
  return app.getVersion();
});

// ── App lifecycle ─────────────────────────────────────────────

app.whenReady().then(() => {
  createWindow();

  // macOS: re-create window when dock icon is clicked
  app.on('activate', () => {
    if (BrowserWindow.getAllWindows().length === 0) createWindow();
  });
});

app.on('window-all-closed', () => {
  if (process.platform !== 'darwin') app.quit();
});

// Security: prevent new window creation from renderer
app.on('web-contents-created', (event, contents) => {
  contents.on('will-navigate', (event, navigationUrl) => {
    const parsedUrl = new URL(navigationUrl);
    if (isDev && parsedUrl.origin === 'http://localhost:5173') return;
    if (!isDev) event.preventDefault();
  });
});
