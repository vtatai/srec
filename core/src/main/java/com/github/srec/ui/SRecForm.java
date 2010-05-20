package com.github.srec.ui;

import com.github.srec.SRecException;
import com.github.srec.command.*;
import com.github.srec.jemmy.JemmyDSL;
import com.github.srec.rec.Recorder;
import com.github.srec.rec.RecorderCommandListener;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Victor Tatai
 */
public class SRecForm {
    private static final Logger logger = Logger.getLogger(SRecForm.class);
    /**
     * The limit for the recent files list (Reopen menu).
     */
    private static final int RECENT_FILES_LIMIT = 5;
    private JButton recordButton;
    private JButton playButton;
    private JTable eventsTbl;
    private JPanel mainPanel;
    private JButton launchButton;
    private JButton saveButton;
    private JButton openButton;
    private JTree fileTree;
    private JLabel statusBar;
    private DefaultMutableTreeNode root = new DefaultMutableTreeNode();
    private DefaultTreeModel treeModel = new DefaultTreeModel(root);

    private Recorder recorder;

    protected CommandsTableModel tableModel;

    private JMenuItem reopenMenu;

    private List<String> recentFiles = new ArrayList<String>();

    protected JFrame frame = new JFrame("srec");

    private Timer statusBarTimer;

    private File currentOpenFile;

    private boolean currentOpenFileDirty;

