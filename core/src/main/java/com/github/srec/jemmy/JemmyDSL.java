package com.github.srec.jemmy;

import static org.apache.commons.lang.StringUtils.isBlank;

import java.awt.FontMetrics;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JTree;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.text.JTextComponent;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;
import org.netbeans.jemmy.ComponentChooser;
import org.netbeans.jemmy.ComponentSearcher;
import org.netbeans.jemmy.JemmyException;
import org.netbeans.jemmy.JemmyProperties;
import org.netbeans.jemmy.TestOut;
import org.netbeans.jemmy.Timeouts;
import org.netbeans.jemmy.Waitable;
import org.netbeans.jemmy.Waiter;
import org.netbeans.jemmy.operators.AbstractButtonOperator;
import org.netbeans.jemmy.operators.ComponentOperator;
import org.netbeans.jemmy.operators.ContainerOperator;
import org.netbeans.jemmy.operators.JButtonOperator;
import org.netbeans.jemmy.operators.JCheckBoxOperator;
import org.netbeans.jemmy.operators.JComboBoxOperator;
import org.netbeans.jemmy.operators.JComponentOperator;
import org.netbeans.jemmy.operators.JDialogOperator;
import org.netbeans.jemmy.operators.JFrameOperator;
import org.netbeans.jemmy.operators.JInternalFrameOperator;
import org.netbeans.jemmy.operators.JLabelOperator;
import org.netbeans.jemmy.operators.JMenuBarOperator;
import org.netbeans.jemmy.operators.JMenuItemOperator;
import org.netbeans.jemmy.operators.JMenuOperator;
import org.netbeans.jemmy.operators.JProgressBarOperator;
import org.netbeans.jemmy.operators.JRadioButtonOperator;
import org.netbeans.jemmy.operators.JScrollBarOperator;
import org.netbeans.jemmy.operators.JScrollPaneOperator;
import org.netbeans.jemmy.operators.JSliderOperator;
import org.netbeans.jemmy.operators.JTabbedPaneOperator;
import org.netbeans.jemmy.operators.JTableHeaderOperator;
import org.netbeans.jemmy.operators.JTableOperator;
import org.netbeans.jemmy.operators.JTextAreaOperator;
import org.netbeans.jemmy.operators.JTextComponentOperator;
import org.netbeans.jemmy.operators.JTextFieldOperator;
import org.netbeans.jemmy.operators.JToggleButtonOperator;
import org.netbeans.jemmy.operators.JTreeOperator;
import org.netbeans.jemmy.operators.Operator;
import org.netbeans.jemmy.operators.Operator.StringComparator;
import org.netbeans.jemmy.util.NameComponentChooser;

import antlr.debug.misc.JTreeASTPanel;

import com.github.srec.UnsupportedFeatureException;
import com.github.srec.command.exception.AssertionFailedException;
import com.github.srec.util.AWTTreeScanner;
import com.github.srec.util.ScannerMatcher;
import com.github.srec.util.Utils;

/**
 * A DSL wrapper for Jemmy operators.
 *
 * @author Victor Tatai
 */
public class JemmyDSL {
    private static final Logger logger = Logger.getLogger(JemmyDSL.class);
    private static final StringComparator comparator = new Operator.DefaultStringComparator(true,false);

    public enum ComponentType {
        text_field(JTextFieldOperator.class, JTextField.class),
        combo_box(JComboBoxOperator.class, JComboBox.class),
        label(JLabelOperator.class, JLabel.class),
        progress_bar(JProgressBarOperator.class, JProgressBar.class),
        tabbed_pane(JTabbedPaneOperator.class, JTabbedPane.class),
        table_header(JTableHeaderOperator.class, JTableHeader.class),
        button(JButtonOperator.class, JButton.class),
        radio_button(JRadioButtonOperator.class, JRadioButton.class),
        check_box(JCheckBoxOperator.class, JCheckBox.class),
        table(TableOperator.class, JTable.class),
        menu_bar(JMenuBarOperator.class, JMenuBar.class),
        dialog(JDialogOperator.class, JDialog.class),
        text_area(JTextAreaOperator.class, JTextArea.class),
        scroll_pane(JScrollPaneOperator.class, JScrollPane.class),
        tree(JTreeOperator.class, JTree.class);

        private final Class<? extends ComponentOperator> operatorClass;
        private final Class<? extends java.awt.Component> awtClass;

        ComponentType(Class<? extends ComponentOperator> operatorClass,
                      Class<? extends java.awt.Component> awtClass) {
            this.operatorClass = operatorClass;
            this.awtClass = awtClass;
        }

        public Class<? extends ComponentOperator> getOperatorClass() {
            return operatorClass;
        }

        public Class<? extends java.awt.Component> getAwtClass() {
            return awtClass;
        }
    }

    private static Window currentWindow;
    private static Properties props = new Properties();

    static {
        props.put("ComponentOperator.WaitComponentEnabledTimeout", "15000");
        props.put("ComponentOperator.WaitComponentTimeout", "30000");
        props.put("ComponentOperator.WaitStateTimeout", "10000");
        props.put("DialogWaiter.WaitDialogTimeout", "10000");
        props.put("FrameWaiter.WaitFrameTimeout", "30000");
        props.put("JComboBoxOperator.WaitListTimeout", "30000");
        props.put("JScrollBarOperator.WholeScrollTimeout", "10000");
        props.put("JSliderOperator.WholeScrollTimeout", "10000");
        props.put("JSplitPaneOperator.WholeScrollTimeout", "10000");
        props.put("ScrollbarOperator.WholeScrollTimeout", "10000");
        props.put("Waiter.WaitingTime", "10000");
        props.put("WindowWaiter.WaitWindowTimeout", "30000");
    }

