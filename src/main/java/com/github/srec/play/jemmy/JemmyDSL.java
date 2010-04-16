package com.github.srec.play.jemmy;

import com.github.srec.Utils;
import com.github.srec.play.exception.IllegalParametersException;
import org.apache.log4j.Logger;
import org.netbeans.jemmy.*;
import org.netbeans.jemmy.operators.*;
import org.netbeans.jemmy.util.NameComponentChooser;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.List;

import static org.testng.Assert.assertEquals;

/**
 * A DSL wrapper for Jemmy operators.
 */
public class JemmyDSL {
    private static final Logger logger = Logger.getLogger(JemmyDSL.class);
    private static Container currentContainer;
    private static Properties props = new Properties();
    static {
        props.put("DialogWaiter.WaitDialogTimeout", "10000");
        props.put("FrameWaiter.WaitFrameTimeout", "10000");
        props.put("Waiter.WaitingTime", "10000");
        props.put("WindowWaiter.WaitWindowTimeout", "10000");
        props.put("JScrollBarOperator.WholeScrollTimeout", "10000");
        props.put("JSliderOperator.WholeScrollTimeout", "10000");
        props.put("JSplitPaneOperator.WholeScrollTimeout", "10000");
        props.put("ScrollbarOperator.WholeScrollTimeout", "10000");
        props.put("ComponentOperator.WaitComponentTimeout", "10000");
    }
    private static List<java.awt.Container> ignored = new ArrayList<java.awt.Container>();
    private static Map<String, JComponentOperator> idMap = new HashMap<String, JComponentOperator>();

    public static void init(java.awt.Container... ignored) {
        Timeouts timeouts = JemmyProperties.getCurrentTimeouts();
        for (Map.Entry<Object, Object> entry : props.entrySet()) {
            timeouts.setTimeout((String) entry.getKey(), Long.parseLong((String) entry.getValue()));
        }
        currentContainer = null;
        JemmyDSL.ignored = Arrays.asList(ignored);
    }

    public static Frame frame(String title) {
        Frame frame = new Frame(title);
        currentContainer = frame;
        return frame;
    }

    public static Frame frame() {
        return (Frame) currentContainer;
    }

    public static Container container() {
        if (currentContainer == null) {
            logger.info("No current container found, trying to find one.");
            // Try to find a container
            Window[] windows = JFrame.getWindows();
            for (Window w : windows) {
                if (ignored.contains(w)) continue;
                if (w instanceof JFrame) {
                    currentContainer = new Frame((JFrame) w);
                    break;
                } else if (w instanceof JDialog) {
                    currentContainer = new Dialog((JDialog) w);
                    break;
                } else {
                    logger.info("Found a window which is neither a JFrame nor JDialog");
                }
            }
            logger.info("Using as current container: " + currentContainer.getComponent().getSource());
        }
        return currentContainer;
    }

    public static Container container(JFrame frame) {
        currentContainer = new Frame(frame);
        return currentContainer;
    }

    public static Dialog dialog(String title) {
        final Dialog dialog = new Dialog(title);
        currentContainer = dialog;
        return dialog;
    }

    public static TextField textField(String name) {
        return new TextField(name);
    }

    public static Button button(String locator) {
        return new Button(locator);
    }

    public static ComboBox comboBox(String name) {
        return new ComboBox(name);
    }

    public static Table table(String locator) {
        return new Table(locator);
    }

    /**
     * Finds a component and stores it under the given id. The component can later be used on other commands using the
     * locator "id=ID_ASSIGNED".
     *
     * @param locator The locator
     * @param id The id
     * @return The component found
     */
    public static <X extends JComponentOperator> X find(String locator, Class<X> cl, String id) {
        X x = find(locator, cl);
        idMap.put(id, x);
        return x;
    }

    public static void assertText(String locator, String text) {
        JTextComponentOperator component = find(locator, JTextComponentOperator.class);
        assertEquals(component.getText(), text);
    }

    public static void click(String locator) {
        find(locator, AbstractButtonOperator.class).push();
    }

    public static <X extends JComponentOperator> X find(String locator, Class<X> clazz) {
        Map<String, String> locatorMap = Utils.parseLocator(locator);
        X component;
        if (locatorMap.containsKey("name")) {
            component = newInstance(clazz, container().getComponent(), new NameComponentChooser(locator));
        } else if (locatorMap.containsKey("label")) {
            JLabelOperator jlabel = new JLabelOperator(container().getComponent(), locatorMap.get("label"));
            if (!(jlabel.getLabelFor() instanceof JTextComponent)) {
                throw new JemmyDSLException("Associated component for " + locator + " is not a JTextComponent");
            }
            component = newInstance(clazz, (AbstractButton) jlabel.getLabelFor());
        } else if (locatorMap.containsKey("text")) {
            if (JTextComponentOperator.class.isAssignableFrom(clazz)) {
                component = newInstance(clazz, container().getComponent(), new JTextComponentOperator.JTextComponentByTextFinder(locatorMap.get("text")));
            } else if (AbstractButtonOperator.class.isAssignableFrom(clazz)) {
                component = newInstance(clazz, container().getComponent(), new AbstractButtonOperator.AbstractButtonByLabelFinder(locatorMap.get("text")));
            } else {
                throw new JemmyDSLException("Unsupported component type for location by text locator: " + locator);
            }
        } else if (locatorMap.containsKey("id")) {
            JComponentOperator operator = idMap.get(locatorMap.get("id"));
            if (!clazz.isAssignableFrom(operator.getClass())) {
                throw new JemmyDSLException("Cannot convert component with " + locator + " from "
                        + operator.getClass().getName() + "to " + clazz.getName());
            }
            component = (X) operator;
        } else {
            throw new JemmyDSLException("Unsupported locator: " + locator);
        }
        return component;
    }

