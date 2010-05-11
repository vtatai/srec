package com.github.srec.jemmy;

import com.github.srec.Utils;
import com.github.srec.command.exception.IllegalParametersException;
import org.apache.log4j.Logger;
import org.netbeans.jemmy.*;
import org.netbeans.jemmy.operators.*;
import org.netbeans.jemmy.util.NameComponentChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.List;

import static org.testng.Assert.assertEquals;

/**
 * A DSL wrapper for Jemmy operators.
 *
 * @author Victor Tatai
 */
public class JemmyDSL {
    private static final Logger logger = Logger.getLogger(JemmyDSL.class);
    public enum ComponentType {
        text_field(JTextFieldOperator.class),
        combo_box(JComboBoxOperator.class),
        button(JButtonOperator.class),
        radio_button(JRadioButtonOperator.class),
        check_box(JCheckBoxOperator.class);

        private Class associatedClass;

        ComponentType(Class associatedClass) {
            this.associatedClass = associatedClass;
        }

        public Class getAssociatedClass() {
            return associatedClass;
        }
    }
    private static Container currentContainer;
    private static Properties props = new Properties();
    static {
        props.put("DialogWaiter.WaitDialogTimeout", "5000");
        props.put("FrameWaiter.WaitFrameTimeout", "5000");
        props.put("Waiter.WaitingTime", "5000");
        props.put("WindowWaiter.WaitWindowTimeout", "5000");
        props.put("JScrollBarOperator.WholeScrollTimeout", "5000");
        props.put("JSliderOperator.WholeScrollTimeout", "5000");
        props.put("JSplitPaneOperator.WholeScrollTimeout", "5000");
        props.put("ScrollbarOperator.WholeScrollTimeout", "5000");
        props.put("ComponentOperator.WaitComponentTimeout", "5000");
        props.put("ComponentOperator.WaiStateTimeout", "5000");
        props.put("JComboBoxOperator.WaitListTimeout", "5000");
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
        JemmyProperties.setCurrentOutput(new TestOut(System.in, (PrintStream) null, null));
        robotMode();
    }

    public static void robotMode() {
        JemmyProperties.setCurrentDispatchingModel(JemmyProperties.ROBOT_MODEL_MASK);
    }

    public static void dispatchingMode() {
        JemmyProperties.setCurrentDispatchingModel(JemmyProperties.QUEUE_MODEL_MASK);
    }

    public static boolean isRobotMode() {
        return JemmyProperties.getCurrentDispatchingModel() == JemmyProperties.ROBOT_MODEL_MASK;
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

    public static ComboBox comboBox(String locator) {
        return new ComboBox(locator);
    }

    public static Table table(String locator) {
        return new Table(locator);
    }

    /**
     * Finds a component and stores it under the given id. The component can later be used on other commands using the
     * locator "id=ID_ASSIGNED".
     *
     * @param locator The locator
     * @param findType The find type name
     * @param id The id
     * @return The component found
     */
    public static JComponentOperator find(String locator, String id, String findType) {
        return find(locator, id, translateFindType(findType));
    }

    private static Class translateFindType(String findType) {
        if (findType.equals(ComponentType.combo_box.name())) return ComponentType.combo_box.getAssociatedClass();
        if (findType.equals(ComponentType.text_field.name())) return ComponentType.text_field.getAssociatedClass();
        if (findType.equals(ComponentType.check_box.name())) return ComponentType.check_box.getAssociatedClass();
        if (findType.equals(ComponentType.radio_button.name())) return ComponentType.radio_button.getAssociatedClass();
        if (findType.equals(ComponentType.button.name())) return ComponentType.button.getAssociatedClass();
        throw new JemmyDSLException("Unsupported find type " + findType);
    }

    /**
     * Finds a component and stores it under the given id. The component can later be used on other commands using the
     * locator "id=ID_ASSIGNED".
     *
     * @param locator The locator
     * @param id The id
     * @param cl The type
     * @return The component found
     */
    private static <X extends JComponentOperator> X find(String locator, String id, Class<X> cl) {
        X x = find(locator, cl);
        idMap.put(id, x);
        return x;
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
            if (!(jlabel.getLabelFor() instanceof JTextField)) {
                throw new JemmyDSLException("Associated component for " + locator + " is not a JTextComponent");
            }
            component = newInstance(clazz, JTextField.class, (JTextField) jlabel.getLabelFor());
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

    private static <X extends JComponentOperator, Y> X newInstance(Class<X> clazz, Class<Y> componentClass, JComponent component) {
        try {
            Constructor<X> c = clazz.getConstructor(componentClass);
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
        }

        public Frame(JFrame frame) {
            component = new JFrameOperator(frame);
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
            component = new JDialogOperator(title);
        }

        public Dialog(JDialog dialog) {
            component = new JDialogOperator(dialog);
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

        public TextField type(char key) {
            component.typeKey(key);
            if (!isRobotMode()) {
                // This is a hack because type key in queue mode does not wait for events to be fired
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new JemmyDSLException(e);
                }
            }
            return this;
        }

        public TextField type(int key) {
            component.typeKey(key, ' ', 0);
            return this;
        }

        public String text() {
            return component.getText();
        }

        public void assertText(String text) {
            component.waitText(text);
        }

        public JTextFieldOperator getComponent() {
            return component;
        }
    }

    public static class ComboBox implements Component {
        private JComboBoxOperator component;

        public ComboBox(String locator) {
            component = find(locator, JComboBoxOperator.class);
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
            component = find(locator, JButtonOperator.class);
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
            component = find(locator, JTableOperator.class);
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