    private static List<java.awt.Container> ignored = new ArrayList<java.awt.Container>();
    private static ComponentMap componentMap = new DefaultComponentMap();

    public static void init(java.awt.Container... ignored) {
        Timeouts timeouts = JemmyProperties.getCurrentTimeouts();
        for (Map.Entry<Object, Object> entry : props.entrySet()) {
            timeouts.setTimeout((String) entry.getKey(), Long.parseLong((String) entry.getValue()));
        }
        currentWindow = null;
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
        currentWindow = frame;
        return frame;
    }

    public static Frame frame() {
        return (Frame) currentWindow;
    }

    public static Window currentWindow() {
        if (currentWindow == null) {
            logger.info("No current container found, trying to find one.");
            currentWindow = findActiveWindow();
        } else if (!currentWindow.isActive()) {
            currentWindow = findActiveWindow();
        }
        if (currentWindow == null) {
			throw new JemmyDSLException("Cannot find a currently active window");
		}
        logger.info("Using as current container: " + currentWindow.getComponent());
        return currentWindow;
    }

    private static Window findActiveWindow() {
        java.awt.Window[] windows = JFrame.getWindows();
        for (java.awt.Window w : windows) {
            if (ignored.contains(w)) {
				continue;
			}
            if (!w.isActive()) {
				continue;
			}
            if (w instanceof JFrame) {
                return new Frame((JFrame) w);
            } else if (w instanceof JDialog) {
                return new Dialog((JDialog) w);
            } else {
                logger.info("Found a window which is neither a JFrame nor JDialog");
            }
        }
        return null;
    }

    public static Container container(JFrame frame) {
        currentWindow = new Frame(frame);
        return currentWindow;
    }

