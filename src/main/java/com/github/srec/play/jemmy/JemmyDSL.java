package com.github.srec.play.jemmy;

import com.github.srec.Utils;
import com.github.srec.play.exception.IllegalParametersException;
import org.netbeans.jemmy.ComponentChooser;
import org.netbeans.jemmy.JemmyProperties;
import org.netbeans.jemmy.Timeouts;
import org.netbeans.jemmy.operators.*;
import org.netbeans.jemmy.util.NameComponentChooser;
import org.testng.Assert;

import javax.swing.*;
import java.util.Map;
import java.util.Properties;

public class JemmyDSL {
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

    public static void init() {
        Timeouts timeouts = JemmyProperties.getCurrentTimeouts();
        for (Map.Entry<Object, Object> entry : props.entrySet()) {
            timeouts.setTimeout((String) entry.getKey(), Long.parseLong((String) entry.getValue()));
        }
        currentContainer = null;
    }

    public static Frame frame(String title) {
        Frame frame = new Frame(title);
        currentContainer = frame;
        return frame;
    }

    public static Frame frame() {
        return (Frame) currentContainer;
    }

    private static Container container() {
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

    private interface Container {
        ContainerOperator getComponent();
    }

    public static class Frame implements Container {
        private JFrameOperator component;

        public Frame(String title) {
            component = new JFrameOperator(title);
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
        }

        public Dialog close() {
            component.requestClose();
            return this;
        }

        public JDialogOperator getComponent() {
            return component;
        }
    }

    public static class TextField {
        private JTextFieldOperator component;

        public TextField(String locator) {
            Map<String, String> locatorMap = Utils.parseLocator(locator);
            if (locatorMap.containsKey("name")) {
                component = new JTextFieldOperator(container().getComponent(), new NameComponentChooser(locator));
            } else if (locatorMap.containsKey("label")) {
                JLabelOperator jlabel = new JLabelOperator(container().getComponent(), locatorMap.get("label"));
                if (!(jlabel.getLabelFor() instanceof JTextField)) {
                    throw new IllegalParametersException("Associated component for " + locator + " is not a JTextField");
                }
                component = new JTextFieldOperator((JTextField) jlabel.getLabelFor());
            }
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
    }

    public static class ComboBox {
        private JComboBoxOperator component;

        public ComboBox(String name) {
            component = new JComboBoxOperator(container().getComponent(), new NameComponentChooser(name));
        }

        public void select(String text) {
            component.selectItem(text);
        }
    }

    public static class Button {
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
    }

    public static class Table {
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
            Assert.assertEquals(realValue, value);
            return this;
        }
    }
}
