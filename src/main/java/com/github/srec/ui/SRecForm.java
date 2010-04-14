package com.github.srec.ui;

import com.github.srec.RecorderEvent;
import com.github.srec.play.Player;
import com.github.srec.rec.EventSerializer;
import com.github.srec.rec.Recorder;
import com.github.srec.rec.RecorderEventListener;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

public class SRecForm {
    private static final Logger logger = Logger.getLogger(SRecForm.class);
    private JButton recordButton;
    private JButton playButton;
    private JTable eventsTbl;
    private JPanel mainPanel;
    private JButton launchButton;
    private JButton saveButton;
    private JButton openButton;

    private Recorder recorder = new Recorder();
    private Player player = new Player();

    protected EventsTableModel tableModel;

    protected JFrame frame;

    private Action openAction = new AbstractAction("Open") {
        @Override
        public void actionPerformed(ActionEvent e) {
            openScript();
        }
    };
    private Action saveAction = new AbstractAction("Save") {
        @Override
        public void actionPerformed(ActionEvent e) {
            saveScript();
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
        player.init();
        recordButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (recorder.isRecording()) {
                    recorder.setRecording(false);
                    recordButton.setText("Record");
                } else {
                    recorder.setRecording(true);
                    recordButton.setText("Stop");
                }
            }
        });
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
        saveButton.setAction(saveAction);
        openButton.setAction(openAction);
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.play(recorder.getEvents());
            }
        });

        // Setup mnemonics
        saveButton.setMnemonic(KeyEvent.VK_S);
        openButton.setMnemonic(KeyEvent.VK_O);
        playButton.setMnemonic(KeyEvent.VK_P);
        recordButton.setMnemonic(KeyEvent.VK_E);
    }

    public void showFrame(String[] args) {
        // Init frame
        frame = new JFrame("srec");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(createMenuBar());
        frame.setSize(400, 400);
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
                JOptionPane.showMessageDialog(frame, "Incorrect signature for main method", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!m.getReturnType().equals(void.class)) {
                JOptionPane.showMessageDialog(frame, "Incorrect signature for main method", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String[] newArgs = new String[args.length - 1];
            if (newArgs.length > 0) System.arraycopy(args, 1, newArgs, 0, newArgs.length);
            m.invoke(null, new Object[]{newArgs});
        } catch (ClassNotFoundException e1) {
            JOptionPane.showMessageDialog(frame, "Main class not found", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NoSuchMethodException e1) {
            JOptionPane.showMessageDialog(frame, "Main method not found", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (InvocationTargetException e1) {
            JOptionPane.showMessageDialog(frame, "Main method not found", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalAccessException e1) {
            JOptionPane.showMessageDialog(frame, "Main method not found", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openScript() {
        final JFileChooser fc = new JFileChooser();
        fc.addChoosableFileFilter(new FileNameExtensionFilter("Ruby files", "rb"));
        int returnVal = fc.showOpenDialog(mainPanel);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            try {
                List<RecorderEvent> events = EventSerializer.read(file);
                recorder.emptyEvents();
                recorder.addEvents(events);
            } catch (IOException e) {
                ErrorDialog.show(e);
            }
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
        } catch (IOException ex) {
            ErrorDialog.show(ex);
        }
    }

    private void shutdown() {
        recorder.shutdown();
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);

        final JMenuItem openMenuItem = new JMenuItem("Open", KeyEvent.VK_O);
        openMenuItem.setAction(openAction);
        file.add(openMenuItem);

        final JMenuItem saveMenuItem = new JMenuItem("Save", KeyEvent.VK_S);
        saveMenuItem.setAction(saveAction);
        file.add(saveMenuItem);

        menuBar.add(file);

        JMenu record = new JMenu("Record");
        record.setMnemonic(KeyEvent.VK_R);
        menuBar.add(record);

        return menuBar;
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
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(-1, 20), null, null, 0, false));
        panel3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel3.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        eventsTbl.setName("eventsTbl");
        scrollPane1.setViewportView(eventsTbl);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
