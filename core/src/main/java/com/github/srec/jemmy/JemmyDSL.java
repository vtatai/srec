package com.github.srec.jemmy;

import com.github.srec.UnsupportedFeatureException;
import com.github.srec.util.Utils;
import org.apache.log4j.Logger;
import org.netbeans.jemmy.*;
import org.netbeans.jemmy.operators.*;
import org.netbeans.jemmy.util.NameComponentChooser;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.List;

/**
 * A DSL wrapper for Jemmy operators.
 *
 * @author Victor Tatai
 */
@SuppressWarnings({"UnusedDeclaration"})
public class JemmyDSL {
    private static final Logger logger = Logger.getLogger(JemmyDSL.class);

    public enum ComponentType {
        text_field(JTextFieldOperator.class),
        combo_box(JComboBoxOperator.class),
        button(JButtonOperator.class),
        radio_button(JRadioButtonOperator.class),
        check_box(JCheckBoxOperator.class),
        table(JTableOperator.class);

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
        props.put("ComponentOperator.WaitComponentEnabledTimeout", "5000");
        props.put("ComponentOperator.WaitComponentTimeout", "5000");
        props.put("ComponentOperator.WaitStateTimeout", "5000");
        props.put("DialogWaiter.WaitDialogTimeout", "10000");
        props.put("FrameWaiter.WaitFrameTimeout", "10000");
        props.put("JComboBoxOperator.WaitListTimeout", "10000");
        props.put("JScrollBarOperator.WholeScrollTimeout", "10000");
        props.put("JSliderOperator.WholeScrollTimeout", "10000");
        props.put("JSplitPaneOperator.WholeScrollTimeout", "10000");
        props.put("ScrollbarOperator.WholeScrollTimeout", "10000");
        props.put("Waiter.WaitingTime", "10000");
        props.put("WindowWaiter.WaitWindowTimeout", "10000");
    }

    private static List<java.awt.Container> ignored = new ArrayList<java.awt.Container>();
    private static ComponentMap componentMap = new DefaultComponentMap();

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

    public static ComponentMap getComponentMap() {
        return componentMap;
    }