    private Action openAction = new AbstractAction("Open") {
        @Override
        public void actionPerformed(ActionEvent e) {
            load();
        }
    };
    private Action saveAction = new AbstractAction("Save") {
        @Override
        public void actionPerformed(ActionEvent e) {
            saveScript();
        }
    };
    private Action closeAction = new AbstractAction("Close") {
        @Override
        public void actionPerformed(ActionEvent e) {
            closeScript();
        }
    };
    private Action recordAction = new AbstractAction("Record") {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (recorder.isRecording()) {
                recorder.setRecording(false);
                recordButton.setText("Record");
            } else {
                recorder.setRecording(true);
                recordButton.setText("Stop");
            }
        }
    };
    private Action playAction = new AbstractAction("Play") {
        @Override
        public void actionPerformed(ActionEvent e) {
            play(recorder.getCommands());
        }
    };

    public SRecForm() {
        $$$setupUI$$$();
        launchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new LaunchDialog(frame).setVisible(true);
            }
        });
        recorder = new Recorder(ExecutionContextFactory.getInstance().create(null));
        recorder.init();
        JemmyDSL.init(frame);
        recorder.addListener(new RecorderCommandListener() {
            @Override
            public void eventAdded(CallEventCommand event) {
                tableModel.add(event);
            }

            @Override
            public void eventsRemoved() {
                tableModel.clear();
            }

            @Override
            public void commandsAdded(List<Command> commands) {
                tableModel.add(commands);
            }

            @Override
            public void eventsUpdated(int index, List<Command> commands) {
                tableModel.update(index, commands);
            }
        });
        recordButton.setAction(recordAction);
        saveButton.setAction(saveAction);
        openButton.setAction(openAction);
        playButton.setAction(playAction);

        // Setup mnemonics
        saveButton.setMnemonic(KeyEvent.VK_S);
        openButton.setMnemonic(KeyEvent.VK_O);
        playButton.setMnemonic(KeyEvent.VK_P);
        recordButton.setMnemonic(KeyEvent.VK_E);
        launchButton.setMnemonic(KeyEvent.VK_L);

        // Setup exception handler
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                error("Unexpected error: " + throwable.getMessage(), throwable);
            }
        });

        // Setup application-wide keyboard shortcuts
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent keyEvent) {
                if (keyEvent.getID() != KeyEvent.KEY_PRESSED) return false;
                if (!keyEvent.isControlDown()) return false;
                if (keyEvent.getKeyCode() == KeyEvent.VK_S) {
                    saveScript();
                    return true;
                } else if (keyEvent.getKeyCode() == KeyEvent.VK_O) {
                    load();
                    return true;
                }
                return false;
            }
        });
    }

    public void play(final List<Command> events) {
        Thread t = new Thread() {
            @Override
            public void run() {
                int currentIndex = eventsTbl.getSelectedRow();
                if (currentIndex < 0) currentIndex = 0;
                for (int i = currentIndex; i < events.size(); i++) {
                    Command command = events.get(i);
                    eventsTbl.getSelectionModel().setSelectionInterval(i, i);
                    play(command);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                status("Test played successfully!");
            }
        };
        t.start();
    }

    private void status(final String message) {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    statusBar.setText(message);
                }
            });
        } catch (InterruptedException e) {
            error(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            error(e.getMessage(), e);
        }
        statusBarTimer = new Timer(10000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                statusBar.setText("");
            }
        });
        statusBarTimer.start();
    }

    private void play(Command command) {
        command.run(recorder.getExecutionContext());
    }

    public void showFrame(String[] args) {
        // Init frame
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(createMenuBar());
        frame.setSize(800, 800);
        frame.setLocation(100, 100);
        frame.pack();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                shutdown();
            }
        });
        frame.setVisible(true);
        recorder.addIgnoredContainer(frame);

        // Run application
        runMainMethod(args);
    }

    private void runMainMethod(String[] args) {
        if (args == null || args.length == 0) return;
        try {
            Class cl = Class.forName(args[0]);
            Method m = cl.getMethod("main", String[].class);
            if (!Modifier.isStatic(m.getModifiers())) {
                error("Incorrect signature for main method", null);
                return;
            }
            if (!m.getReturnType().equals(void.class)) {
                error("Incorrect signature for main method", null);
                return;
            }
            String[] newArgs = new String[args.length - 1];
            if (newArgs.length > 0) System.arraycopy(args, 1, newArgs, 0, newArgs.length);
            m.invoke(null, new Object[]{newArgs});
        } catch (ClassNotFoundException e1) {
            error("Main class not found", e1);
        } catch (NoSuchMethodException e1) {
            error("Main method not found", e1);
        } catch (InvocationTargetException e1) {
            error("Main method not found", e1);
        } catch (IllegalAccessException e1) {
            error("Main method not found", e1);
        }
    }

    private void error(String message, Throwable throwable) {
        JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
        throwable.printStackTrace();
    }

    /**
     * Loads a file or directory by presenting the user a file chooser.
     */
    private void load() {
        try {
            final JFileChooser fc = new JFileChooser();
            fc.addChoosableFileFilter(new FileNameExtensionFilter("Ruby files", "rb"));
            fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int returnVal = fc.showOpenDialog(mainPanel);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                load(file);
                addRecentFile(file.getCanonicalPath());
            }
        } catch (IOException e) {
            error(e.getMessage(), e);
        }
    }

    /**
     * Loads a directory or a script file, handling the UI tree view.
     *
     * @param file The file or directory
     * @throws java.io.IOException In case there is an exception loading the file
     */
    public void load(File file) throws IOException {
        if (!file.exists()) {
            error("File does not exist", null);
            return;
        }
        if (file.isDirectory()) {
            List<UITestFileManager.FileNode> nodes = UITestFileManager.scanFiles(file);
            root.setUserObject(file.getCanonicalPath());
            root.removeAllChildren();
            for (UITestFileManager.FileNode node : nodes) {
                root.add(node);
            }
            treeModel.reload();
        } else {
            loadScript(file);
            if (file.getParentFile() != null) load(file.getParentFile());
        }
    }

    /**
     * Loads a script file.
     *
     * @param file The script file
     */
    private void loadScript(File file) {
        if (!file.exists()) {
            error("File does not exist", null);
            return;
        }
        assert !file.isDirectory();
        try {
            ExecutionContext executionContext = CommandSerializer.load(file);
            List<Command> commands = executionContext.getCommands();
            recorder.emptyCommands();
            recorder.addCommands(commands);
        } catch (SRecException e) {
            error("Error loading script", e);
        }
        setCurrentOpenFile(file);
    }

    /**
     * Loads (ie shows) a method def commands in the UI.
     *
     * @param m The method
     */
    private void loadMethod(MethodScriptCommand m) {
        List<Command> commands = m.getCommands();
        recorder.emptyCommands();
        recorder.addCommands(commands);
    }

    private void saveScript() {
        if (recorder.getCommands().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Nothing to save.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (currentOpenFile == null) {
            saveNewScript();
        } else if (currentOpenFileDirty) {
            saveOpenScript();
        }
    }

    private void saveNewScript() {
        final JFileChooser fc = new JFileChooser();
        fc.addChoosableFileFilter(new FileNameExtensionFilter("Ruby files", "rb"));
        fc.setApproveButtonText("Save");
        fc.setApproveButtonMnemonic(KeyEvent.VK_S);
        int returnVal = fc.showOpenDialog(mainPanel);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            saveScript(file);
            setCurrentOpenFileClean();
        }
    }

    private void saveOpenScript() {
        saveScript(currentOpenFile);
        setCurrentOpenFileClean();
    }

    private void saveScript(File file) {
        logger.debug("Saving: " + file.getName());
        try {
            CommandSerializer.write(file, recorder.getCommands());
        } catch (IOException e) {
            error("Error saving script", e);
        }
    }
    private void shutdown() {
        recorder.shutdown();
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        final JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);

        final JMenuItem openMenuItem = new JMenuItem("Open", KeyEvent.VK_O);
        openMenuItem.setAction(openAction);
        file.add(openMenuItem);

        final JMenuItem saveMenuItem = new JMenuItem("Save", KeyEvent.VK_S);
        saveMenuItem.setAction(saveAction);
        file.add(saveMenuItem);

        final JMenuItem closeMenuItem = new JMenuItem("Close", KeyEvent.VK_C);
        closeMenuItem.setAction(closeAction);
        file.add(closeMenuItem);
        closeAction.setEnabled(false);

        reopenMenu = new JMenu("Reopen");
        reopenMenu.setMnemonic(KeyEvent.VK_E);
        try {
            recentFiles = RecentFilesSerializer.read();
        } catch (IOException e) {
            error("Error reading recent files list", e);
        }
        updateReopenMenu();
        file.add(reopenMenu);

        menuBar.add(file);

        JMenu record = new JMenu("Recording");
        record.setMnemonic(KeyEvent.VK_R);
        menuBar.add(record);

        final JMenuItem recordMenuItem = new JMenuItem("Record", KeyEvent.VK_R);
        recordMenuItem.setAction(recordAction);
        record.add(recordMenuItem);

        final JMenuItem playMenuItem = new JMenuItem("Play", KeyEvent.VK_P);
        playMenuItem.setAction(playAction);
        record.add(playMenuItem);

        return menuBar;
    }

    private void updateReopenMenu() {
        reopenMenu.removeAll();
        for (String recentFile : recentFiles) {
            JMenuItem menuItem = new JMenuItem(recentFile);
            menuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JMenuItem menuItem = (JMenuItem) e.getSource();
                    try {
                        load(new File(menuItem.getText()));
                    } catch (IOException e1) {
                        error("Error loading script", e1);
                    }
                    addRecentFile(menuItem.getText());
                    updateReopenMenu();
                }
            });
            reopenMenu.add(menuItem);
        }
        if (reopenMenu.getParent() != null) ((JPopupMenu) reopenMenu.getParent()).pack();
    }

    private void addRecentFile(String fileName) {
        recentFiles.remove(fileName);
        recentFiles.add(0, fileName);
        if (recentFiles.size() > RECENT_FILES_LIMIT) recentFiles.remove(RECENT_FILES_LIMIT);
        try {
            RecentFilesSerializer.save(recentFiles);
        } catch (IOException e) {
            error("Error saving recent files list", e);
            e.printStackTrace();
        }
    }

    private void closeScript() {
        if (currentOpenFile == null) return;
        if (currentOpenFileDirty) {
            int option = JOptionPane.showConfirmDialog(frame, "Save modified file " + currentOpenFile.getName() + "?",
                    "Save modified file?", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                saveScript();
            }
        }
        currentOpenFile = null;
        currentOpenFileDirty = false;
        tableModel.clear();
        updateTitle();
    }

    private void setCurrentOpenFile(File file) {
        currentOpenFile = file;
        closeAction.setEnabled(true);
        setCurrentOpenFileClean();
    }

    private void updateTitle() {
        StringBuilder strb = new StringBuilder();
        if (currentOpenFileDirty) {
            strb.append("* ");
        }
        strb.append("srec");
        if (currentOpenFile != null) {
            try {
                strb.append(" ").append(currentOpenFile.getCanonicalPath());
            } catch (IOException e) {
                error(e.getMessage(), e);
            }
        }
        frame.setTitle(strb.toString());
    }

    private void setCurrentOpenFileDirty() {
        currentOpenFileDirty = true;
        updateTitle();
    }

    private void setCurrentOpenFileClean() {
        currentOpenFileDirty = false;
        updateTitle();
    }

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SRecForm().showFrame(args);
            }
        });
    }

    private void createUIComponents() {
        tableModel = new CommandsTableModel();
        eventsTbl = new JTable(tableModel);
        eventsTbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        fileTree = new JTree(treeModel);
        fileTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        fileTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) fileTree.getLastSelectedPathComponent();
                if (!(node instanceof UITestFileManager.FileNode || node instanceof UITestFileManager.MethodNode))
                    return;

                if (node instanceof UITestFileManager.FileNode) {
                    UITestFileManager.FileNode fileNode = (UITestFileManager.FileNode) node;
                    loadScript(fileNode.getFile());
                } else {
                    UITestFileManager.MethodNode methodNode = (UITestFileManager.MethodNode) node;
                    loadMethod(methodNode.getMethod());
                }
            }
        });
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.setPreferredSize(new Dimension(600, 600));
        final JToolBar toolBar1 = new JToolBar();
        mainPanel.add(toolBar1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 20), null, 0, false));
        recordButton = new JButton();
        recordButton.setName("recordBtn");
        recordButton.setText("Record");
        toolBar1.add(recordButton);
        playButton = new JButton();
        playButton.setName("playBtn");
        playButton.setText("Play");
        toolBar1.add(playButton);
        openButton = new JButton();
        openButton.setName("openBtn");
        openButton.setText("Open");
        toolBar1.add(openButton);
        saveButton = new JButton();
        saveButton.setName("saveBtn");
        saveButton.setText("Save");
        toolBar1.add(saveButton);
        launchButton = new JButton();
        launchButton.setName("launchBtn");
        launchButton.setText("Launch");
        toolBar1.add(launchButton);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        toolBar1.add(panel1);
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panel2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JSplitPane splitPane1 = new JSplitPane();
        splitPane1.setDividerLocation(200);
        panel2.add(splitPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        splitPane1.setRightComponent(scrollPane1);
        eventsTbl.setName("eventsTbl");
        scrollPane1.setViewportView(eventsTbl);
        final JScrollPane scrollPane2 = new JScrollPane();
        splitPane1.setLeftComponent(scrollPane2);
        scrollPane2.setViewportView(fileTree);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 5, 0, 0), -1, -1));
        mainPanel.add(panel3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 16), new Dimension(-1, 16), new Dimension(-1, 16), 0, false));
        statusBar = new JLabel();
        statusBar.setName("statusBar");
        statusBar.setText("");
        panel3.add(statusBar, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 15), new Dimension(-1, 15), new Dimension(-1, 20), 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