    public static Dialog dialog(String title) {
        final Dialog dialog = new Dialog(title);
        currentWindow = dialog;
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

    public static Tree tree(String locator) {
        return new Tree(locator);
    }

    /**
     * Finds a component and stores it under the given id. The component can later be used on other
     * commands using the locator "id=ID_ASSIGNED". This method searches both VISIBLE and INVISIBLE
     * components.
     *
     * @param locator The locator (accepted are name (default), title, text, label)
     * @param id The id
     * @param componentType The component type
     * @return The component found
     */
    public static Component find(String locator, String id, String componentType, boolean required) {
        java.awt.Component component = findComponent(locator, currentWindow().getComponent()
            .getSource());
        if (component == null) {
        	if (!required) {
        		componentMap.putComponent(id, null);
        		return null;
        	} else {
        		throw new JemmyDSLException("Component not found");
        	}
        }
        JComponentOperator operator = convertFind(component);
        componentMap.putComponent(id, operator);
        final Component finalComponent = convertFind(operator);
        if (finalComponent instanceof Window) {
            currentWindow = (Window) finalComponent;
        }
        return finalComponent;
    }

    @SuppressWarnings({"unchecked"})
    private static Class<? extends java.awt.Component> translateFindType(String findType) {
        for (ComponentType componentType : ComponentType.values()) {
            if (findType.equals(componentType.name())) {
				return componentType.getAwtClass();
			}
        }
        try {
            return (Class<? extends java.awt.Component>) Class.forName(findType);
        } catch (ClassNotFoundException e) {
            throw new JemmyDSLException("Unsupported find type " + findType);
        }
    }

    public static java.awt.Component findComponent(String locator, java.awt.Component component) {
        assert locator != null;
        String[] strs = parseLocator(locator);
        if (strs.length != 2) {
			throw new JemmyDSLException("Invalid locator " + locator);
		}
        if (strs[0].equals("id")) {
            return componentMap.getComponent(strs[1]).getSource();
        } else {
            return AWTTreeScanner.scan(component, compileMatcher(strs));
        }
    }

    private static String[] parseLocator(String locator) {
        int i = locator.indexOf("=");
        if (i == -1) {
            return new String[]{"name", locator.substring(i + 1).trim()};
        }
        return new String[]{locator.substring(0, i).trim(), locator.substring(i + 1).trim()};
    }

    private static ScannerMatcher compileMatcher(String[] strs) {
        if (strs[0].equals("name")) {
			return new AWTTreeScanner.NameScannerMatcher(strs[1]);
		}
        if (strs[0].equals("title")) {
			return new AWTTreeScanner.TitleScannerMatcher(strs[1]);
		}
        if (strs[0].equals("text")) {
			return new AWTTreeScanner.TextScannerMatcher(strs[1]);
		}
        throw new JemmyDSLException("Invalid locator " + strs[0] + "=" + strs[1]);
    }

    /**
     * Returns a list of all visible components which are instances of the given class. If one needs
     * a find that returns an invisible component should add a parameter here. But the default
     * behavior must be returning only visible components as it is the most common operation and
     * required for compatibility with scripts converted from jfcunit. see #15790.
     *
     * @param container
     * @param componentClass
     * @return
     */
    private static List<java.awt.Component> findComponents(java.awt.Container container,
                                                           Class<? extends java.awt.Component> componentClass) {
        List<java.awt.Component> list = new ArrayList<java.awt.Component>();
        for (java.awt.Component component : container.getComponents()) {
            if (component.isVisible() && componentClass.isAssignableFrom(component.getClass())) {
                list.add(component);
            }
            if (component instanceof java.awt.Container) {
                List<java.awt.Component> children = findComponents((java.awt.Container) component,
                                                                   componentClass);
                list.addAll(children);
            }
        }
        return list;
    }

    private static JComponentOperator convertFind(java.awt.Component comp) {
        if (comp instanceof JComboBox) {
			return new JComboBoxOperator((JComboBox) comp);
		}
        if (comp instanceof JLabel) {
			return new JLabelOperator((JLabel) comp);
		}
        if (comp instanceof JProgressBar) {
			return new JProgressBarOperator((JProgressBar) comp);
		}
        if (comp instanceof JTabbedPane) {
			return new JTabbedPaneOperator((JTabbedPane) comp);
		}
        if (comp instanceof JTableHeader) {
			return new JTableHeaderOperator((JTableHeader) comp);
		}
        if (comp instanceof JTextArea) {
			return new JTextAreaOperator((JTextArea) comp);
		}
        if (comp instanceof JTree) {
            return new JTreeOperator((JTree) comp);
        }
        if (comp instanceof JTextComponent) {
			return new JTextFieldOperator((JTextField) comp);
		}
        if (comp instanceof JScrollPane) {
			return new JScrollPaneOperator((JScrollPane) comp);
		}
        if (comp instanceof JCheckBox) {
			return new JCheckBoxOperator((JCheckBox) comp);
		}
        if (comp instanceof JRadioButton) {
			return new JRadioButtonOperator((JRadioButton) comp);
		}
        if (comp instanceof JButton) {
			return new JButtonOperator((JButton) comp);
		}
        if (comp instanceof AbstractButton) {
			return new AbstractButtonOperator((AbstractButton) comp);
		}
        if (comp instanceof JTable) {
            return new TableOperator((JTable) comp);
        }
        if (comp instanceof JMenuBar) {
			return new JMenuBarOperator((JMenuBar) comp);
		}
        if (comp instanceof JScrollBar) {
			return new JScrollBarOperator((JScrollBar) comp);
		}
        if (comp instanceof JInternalFrame) {
			return new JInternalFrameOperator((JInternalFrame) comp);
		}
        throw new JemmyDSLException("Unsupported find type " + comp);
    }

    private static Component convertFind(JComponentOperator comp) {
        if (comp instanceof JComboBoxOperator) {
			return new ComboBox((JComboBoxOperator) comp);
		}
        if (comp instanceof JLabelOperator) {
			return new Label((JLabelOperator) comp);
		}
        if (comp instanceof JProgressBarOperator) {
			return new ProgressBar((JProgressBarOperator) comp);
		}
        if (comp instanceof JTabbedPaneOperator) {
			return new TabbedPane((JTabbedPaneOperator) comp);
		}
        if (comp instanceof JTableHeaderOperator) {
			return new TableHeader((JTableHeaderOperator) comp);
		}
        if (comp instanceof JTextAreaOperator) {
			return new TextArea((JTextAreaOperator) comp);
		}
        if (comp instanceof JTreeOperator) {
            return new Tree((JTreeOperator) comp);
        }
        if (comp instanceof JTextComponentOperator) {
			return new TextField((JTextFieldOperator) comp);
		}
        if (comp instanceof JScrollPaneOperator) {
			return new ScrollPane((JScrollPaneOperator) comp);
		}
        if (comp instanceof JCheckBoxOperator) {
			return new CheckBox((JCheckBoxOperator) comp);
		}
        if (comp instanceof JRadioButtonOperator) {
			return new RadioButton((JRadioButtonOperator) comp);
		}
        if (comp instanceof JButtonOperator) {
			return new Button((JButtonOperator) comp);
		}
        if (comp instanceof AbstractButtonOperator) {
			return new GenericButton((AbstractButtonOperator) comp);
		}
        if (comp instanceof JTableOperator) {
			return new Table((JTableOperator) comp);
		}
        if (comp instanceof JMenuBarOperator) {
			return new MenuBar((JMenuBarOperator) comp);
		}
        if (comp instanceof JScrollBarOperator) {
			return new ScrollBar((JScrollBarOperator) comp);
		}
        // not really sure this is the right thing to do, but constructor here expects a component
        // and not an operator:
        if (comp instanceof JInternalFrameOperator) {
			return new InternalFrame((JInternalFrame) ((JInternalFrameOperator) comp).getSource());
		}
        throw new JemmyDSLException("Unsupported find type " + comp);
    }

    /**
     * Finds the first component with the given component type and stores it under the given id. The
     * component can later be used on other commands using the locator "id=ID_ASSIGNED". This method
     * searches both VISIBLE and INVISIBLE components.
     *
     * @param id The id
     * @param componentType The component type
     * @return The component found
     */
    @SuppressWarnings({"unchecked"})
    public static Component findByComponentType(String id,
                                                String containerId,
                                                String componentType,
                                                int index,
                                                boolean required) {
        java.awt.Container container = null;
        if (isBlank(containerId)) {
            container = (java.awt.Container) currentWindow().getComponent().getSource();
        } else {
            ComponentOperator op = componentMap.getComponent(containerId);

            if (op != null && op.getSource() instanceof java.awt.Container) {
                container = (java.awt.Container) op.getSource();
            } else {
                container = (java.awt.Container) findComponent(containerId,
                        currentWindow().getComponent().getSource());

                if (container == null) {
                    container = (java.awt.Container) currentWindow().getComponent().getSource();
                }
            }
        }
        List<java.awt.Component> list = findComponents(container, translateFindType(componentType));
        if (logger.isDebugEnabled()) {
            logger.debug("findComponents returned list :");
            for (java.awt.Component c : list) {
                logger.debug(" " + c.getName());
            }
            logger.debug(" index = " + index);
        }
        if (index < 0 || index >= list.size()) {
            if (required) {
                throw new JemmyDSLException("Component " + id + " not found");                
            } else {
                componentMap.putComponent(id, null);
                logger.debug("findByComponentType returned null");
                return null;
            }
        }
        java.awt.Component component = list.get(index);
        if (component == null) {
        	if (!required) {
        		componentMap.putComponent(id, null);
        		logger.debug("findByComponentType returned null");
        		return null;
        	} else {
        		throw new JemmyDSLException("Component " + id + " not found");
        	}
        }
        JComponentOperator operator = convertFind(component);
        componentMap.putComponent(id, operator);
        if (logger.isDebugEnabled()) {
            logger.debug("findByComponentType returned " + component);
        }
        return convertFind(operator);
    }

    public static void click(String locator, int count, String modifiers, boolean requestFocus) {
        final JComponentOperator operator = find(locator, JComponentOperator.class);
        if (operator == null) {
			throw new JemmyDSLException("Could not find component for clicking " + locator);
		}
        if (requestFocus) {
        		operator.requestFocus();
        }
        operator.clickMouse(operator.getCenterXForClick(),
                            operator.getCenterYForClick(),
                            count,
                            InputEvent.BUTTON1_MASK,
                            convertModifiers(modifiers));
    }

    public static void typeSpecial(String locator, String keyString, String modifiers) {
        final ContainerOperator operator = find(locator, ContainerOperator.class);

        if (operator == null) {
			throw new JemmyDSLException("Could not find component for typing key " + locator);
		}
        int key = convertKey(keyString);
        // TODO: find a better way to guarantee focus on the target typing component
        // The solution proposed here tries to guarantee that the textField has the focus
        // to make the test as closes as the human interactions as possible.
        operator.requestFocus();
        operator.enableInputMethods(true);
        operator.pushKey(key, convertModifiers(modifiers));
    }

    private static int convertModifiers(String modifiers) {
        if (isBlank(modifiers)) {
			return 0;
		}
        String[] mods = modifiers.split("[ |\\+|,]+");
        int flags = 0;
        for (String mod : mods) {
            if ("Shift".equalsIgnoreCase(mod)) {
                flags |= InputEvent.SHIFT_MASK;
            } else if ("Control".equalsIgnoreCase(mod) || "Ctrl".equalsIgnoreCase(mod)) {
                flags |= InputEvent.CTRL_MASK;
            } else if ("Alt".equalsIgnoreCase(mod)) {
                flags |= InputEvent.ALT_MASK;
            } else {
                throw new JemmyDSLException("Unknown modifier " + mod);
            }
        }
        return flags;
    }

    @SuppressWarnings({"unchecked"})
    public static <X extends ContainerOperator> X find(String locator, Class<X> clazz) {
        Map<String, String> locatorMap = Utils.parseLocator(locator);
        X component;
        if (locatorMap.containsKey("name")) {
            component = newInstance(clazz,
                                    currentWindow().getComponent(),
                                    new NameComponentChooser(locator));
        } else if (locatorMap.containsKey("label")) {
            JLabelOperator jlabel = new JLabelOperator(currentWindow().getComponent(),
                    locatorMap.get("label"));
            if (!(jlabel.getLabelFor() instanceof JTextField)) {
                throw new JemmyDSLException("Associated component for " + locator
                        + " is not a JTextComponent");
            }
            component = newInstance(clazz, JTextField.class, (JTextField) jlabel.getLabelFor());
        } else if (locatorMap.containsKey("text")) {
            if (JTextComponentOperator.class.isAssignableFrom(clazz)) {
                component = newInstance(clazz,
                                        currentWindow().getComponent(),
                                        new JTextComponentOperator.JTextComponentByTextFinder(
                                                locatorMap.get("text")));
            } else if (AbstractButtonOperator.class.isAssignableFrom(clazz)) {
                component = newInstance(clazz,
                                        currentWindow().getComponent(),
                                        new AbstractButtonOperator.AbstractButtonByLabelFinder(
                                                locatorMap.get("text")));
            } else if (JComponentOperator.class.isAssignableFrom(clazz)) {
                // Hack, we assume that what was really meant was AbstractButtonOperator
                component = newInstance(clazz,
                                        currentWindow().getComponent(),
                                        new AbstractButtonOperator.AbstractButtonByLabelFinder(
                                                locatorMap.get("text")));
            } else {
                throw new JemmyDSLException(
                        "Unsupported component type for location by text locator: " + locator);
            }
        } else if (locatorMap.containsKey("id")) {
            ComponentOperator operator = componentMap.getComponent(locatorMap.get("id"));
            if (operator == null) {
				return null;
			}
            if (!clazz.isAssignableFrom(operator.getClass())) {
                throw new JemmyDSLException("Cannot convert component with " + locator + " from "
                        + operator.getClass().getName() + " to " + clazz.getName());
            }
            component = (X) operator;
        } else if (locatorMap.containsKey("title")) {
            if (JInternalFrameOperator.class.isAssignableFrom(clazz)) {
                component = newInstance(clazz,
                                        currentWindow().getComponent(),
                                        new JInternalFrameOperator.JInternalFrameByTitleFinder(
                                                locatorMap.get("title")));
            } else {
                throw new JemmyDSLException(
                        "Unsupported component type for location by text locator: " + locator);
            }
        } else {
            throw new JemmyDSLException("Unsupported locator: " + locator);
        }
        if (component == null) {
            throw new JemmyDSLException("Component not found: " + locator);
        }
        return component;
    }

    private static <X extends ContainerOperator> X newInstance(Class<X> clazz,
                                                                ContainerOperator parent,
                                                                ComponentChooser chooser) {
        try {
            Constructor<X> c = clazz.getConstructor(ContainerOperator.class, ComponentChooser.class);
            return c.newInstance(parent, chooser);
        } catch (Exception e) {
            // Check to see if the nested exception was caused by a regular Jemmy exception
            if (e.getCause() != null && e.getCause() instanceof JemmyException) {
				throw (JemmyException) e.getCause();
			}
            throw new JemmyDSLException(e);
        }
    }

    private static <X extends ContainerOperator, Y> X newInstance(Class<X> clazz,
                                                                   Class<Y> componentClass,
                                                                   JComponent component) {
        try {
            Constructor<X> c = findConstructor(clazz, componentClass);
            return c.newInstance(component);
        } catch (Exception e) {
            // Check to see if the nested exception was caused by a regular Jemmy exception
            if (e.getCause() != null && e.getCause() instanceof JemmyException) {
				throw (JemmyException) e.getCause();
			}
            throw new JemmyDSLException(e);
        }
    }

    @SuppressWarnings({"unchecked"})
    private static <X, Y> Constructor<X> findConstructor(Class<X> clazz, Class<Y> componentClass) {
        Constructor<X>[] cs = (Constructor<X>[]) clazz.getConstructors();
        for (Constructor<X> c : cs) {
            final Class<?>[] types = c.getParameterTypes();
            if (types.length == 1 && types[0].isAssignableFrom(componentClass)) {
				return c;
			}
        }
        throw new JemmyDSLException("Could not find suitable constructor in class "
                + clazz.getCanonicalName());
    }

    public static JComponent getSwingComponentById(String id) {
        ComponentOperator op = componentMap.getComponent(id);
        return (JComponent) op.getSource();
    }

    public static Dialog getDialogById(String id) {
        ComponentOperator op = componentMap.getComponent(id);
        if (!(op instanceof JDialogOperator)) {
			return null;
		}
        return new Dialog((JDialog) op.getSource());
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

    public static void waitHasFocus(String locator) {
        JComponentOperator op = find(locator, JComponentOperator.class);
        op.waitHasFocus();
    }

    public static void waitChecked(String locator, boolean checked) {
        JToggleButtonOperator op = find(locator, JToggleButtonOperator.class);
        try {
            waitComponentChecked(op, checked);
        } catch (InterruptedException e) {
            throw new JemmyDSLException(e);
        }
    }

    private static void waitComponentDisabled(final ComponentOperator op)
        throws InterruptedException {
        Waiter waiter = new Waiter(new Waitable() {
            @Override
			public Object actionProduced(Object obj) {
                if (((java.awt.Component) obj).isEnabled()) {
                    return null;
                } else {
                    return obj;
                }
            }

            @Override
			public String getDescription() {
                return ("Component description: " + op.getSource().getClass().toString());
            }
        });
        waiter.setOutput(op.getOutput());
        waiter.setTimeoutsToCloneOf(op.getTimeouts(),
                                    "ComponentOperator.WaitComponentEnabledTimeout");
        waiter.waitAction(op.getSource());
    }

    private static void waitComponentChecked(final JToggleButtonOperator op, final boolean checked)
        throws InterruptedException {
        Waiter waiter = new Waiter(new Waitable() {
            @Override
			public Object actionProduced(Object obj) {
                if (((JToggleButton) obj).isSelected() != checked) {
                    return null;
                } else {
                    return obj;
                }
            }

            @Override
			public String getDescription() {
                return ("Component description: " + op.getSource().getClass().toString());
            }
        });
        waiter.setOutput(op.getOutput());
        waiter.setTimeoutsToCloneOf(op.getTimeouts(),
                                    "ComponentOperator.WaitComponentEnabledTimeout");
        waiter.waitAction(op.getSource());
    }

    private static int convertKey(String keyString) {
        if ("Tab".equalsIgnoreCase(keyString)) {
			return KeyEvent.VK_TAB;
		} else if ("Enter".equalsIgnoreCase(keyString)) {
			return KeyEvent.VK_ENTER;
		} else if ("End".equalsIgnoreCase(keyString)) {
			return KeyEvent.VK_END;
		} else if ("Backspace".equalsIgnoreCase(keyString)) {
			return KeyEvent.VK_BACK_SPACE;
		} else if ("Delete".equalsIgnoreCase(keyString)) {
			return KeyEvent.VK_DELETE;
		} else if ("Up".equalsIgnoreCase(keyString)) {
			return KeyEvent.VK_UP;
		} else if ("Down".equalsIgnoreCase(keyString)) {
			return KeyEvent.VK_DOWN;
		} else if ("Right".equalsIgnoreCase(keyString)) {
			return KeyEvent.VK_RIGHT;
		} else if ("Left".equalsIgnoreCase(keyString)) {
			return KeyEvent.VK_LEFT;
		} else if ("Home".equalsIgnoreCase(keyString)) {
			return KeyEvent.VK_HOME;
		} else if ("End".equalsIgnoreCase(keyString)) {
			return KeyEvent.VK_END;
		} else if ("F4".equalsIgnoreCase(keyString)) {
			return KeyEvent.VK_F4;
		} else if ("F5".equalsIgnoreCase(keyString)) {
			return KeyEvent.VK_F5;
		} else if ("Space".equalsIgnoreCase(keyString)) {
			return KeyEvent.VK_SPACE;
		} else if ("F7".equalsIgnoreCase(keyString)) {
			return KeyEvent.VK_F7;
		} else if ("F8".equalsIgnoreCase(keyString)) {
			return KeyEvent.VK_F8;
		} else if ("F11".equalsIgnoreCase(keyString)) {
			return KeyEvent.VK_F11;
		} else if ("F12".equalsIgnoreCase(keyString)) {
			return KeyEvent.VK_F12;
		} else if ("A".equalsIgnoreCase(keyString)) {
            return KeyEvent.VK_A;
		} else if ("N".equalsIgnoreCase(keyString)) {
			return KeyEvent.VK_N;		
		} else if ("R".equalsIgnoreCase(keyString)) {
			return KeyEvent.VK_R;
		} else {
			throw new UnsupportedFeatureException("Type special for " + keyString
                    + " not supported");
		}
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

        public void store(String id) {
            componentMap.putComponent(id, getComponent());
        }
    }

    public static abstract class Container extends Component {
        @Override
        public abstract ContainerOperator getComponent();
    }

    public static abstract class Window extends Container {
        public abstract boolean isActive();
        public abstract boolean isShowing();
    }

    public static class Frame extends Window {
        private final JFrameOperator component;

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
            currentWindow = this;
            return this;
        }

        @Override
        public boolean isActive() {
            return component.isActive();
        }

        @Override
        public boolean isShowing() {
            return component.isShowing();
        }

        @Override
        public JFrameOperator getComponent() {
            return component;
        }
    }

    public static class Dialog extends Window {
        private final JDialogOperator component;

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

        @Override
        public JDialogOperator getComponent() {
            return component;
        }

        public Dialog activate() {
            component.activate();
            currentWindow = this;
            return this;
        }

        @Override
        public boolean isShowing() {
            return component.isShowing();
        }

        @Override
        public boolean isActive() {
            return component.isActive();
        }

        public void assertText(String text) {
            final String t = text;
            if (text == null) {
				text = "";
			}
            java.awt.Component c = component.findSubComponent(new ComponentChooser() {

                @Override
                public String getDescription() {
                    return null;
                }

                @Override
                public boolean checkComponent(java.awt.Component comp) {
                    if (!comp.isVisible()) {
						return false;
					}

                    if (comp instanceof JTextField) {
                        String compText = ((JTextField) comp).getText();
                        if (compText == null) {
							compText = "";
						}
                        return t.equals(compText);
                    } else if (comp instanceof JLabel) {
                        String compText = ((JLabel) comp).getText();
                        if (compText == null) {
							compText = "";
						}
                        return t.equals(compText);
                    }
                    return false;
                }
            });
            if (c == null) {
                throw new AssertionFailedException(
                        "assert_dialog: could not find component with text " + text);
            }
        }
    }

    public static class ScrollPane extends Component {
        private final JScrollPaneOperator component;

        public ScrollPane(String locator) {
            component = find(locator, JScrollPaneOperator.class);
        }

        public ScrollPane(JScrollPaneOperator component) {
            this.component = component;
        }

        @Override
        public ComponentOperator getComponent() {
            return component;
        }
    }

    public static class TextField extends Component {
        private final JTextComponentOperator component;

        public TextField(String locator) {
            component = find(locator, JTextComponentOperator.class);
            if (component == null) {
            	throw new JemmyDSLException("Component not found: " + locator);
            }
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
            dispatchingMode();
            try {
                // TODO: find a better way to guarantee focus on the target typing component
                // The solution proposed here tries to guarantee that the textField has the focus
                // to make the test as closes as the human interactions as possible.
                component.requestFocus();
                component.setVerification(false);
                component.typeText(text);
                return this;
            } finally {
                robotMode();
            }
        }

        public TextField type(char key) {
            component.typeKey(key);
            if (!isRobotMode()) {
                // This is a hack because type key in queue mode does not wait for events to be
                // fired
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new JemmyDSLException(e);
                }
            }
            return this;
        }

        public TextField type(int key) {
            component.pushKey(key);
            return this;
        }

        public String text() {
            return component.getText();
        }

        public void assertText(String text) {
            component.waitText(text);
        }

        @Override
        public JTextComponentOperator getComponent() {
            return component;
        }

        public TextField assertEmpty() {
            component.waitText("");
            return this;
        }

        public TextField assertNotEmpty() {
            String text = component.getText();

            if (text == null || text.length() == 0) {
                throw new AssertionFailedException("TextField [" + component.getName() + "] is empty.");
            }

            return this;
        }

        public TextField clickCharPosition(int pos, String modifiers, int count) {
            FontMetrics fm = component.getFontMetrics(component.getFont());
            component.clickMouse(fm.stringWidth(component.getText().substring(0, pos))
                                         + component.getInsets().left,
                                 component.getCenterYForClick(),
                                 count,
                                 KeyEvent.BUTTON1_MASK,
                                 convertModifiers(modifiers));
            return this;
        }
    }