    public static void setComponentMap(ComponentMap componentMap) {
        JemmyDSL.componentMap = componentMap;
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

    public static TextField textField(String locator) {
        return new TextField(locator);
    }

    public static TextArea textArea(String locator) {
        return new TextArea(locator);
    }

    public static Button button(String locator) {
        return new Button(locator);
    }

    public static ComboBox comboBox(String locator) {
        return new ComboBox(locator);
    }

    public static CheckBox checkBox(String locator) {
        return new CheckBox(locator);
    }

    public static Table table(String locator) {
        return new Table(locator);
    }

    /**
     * Finds a component and stores it under the given id. The component can later be used on other commands using the
     * locator "id=ID_ASSIGNED".
     *
     * @param locator  The locator
     * @param findType The find type name
     * @param id       The id
     * @return The component found
     */
    @SuppressWarnings({"unchecked"})
    public static Component find(String locator, String id, String findType) {
        JComponentOperator comp = find(locator, id, translateFindType(findType));
        return convertFind(comp);
    }

    private static Component convertFind(JComponentOperator comp) {
        if (comp instanceof JComboBoxOperator) return new ComboBox((JComboBoxOperator) comp);
        if (comp instanceof JTextComponentOperator) return new TextField((JTextFieldOperator) comp);
        if (comp instanceof JCheckBoxOperator) return new CheckBox((JCheckBoxOperator) comp);
        if (comp instanceof JRadioButtonOperator) return new RadioButton((JRadioButtonOperator) comp);
        if (comp instanceof JButtonOperator) return new Button((JButtonOperator) comp);
        if (comp instanceof AbstractButtonOperator) return new GenericButton((AbstractButtonOperator) comp);
        throw new JemmyDSLException("Unsupported find type " + comp);
    }

    private static Class translateFindType(String findType) {
        for (ComponentType componentType : ComponentType.values()) {
            if (findType.equals(componentType.name())) return componentType.getAssociatedClass();
        }
        throw new JemmyDSLException("Unsupported find type " + findType);
    }

    /**
     * Finds a component and stores it under the given id. The component can later be used on other commands using the
     * locator "id=ID_ASSIGNED".
     *
     * @param locator The locator
     * @param id      The id
     * @param cl      The type
     * @return The component found
     */
    private static <X extends JComponentOperator> X find(String locator, String id, Class<X> cl) {
        X x = find(locator, cl);
        if (id != null) componentMap.putComponent(id, x);
        return x;
    }

    public static void click(String locator) {
        find(locator, JComponentOperator.class).clickMouse();
    }

    @SuppressWarnings({"unchecked"})
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
            } else if (JComponentOperator.class.isAssignableFrom(clazz)) {
                // Hack, we assume that what was really meant was AbstractButtonOperator
                component = newInstance(clazz, container().getComponent(), new AbstractButtonOperator.AbstractButtonByLabelFinder(locatorMap.get("text")));
            } else {
                throw new JemmyDSLException("Unsupported component type for location by text locator: " + locator);
            }
        } else if (locatorMap.containsKey("id")) {
            final String id = locatorMap.get("id");
            JComponentOperator operator = componentMap.getComponent(locatorMap.get("id"));
            if (!clazz.isAssignableFrom(operator.getClass())) {
                throw new JemmyDSLException("Cannot convert component with " + locator + " from "
                        + operator.getClass().getName() + " to " + clazz.getName());
            }
            component = (X) operator;
        } else if (locatorMap.containsKey("title")) {
            if (JInternalFrameOperator.class.isAssignableFrom(clazz)) {
                component = newInstance(clazz, container().getComponent(), new JInternalFrameOperator.JInternalFrameByTitleFinder(locatorMap.get("title")));
            } else {
                throw new JemmyDSLException("Unsupported component type for location by text locator: " + locator);
            }
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
            Constructor<X> c = findConstructor(clazz, componentClass);
            return c.newInstance(component);
        } catch (Exception e) {
            // Check to see if the nested exception was caused by a regular Jemmy exception
            if (e.getCause() != null && e.getCause() instanceof JemmyException) throw (JemmyException) e.getCause();
            throw new JemmyDSLException(e);
        }
    }

    @SuppressWarnings({"unchecked"})
    private static <X, Y> Constructor<X> findConstructor(Class<X> clazz, Class<Y> componentClass) {
        Constructor<X>[] cs = (Constructor<X>[]) clazz.getConstructors();
        for (Constructor<X> c : cs) {
            final Class<?>[] types = c.getParameterTypes();
            if (types.length == 1 && types[0].isAssignableFrom(componentClass)) return c;
        }
        throw new JemmyDSLException("Could not find suitable constructor in class " + clazz.getCanonicalName());
    }

    public static JComponent getSwingComponentById(String id) {
        JComponentOperator op = componentMap.getComponent(id);
        return (JComponent) op.getSource();
    }

    public static Label label(String locator) {
        return new Label(find(locator, JLabelOperator.class));
    }

    public static TabbedPane tabbedPane(String locator) {
        return new TabbedPane(locator);
    }

    public static Slider slider(String locator) {
        return new Slider(locator);
    }

    public static InternalFrame internalFrame(String locator) {
        return new InternalFrame(locator);
    }

    /**
     * Gets the menu bar for the last activated frame.
     *
     * @return The menu bar
     */
    public static MenuBar menuBar() {
        return new MenuBar();
    }

    public static void waitEnabled(String locator, boolean enabled) {
        JComponentOperator op = find(locator, JComponentOperator.class);
        try {
            if (enabled) {
                op.waitComponentEnabled();
            } else {
                waitComponentDisabled(op);
            }
        } catch (InterruptedException e) {
            throw new JemmyDSLException(e);
        }
    }

    private static void waitComponentDisabled(final ComponentOperator op) throws InterruptedException {
        Waiter waiter = new Waiter(new Waitable() {
            public Object actionProduced(Object obj) {
                if (((java.awt.Component) obj).isEnabled()) {
                    return null;
                } else {
                    return obj;
                }
            }

            public String getDescription() {
                return ("Component description: " + op.getSource().getClass().toString());
            }
        });
        waiter.setOutput(op.getOutput());
        waiter.setTimeoutsToCloneOf(op.getTimeouts(), "ComponentOperator.WaitComponentEnabledTimeout");
        waiter.waitAction(op.getSource());
    }


    public static abstract class Component {
        public abstract ComponentOperator getComponent();

        public void assertEnabled() {
            try {
                getComponent().waitComponentEnabled();
            } catch (InterruptedException e) {
                throw new JemmyDSLException(e);
            }
        }

        public void assertDisabled() {
            try {
                waitComponentDisabled(getComponent());
            } catch (InterruptedException e) {
                throw new JemmyDSLException(e);
            }
        }
    }

    public static abstract class Container extends Component {
        public abstract ContainerOperator getComponent();
    }

    public static class Frame extends Container {
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

    public static class Dialog extends Container {
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

    public static class TextField extends Component {
        private JTextComponentOperator component;

        public TextField(String locator) {
            component = find(locator, JTextComponentOperator.class);
            component.setComparator(new Operator.DefaultStringComparator(true, true));
        }

        public TextField(JTextFieldOperator component) {
            this.component = component;
            component.setComparator(new Operator.DefaultStringComparator(true, true));
        }

        public TextField type(String text) {
            if (text.contains("\t") || text.contains("\r") || text.contains("\n")) {
                throw new IllegalParametersException("Text cannot contain \\t \\r \\n");
            }
            component.setVerification(false);
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

        public TextField typeSpecial(String specialString) {
            int key;
            if (specialString.equals("Tab")) key = KeyEvent.VK_TAB;
            else if (specialString.equals("End")) key = KeyEvent.VK_END;
            else throw new UnsupportedFeatureException("Type special for " + specialString + " not supported");
            type(key);
            return this;
        }

        public String text() {
            return component.getText();
        }

        public void assertText(String text) {
            component.waitText(text);
        }

        public JTextComponentOperator getComponent() {
            return component;
        }

        public TextField assertEmpty() {
            component.waitText("");
            return this;
        }
    }

    public static class TextArea extends Component {
        private JTextAreaOperator component;

        public TextArea(String locator) {
            component = find(locator, JTextAreaOperator.class);
        }

        public TextArea(JTextAreaOperator component) {
            this.component = component;
        }

        public TextArea type(String text) {
            if (text.contains("\t") || text.contains("\r") || text.contains("\n")) {
                throw new IllegalParametersException("Text cannot contain \\t \\r \\n");
            }
            component.setVerification(false);
            component.typeText(text);
            return this;
        }

        public String text() {
            return component.getText();
        }

        public JTextAreaOperator getComponent() {
            return component;
        }

        public TextArea assertEmpty() {
            component.waitText("");
            return this;
        }
    }

    public static class ComboBox extends Component {
        private JComboBoxOperator component;

        public ComboBox(String locator) {
            component = find(locator, JComboBoxOperator.class);
        }

        public ComboBox(JComboBoxOperator comp) {
            this.component = comp;
        }

        public void select(String text) {
            Map<String, String> selectedItem = Utils.parseLocator(text);
            if (selectedItem.containsKey("name")) component.selectItem(selectedItem.get("name"));
            else if (selectedItem.containsKey("index")) select(Integer.parseInt(selectedItem.get("index")));
            else throw new IllegalParametersException("Illegal parameters " + text + " for select command");
        }

        public void select(int index) {
            component.clickMouse(); // hack because sometimes combobox does not seem to open
            component.selectItem(index);
        }

        public JComboBoxOperator getComponent() {
            return component;
        }

        public void assertSelected(String text) {
            component.waitItemSelected(text);
        }
    }

    public static class GenericButton extends Component {
        protected AbstractButtonOperator component;

        protected GenericButton() {}

        public GenericButton(String locator) {
            component = find(locator, AbstractButtonOperator.class);
        }

        public GenericButton(AbstractButtonOperator component) {
            this.component = component;
        }

        public void click() {
            component.push();
        }
        
        @Override
        public AbstractButtonOperator getComponent() {
            return component;
        }
    }

    public static class Button extends GenericButton {
        public Button(String locator) {
            component = find(locator, JButtonOperator.class);
        }

        public Button(JButtonOperator component) {
            super(component);
        }

        @Override
        public JButtonOperator getComponent() {
            return (JButtonOperator) component;
        }
    }

    public static class CheckBox extends GenericButton {
        public CheckBox(String locator) {
            component = find(locator, JCheckBoxOperator.class);
        }

        public CheckBox(JCheckBoxOperator component) {
            super(component);
        }

        @Override
        public JCheckBoxOperator getComponent() {
            return (JCheckBoxOperator) component;
        }
    }

    public static class RadioButton extends GenericButton {
        public RadioButton(String locator) {
            component = find(locator, JRadioButtonOperator.class);
        }

        public RadioButton(JRadioButtonOperator component) {
            super(component);
        }

        @Override
        public JRadioButtonOperator getComponent() {
            return (JRadioButtonOperator) component;
        }
    }

    public static class Table extends Component {
        private JTableOperator component;

        public Table(String locator) {
            component = find(locator, JTableOperator.class);
        }

        public Row row(int index) {
            return new Row(component, index);
        }

        public TableHeader header() {
            return new TableHeader(component.getTableHeader());
        }

        public Table selectRows(int first, int last) {
            component.setRowSelectionInterval(first, last);
            return this;
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
            component.waitCell(value, index, col);
            return this;
        }

        public Row select() {
            component.setRowSelectionInterval(index, index);
            return this;
        }

        public Row assertSelected(final boolean selected) {
            component.waitState(new ComponentChooser() {
                @Override
                public boolean checkComponent(java.awt.Component comp) {
                    return ((JTable) comp).isRowSelected(index) == selected;
                }

                @Override
                public String getDescription() {
                    return null;
                }
            });
            return this;
        }
    }

    public static class TableHeader {
        private JTableHeaderOperator component;

        public TableHeader(JTableHeader swingComponent) {
            component = new JTableHeaderOperator(swingComponent);
        }

        public TableHeader assertTitle(final int col, final String title) {
            component.waitState(new ComponentChooser() {
                @Override
                public boolean checkComponent(java.awt.Component comp) {
                    return ((JTableHeader) comp).getColumnModel().getColumn(col).getHeaderValue().equals(title);
                }

                @Override
                public String getDescription() {
                    return null;
                }
            });
            return this;
        }
    }

    public static class Label {
        private JLabelOperator component;

        public Label(JLabelOperator component) {
            this.component = component;
        }

        public Label text(String text) {
            component.waitText(text);
            return this;
        }
    }

    public static class TabbedPane extends Component {
        private JTabbedPaneOperator component;

        public TabbedPane(String locator) {
            component = find(locator, JTabbedPaneOperator.class);
        }

        public TabbedPane select(String title) {
            component.selectPage(title);
            return this;
        }

        public JTabbedPaneOperator getComponent() {
            return component;
        }
    }

    public static class Slider extends Component {
        private JSliderOperator component;

        public Slider(String locator) {
            component = find(locator, JSliderOperator.class);
        }

        public Slider value(int i) {
            component.setValue(i);
            return this;
        }

        public Slider assertValue(final int i) {
            component.waitState(new ComponentChooser() {
                @Override
                public boolean checkComponent(java.awt.Component comp) {
                    return ((JSlider) comp).getValue() == i;
                }

                @Override
                public String getDescription() {
                    return null;
                }
            });
            return this;
        }

        @Override
        public ComponentOperator getComponent() {
            return component;
        }
    }

    public static class MenuBar extends Container {
        private JMenuBarOperator component;

        public MenuBar() {
            component = new JMenuBarOperator(currentContainer.getComponent());
        }

        public MenuBar clickMenu(int... indices) {
            if (indices.length == 0) return this;
            String[] texts = new String[indices.length];
            JMenu menu = component.getMenu(indices[0]);
            texts[0] = menu.getText();
            for (int i = 1; i < indices.length; i++) {
                int index = indices[i];
                assert menu != null;
                if (i == indices.length - 1) {
                    JMenuItem item = (JMenuItem) menu.getMenuComponent(index);
                    texts[i] = item.getText();
                    menu = null;
                } else {
                    menu = (JMenu) menu.getMenuComponent(index);
                    texts[i] = menu.getText();
                }
            }
            clickMenu(texts);
            return this;
        }

        public MenuBar clickMenu(String... texts) {
            if (texts.length == 0) return this;
            component.showMenuItem(texts[0]);
            for (int i = 1; i < texts.length; i++) {
                String text = texts[i];
                new JMenuOperator(currentContainer.getComponent(), texts[i - 1]).showMenuItem(new String[] {text});
            }
            new JMenuItemOperator(currentContainer.getComponent(), texts[texts.length - 1]).clickMouse();
            return this;
        }

        @Override
        public JMenuBarOperator getComponent() {
            return component;
        }
    }

    public static class Menu extends Container {
        private JMenuOperator component;

        public Menu() {
        }

        @Override
        public JMenuOperator getComponent() {
            return component;
        }
    }

    public static class InternalFrame extends Container {
        private JInternalFrameOperator component;

        public InternalFrame(String locator) {
            component = find(locator, JInternalFrameOperator.class);
        }

        public InternalFrame(JInternalFrame frame) {
            component = new JInternalFrameOperator(frame);
        }

        public InternalFrame close() {
            ((JInternalFrame) component.getSource()).doDefaultCloseAction();
            return this;
        }

        public InternalFrame hide() {
            component.setVisible(false);
            return this;
        }

        public InternalFrame show() {
            component.setVisible(true);
            return this;
        }

        public InternalFrame activate() {
            component.activate();
            return this;
        }

        public InternalFrame assertVisible(Boolean visible) {
            component.waitComponentVisible(visible);
            return this;
        }

        public JInternalFrameOperator getComponent() {
            return component;
        }
    }
}