    private static <X extends JComponentOperator> X newInstance(Class<X> clazz, ContainerOperator parent,
                                                                ComponentChooser chooser) {
        try {
            Constructor<X> c = clazz.getConstructor(ContainerOperator.class, ComponentChooser.class);
            return c.newInstance(parent, chooser);
        } catch (Exception e) {
            // Check to see if the nested exception was caused by a regular Jemmy exception
            if (e.getCause() != null && e.getCause() instanceof JemmyException) throw (JemmyException) e.getCause();
            throw new JemmyDSLException(e);
        }
    }

    private static <X extends JComponentOperator> X newInstance(Class<X> clazz, JComponent component) {
        try {
            Constructor<X> c = clazz.getConstructor(component.getClass());
            return c.newInstance(component);
        } catch (Exception e) {
            // Check to see if the nested exception was caused by a regular Jemmy exception
            if (e.getCause() != null && e.getCause() instanceof JemmyException) throw (JemmyException) e.getCause();
            throw new JemmyDSLException(e);
        }
    }

    private interface Component {
        ComponentOperator getComponent();
    }

    private interface Container extends Component {
        ContainerOperator getComponent();
    }

    public static class Frame implements Container {
        private JFrameOperator component;

        public Frame(String title) {
            component = new JFrameOperator(title);
            component.setOutput(new TestOut(System.in, (PrintStream) null, null));
        }

        public Frame(JFrame frame) {
            component = new JFrameOperator(frame);
            component.setOutput(new TestOut(System.in, (PrintStream) null, null));
        }

        public Frame close() {
            component.requestClose();
            return this;
        }

        public Frame activate() {
            component.activate();
            return this;
        }

        public JFrameOperator getComponent() {
            return component;
        }
    }

    public static class Dialog implements Container {
        private JDialogOperator component;

        public Dialog(String title) {
            component = new JDialogOperator((WindowOperator) container().getComponent(), title);
            component.setOutput(new TestOut(System.in, (PrintStream) null, null));
        }

        public Dialog(JDialog dialog) {
            component = new JDialogOperator(dialog);
            component.setOutput(new TestOut(System.in, (PrintStream) null, null));
        }

        public Dialog close() {
            component.requestClose();
            return this;
        }

        public JDialogOperator getComponent() {
            return component;
        }
    }

    public static class TextField implements Component {
        private JTextFieldOperator component;

        public TextField(String locator) {
            component = find(locator, JTextFieldOperator.class);
        }

        public TextField type(String text) {
            if (text.contains("\t") || text.contains("\r") || text.contains("\n")) {
                throw new IllegalParametersException("Text cannot contain \\t \\r \\n");
            }
            component.typeText(text);
            return this;
        }

        public TextField type(int key) {
            component.pressKey(key);
            return this;
        }

        public String text() {
            return component.getText();
        }

        public JTextFieldOperator getComponent() {
            return component;
        }
    }

    public static class ComboBox implements Component {
        private JComboBoxOperator component;

        public ComboBox(String name) {
            component = new JComboBoxOperator(container().getComponent(), new NameComponentChooser(name));
        }

        public void select(String text) {
            component.selectItem(text);
        }

        public void select(int index) {
            component.selectItem(index);
        }

        public JComboBoxOperator getComponent() {
            return component;
        }
    }

    public static class Button implements Component {
        private JButtonOperator component;

        public Button(String locator) {
            component = new JButtonOperator(container().getComponent(), parseLocator(locator));
        }

        private ComponentChooser parseLocator(String locator) {
            if (locator.indexOf('=') == -1) return new NameComponentChooser(locator);
            locator = locator.trim();
            String[] locatorParsed = locator.split("=");
            if (locatorParsed.length != 2)
                throw new IllegalArgumentException("Could not understand locator: " + locator);
            if (locatorParsed[0].trim().equals("text"))
                return new AbstractButtonOperator.AbstractButtonByLabelFinder(locatorParsed[1].trim());
            throw new IllegalArgumentException("Could not understand locator: " + locator);
        }

        public void click() {
            component.push();
        }

        public JButtonOperator getComponent() {
            return component;
        }
    }

    public static class Table implements Component {
        private JTableOperator component;

        public Table(String locator) {
            component = new JTableOperator(container().getComponent(), parseLocator(locator));
        }

        private ComponentChooser parseLocator(String locator) {
            if (locator.indexOf('=') == -1) return new NameComponentChooser(locator);
            throw new IllegalParametersException("Locator " + locator + " not supported.");
        }

        public Row row(int index) {
            return new Row(component, index);
        }

        public JTableOperator getComponent() {
            return component;
        }
    }

    public static class Row {
        private JTableOperator component;
        private int index;

        public Row(JTableOperator component, int index) {
            this.component = component;
            this.index = index;
        }

        public Row assertColumn(int col, String value) {
            String realValue = component.getValueAt(index, col).toString();
            assertEquals(realValue, value);
            return this;
        }
    }
}