    public static class TextArea extends Component {
        private final JTextAreaOperator component;

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

        @Override
        public JTextAreaOperator getComponent() {
            return component;
        }

        public TextArea assertEmpty() {
            component.waitText("");
            return this;
        }

        public TextArea assertNotEmpty() {
            String text = component.getText();

            if (text == null || text.length() == 0) {
                throw new AssertionFailedException("TextArea [" + component.getName() + "] is empty.");
            }

            return this;
        }
    }

    public static class ComboBox extends Component {
        private final JComboBoxOperator component;

        public ComboBox(String locator) {
            component = find(locator, JComboBoxOperator.class);            
        }

        public ComboBox(JComboBoxOperator comp) {
            this.component = comp;
        }

        public void select(String text) {
            Map<String, String> selectedItem = Utils.parseLocator(text);
            if (selectedItem.containsKey("name")) {
                clickDropDown();
                component.selectItem(selectedItem.get("name"));
            } else if (selectedItem.containsKey("index")) {
                select(Integer.parseInt(selectedItem.get("index")));
            } else {
                throw new IllegalParametersException("Illegal parameters " + text
                        + " for select command");
            }
        }

        public void select(int index) {
            clickDropDown();
            component.setSelectedIndex(index);
            component.waitItemSelected(index);
            component.hidePopup();
        }

