package com.github.srec.rec.component;

import com.github.srec.command.method.MethodCallEventCommand;
import com.github.srec.rec.EventRecorder;
import com.github.srec.util.Utils;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import static com.github.srec.util.Utils.createParameterMap;

/**
 * Understands recording text entered in text fields.
 *
 * @author Vivek Prahlad
 * @author Victor Tatai
 */
public class TextFieldRecorder extends AbstractComponentRecorder {
    private static final Logger logger = Logger.getLogger(TextFieldRecorder.class);
    private Map<JTextComponent, DocumentListener> listenerMap = new HashMap<JTextComponent, DocumentListener>();
    private ComponentVisibility visibility;

    public TextFieldRecorder(EventRecorder recorder, ComponentVisibility visibility) {
        super(recorder, JTextField.class);
        this.visibility = visibility;
    }

    @Override
    public void register() {
        super.register();
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            @Override
            public void eventDispatched(AWTEvent event) {
                if (event.getID() != KeyEvent.KEY_TYPED) return;
                if (!(event.getSource() instanceof JTextField)) return;
                JTextField tf = (JTextField) event.getSource();
                String locator = getLocator(tf);
                if (locator == null) return;
                logger.debug("TextField event registered: '" + locator + "', value: '" + tf.getText() + "'");
                if (((KeyEvent) event).getKeyChar() == '\t') {
                    recorder.record(new MethodCallEventCommand("type_special", tf, null,
                            createParameterMap("locator", locator, "text", "Tab")));
                } else {
                    recorder.record(new MethodCallEventCommand("type", tf, null, createParameterMap(
                            "locator", locator, "text", tf.getText() + ((KeyEvent) event).getKeyChar()), true));
                }
            }
        }, AWTEvent.KEY_EVENT_MASK);
    }

    private String getLocator(JTextField tf) {
        if (!visibility.isShowingAndHasFocus(tf)) return null;
        String locator = Utils.getLocator(tf);
        if (locator == null) {
            logger.warn("No locator could be determined for text field");
            return null;
        }
        return locator;
    }

    void componentShown(Component component) {
    }

    void componentHidden(Component component) {
    }

    private JTextComponent textField(Component component) {
        return (JTextComponent) component;
    }
}
