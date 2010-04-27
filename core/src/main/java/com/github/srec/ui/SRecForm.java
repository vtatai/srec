package com.github.srec.ui;

import com.github.srec.play.Player;
import com.github.srec.rec.EventSerializer;
import com.github.srec.rec.Recorder;
import com.github.srec.rec.RecorderEvent;
import com.github.srec.rec.RecorderEventListener;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.event.*;
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
    private DefaultMutableTreeNode root = new DefaultMutableTreeNode();
    private DefaultTreeModel treeModel = new DefaultTreeModel(root);

    private Recorder recorder = new Recorder();
    private Player player = new Player();

    protected EventsTableModel tableModel;

    private JMenuBar menuBar;
    private JMenuItem reopenMenu;

    private List<String> recentFiles = new ArrayList<String>();

    protected JFrame frame = new JFrame("srec");

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
            play(recorder.getEvents());
        }
    };

    public SRecForm() {
        $$$setupUI$$$();
        launchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new LaunchDialog(frame).setVisible(true);
            }
        });
        recorder.init();
        player.init(frame);
        recorder.addListener(new RecorderEventListener() {
            @Override
            public void eventAdded(RecorderEvent event) {
                tableModel.add(event);
            }

            @Override
            public void eventsRemoved() {
                tableModel.clear();
            }

            @Override
            public void eventsAdded(List<RecorderEvent> events) {
                tableModel.add(events);
            }

            @Override
            public void eventsUpdated(int index, List<RecorderEvent> recorderEvents) {
                tableModel.update(index, recorderEvents);
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
    }

    public void play(final List<RecorderEvent> events) {
        Thread t = new Thread() {
            @Override
            public void run() {
                int currentIndex = eventsTbl.getSelectedRow();
                if (currentIndex < 0) currentIndex = 0;
                for (int i = currentIndex; i < events.size(); i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    RecorderEvent event = events.get(i);
                    eventsTbl.getSelectionModel().setSelectionInterval(i, i);
                    play(event);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };
        t.start();
    }

    private void play(RecorderEvent event) {
        int length = event.getComponentLocator() == null ? 0 : 1;
        length += event.getArgs().length;
        String[] args = new String[length];
        int dif = 0;
        if (event.getComponentLocator() != null) {
            args[0] = event.getComponentLocator();
            dif = 1;
        }
        for (int i = 0; i < event.getArgs().length; i++) {
            String arg = event.getArgs()[i];
            args[i + dif] = arg;
        }
        player.play(event.getCommand(), args);
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

    private void error(String message, Exception e) {
        JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }

    /**
     * Loads a file or directory by presenting the user a file chooser.
     */
    private void load() {
        final JFileChooser fc = new JFileChooser();
        fc.addChoosableFileFilter(new FileNameExtensionFilter("Ruby files", "rb"));
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int returnVal = fc.showOpenDialog(mainPanel);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            load(file);
        }
    }

    /**
     * Loads a directory or a script file, handling the UI tree view.
     *
     * @param file The file or directory
     */
    public void load(File file) {
        if (!file.exists()) {
            error("File does not exist", null);
            return;
        }
        if (file.isDirectory()) {
            List<UITestFileManager.FileNode> nodes = UITestFileManager.scanFiles(file);
            root.setUserObject(file.getAbsolutePath());
            root.removeAllChildren();
            for (UITestFileManager.FileNode node : nodes) {
                root.add(node);
            }
            treeModel.reload();
        } else {
            loadScript(file);
            if (file.getParentFile() != null) load(file.getParentFile());
        }
        try {
            addRecentFile(file.getCanonicalPath());
            updateReopenMenu();
        } catch (IOException e) {
            error("Unexpected error obtaining file path", e);
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
            List<RecorderEvent> events = EventSerializer.read(file);
            recorder.emptyEvents();
            recorder.addEvents(events);
        } catch (IOException e) {
            error("Error loading script", e);
        }
    }

    private void saveScript() {
        if (recorder.getEvents().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Nothing to save.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        final JFileChooser fc = new JFileChooser();
        fc.addChoosableFileFilter(new FileNameExtensionFilter("Ruby files", "rb"));
        fc.setApproveButtonText("Save");
        fc.setApproveButtonMnemonic(KeyEvent.VK_S);
        int returnVal = fc.showOpenDialog(mainPanel);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            saveScript(file);
        }
    }

    private void saveScript(File file) {
        logger.debug("Opening: " + file.getName());
        try {
            EventSerializer.write(file, recorder.getEvents());
        } catch (IOException e) {
            error("Error saving script", e);
        }
    }

    private void shutdown() {
        recorder.shutdown();
    }

    private JMenuBar createMenuBar() {
        menuBar = new JMenuBar();
        final JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);

        final JMenuItem openMenuItem = new JMenuItem("Open", KeyEvent.VK_O);
        openMenuItem.setAction(openAction);
        file.add(openMenuItem);

        final JMenuItem saveMenuItem = new JMenuItem("Save", KeyEvent.VK_S);
        saveMenuItem.setAction(saveAction);
        file.add(saveMenuItem);

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
                    load(new File(menuItem.getText()));
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

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SRecForm().showFrame(args);
            }
        });
    }

    private void createUIComponents() {
        tableModel = new EventsTableModel();
        eventsTbl = new JTable(tableModel);
        eventsTbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        fileTree = new JTree(treeModel);
        fileTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        fileTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) fileTree.getLastSelectedPathComponent();
                if (!(node instanceof UITestFileManager.FileNode)) return;
                UITestFileManager.FileNode fileNode = (UITestFileManager.FileNode) node;
                loadScript(fileNode.getFile());
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
        mainPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
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
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