        private void clickDropDown() {
            component.pushComboButton();
            component.waitList();
        }

        @Override
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
    
    public static class Tree extends Component {
        private final JTreeOperator operator;
        private JTree component;

        public Tree(String locator) {
            operator = find(locator, JTreeOperator.class);
            component = (JTree) findComponent(locator, currentWindow()
                    .getComponent().getSource());
        }

        public Tree(JTreeOperator component) {
            this.operator = component;
        }

        @Override
        public JTreeOperator getComponent() {
            return operator;
        }

        public void click(int count, String node) {
            TreePath path = findNode(component, node);

            if (path != null) {
                operator.clickOnPath(path, count);
            } else {
                throw new JemmyDSLException("JTree node with label [" + node +
                        " wasn't found.");
            }
        }

        public TreePath findNode(JTree tree, String nodeLabel) {
            TreeNode root = (TreeNode) tree.getModel().getRoot();
            return findNode(tree, new TreePath(root), nodeLabel);
        }

        @SuppressWarnings("rawtypes")
		private static TreePath findNode(JTree tree, TreePath parent,
                String nodeLabel) {
            TreeNode node = (TreeNode)parent.getLastPathComponent();
            Object o = node.toString();

            if (nodeLabel.equals(o)) {
                return parent;
            } else {
                if (node.getChildCount() >= 0) {
                    for (Enumeration e = node.children(); e.hasMoreElements();) {
                        TreeNode n = (TreeNode)e.nextElement();
                        TreePath path = parent.pathByAddingChild(n);
                        TreePath result = findNode(tree, path, nodeLabel);

                        if (result != null) {
                            return result;
                        }
                    }
                }
            }

            return null;
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
        private final JTableOperator component;

        public Table(String locator) {
            component = find(locator, JTableOperator.class);
        }

        public Table(JTableOperator component) {
            this.component = component;
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

        @Override
        public JTableOperator getComponent() {
            return component;
        }
    }

    public static class TableOperator extends JTableOperator {

        public static class XJTableByCellFinder extends JTableByCellFinder {

            private final String label;
            private final int row;
            private final int column;
            private final StringComparator comparator;

            @Override
            public boolean checkComponent(java.awt.Component comp) {
                    if (((JTable) comp).getRowCount() > row
                            && ((JTable) comp).getColumnCount() > column) {
                        int r = row;
                        if (r == -1) {
                            int[] rows = ((JTable) comp).getSelectedRows();
                            if (rows.length != 0) {
                                r = rows[0];
                            } else {
                                return (false);
                            }
                        }
                        int c = column;
                        if (c == -1) {
                            int[] columns = ((JTable) comp).getSelectedColumns();
                            if (columns.length != 0) {
                                c = columns[0];
                            } else {
                                return (false);
                            }
                        }
                        JTable table = (JTable)comp;
                        TableCellRenderer renderer = table.getCellRenderer(r, c);
                        java.awt.Component renderer2 = renderer.getTableCellRendererComponent(table, table.getValueAt(r, c), false, false, r, c);                        
                        Object value;
                        if (renderer2 instanceof DefaultTableCellRenderer) {
                            DefaultTableCellRenderer dtcr = (DefaultTableCellRenderer) renderer2;
                            value = dtcr.getText();                            
                        } else {
                            value = table.getValueAt(r, c);
                        }
                        if (value == null) {
                            value = "";
                        }
                        return (comparator.equals(value.toString(), label));
                    }

                return (false);
            }

            public XJTableByCellFinder(String lb, int r, int c, StringComparator comparator) {
                super(lb, r, c, comparator);
                this.label = lb;
                this.row = r;
                this.column = c;
                this.comparator = comparator;
            }

            public XJTableByCellFinder(String lb, int r, int c) {
                this(lb, r, c, Operator.getDefaultStringComparator());
            }

        }

        public TableOperator(JTable b) {
            super(b);
        }
        /**
         * Overridden to prevent trying to scroll to the row header (which can't be done as the row
         * header is located totally to the left of the scroll bar).
         */
        @Override
        public void scrollToCell(int row, int column) {
            // try to find JScrollPane under.
            JScrollPane scroll = (JScrollPane) getContainer(new JScrollPaneOperator.JScrollPaneFinder(
                    ComponentSearcher.getTrueChooser("JScrollPane")));
            if (scroll == null) {
                return;
            }

            // if source is the row header table, do nothing
            if (getSource() == scroll.getRowHeader().getView()) {
                return;
            }

            super.scrollToCell(row, column);
        }

        /**
         * Overridden method to allow searching for empty strings
         */
        @Override
        public void waitCell(String cellText, int row, int column) {
            waitState(new XJTableByCellFinder(cellText, row, column, getComparator()));
        }

    }

    public static class Row {
        private final JTableOperator component;
        private final int index;

        public Row(JTableOperator component, int index) {
            this.component = component;
            this.index = index;
        }

        public Row assertColumn(int col, String value) {
            component.waitCell(value, index, col);
            return this;
        }

        /**
         * Asserts that a table's cell is not empty, nor null.
         *
         * @param col Column index of the table, starting at 0.
         * @return Cell's row that is asserted to not be empty.
         */
        public Row assertNotEmptyColumn(int col) {
            Object value = component.getValueAt(index, col);

            if (value == null || value.toString().length() == 0) {
                throw new AssertionFailedException("Table cell (" + index + ", " + col + ") is empty.");
            }

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

        public Row selectCell(int col) {
            component.selectCell(index, col);
            return this;
        }

        public Row clickCell(int col, int clicks) {
            component.clickOnCell(index, col, clicks);
            return this;
        }

        public Row clickCell(int col, int clicks, String modifiers) {
            component.clickOnCell(index, col, clicks, JTableOperator.getDefaultMouseButton(),
                    convertModifiers(modifiers));

            return this;
        }
    }

    public static class TableHeader extends Component {
        private final JTableHeaderOperator component;

        public TableHeader(JTableHeader swingComponent) {
            component = new JTableHeaderOperator(swingComponent);
        }

        public TableHeader(JTableHeaderOperator component) {
            this.component = component;
        }

        @Override
        public JTableHeaderOperator getComponent() {
            return component;
        }

        public TableHeader assertTitle(final int col, final String title) {
            component.waitState(new ComponentChooser() {
                @Override
                public boolean checkComponent(java.awt.Component comp) {
                    return ((JTableHeader) comp).getColumnModel()
                        .getColumn(col)
                        .getHeaderValue()
                        .equals(title);
                }

                @Override
                public String getDescription() {
                    return null;
                }
            });
            return this;
        }

        /**
         * Clicks in a column's table header. <p/>
         *
         * @param columnIndex Column' index. It's zero based.
         * @param count Number of clicks
         */
        public void click(int columnIndex, int count) {
            TableColumnModel columnModel =
                    component.getTable().getColumnModel();

            int columnCenterX = 0;
            int counter = 0;
            int convertedColIndex = 0;

            while (counter < columnIndex) {
                int columnWidth = columnModel.getColumn(convertedColIndex)
                        .getWidth();

                if (columnWidth > 0) {
                    columnCenterX += columnWidth;
                    counter++;
                }

                convertedColIndex++;
            }

            // get the X value for the center of the column
            columnCenterX += (columnModel.getColumn(convertedColIndex)
                    .getWidth() / 2);

            component.clickMouse(columnCenterX,
                                 component.getCenterYForClick(),
                                 count,
                                 InputEvent.BUTTON1_MASK,
                                 convertModifiers(null));
        }
    }

    public static class Label extends Component {
        private final JLabelOperator component;

        public Label(JLabelOperator component) {
            this.component = component;
        }

        public Label text(String text) {
            component.waitText(text);
            return this;
        }

        @Override
        public JLabelOperator getComponent() {
            return component;
        }
    }

    public static class ProgressBar extends Component {
        private final JProgressBarOperator component;

        public ProgressBar(JProgressBarOperator component) {
            this.component = component;
        }

        @Override
        public JProgressBarOperator getComponent() {
            return component;
        }
    }

    public static class TabbedPane extends Component {
        private final JTabbedPaneOperator component;

        public TabbedPane(String locator) {
            component = find(locator, JTabbedPaneOperator.class);
        }

        public TabbedPane(JTabbedPaneOperator component) {
            this.component = component;
        }

        public TabbedPane select(String title) {
            component.selectPage(title);
            return this;
        }

        public TabbedPane select(int index) {
            component.selectPage(index);
            return this;
        }

        @Override
        public JTabbedPaneOperator getComponent() {
            return component;
        }
    }

    public static class Slider extends Component {
        private final JSliderOperator component;

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
        private final JMenuBarOperator component;

        public MenuBar() {
            component = new JMenuBarOperator(currentWindow().getComponent());
        }

        public MenuBar(JMenuBarOperator component) {
            this.component = component;
        }

        public MenuBar clickMenu(int... indexes) {
            if (indexes.length == 0) {
				return this;
			}
            String[] texts = new String[indexes.length];
            JMenu menu = component.getMenu(indexes[0]);
            texts[0] = menu.getText();
            for (int i = 1; i < indexes.length; i++) {
                int index = indexes[i];
                assert menu != null;
                if (i == indexes.length - 1) {
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
            if (texts.length == 0) {
				return this;
			}
            component.showMenuItem(texts[0]);
            for (int i = 1; i < texts.length; i++) {
                String text = texts[i];
                ComponentChooser chooser = new JMenuOperator.JMenuByLabelFinder(texts[i - 1], comparator);
                JMenuOperator jmenu = new JMenuOperator(currentWindow().getComponent(), chooser);        
                jmenu.showMenuItem(new String[]{text});
            }
            String text = texts[texts.length - 1];
            ComponentChooser chooser = new JMenuItemOperator.JMenuItemByLabelFinder(text, comparator);
            new JMenuItemOperator(currentWindow().getComponent(), chooser).clickMouse();

            return this;
        }

        @Override
        public JMenuBarOperator getComponent() {
            return component;
        }

    }

    public static class Menu extends Container {
        private JMenuOperator component;

        public Menu() {}

        @Override
        public JMenuOperator getComponent() {
            return component;
        }
    }

    public static class InternalFrame extends Container {
        private final JInternalFrameOperator component;

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

        @Override
        public JInternalFrameOperator getComponent() {
            return component;
        }
    }

    public static class ScrollBar extends Component {
        private final JScrollBarOperator component;

        public ScrollBar(String locator) {
            component = find(locator, JScrollBarOperator.class);
        }

        public ScrollBar(JScrollBarOperator component) {
            this.component = component;
        }

        @Override
        public JScrollBarOperator getComponent() {
            return component;
        }
    }
}