package com.github.srec.rec.component;

import com.github.srec.command.method.MethodCallEventCommand;
import com.github.srec.rec.EventRecorder;
import com.github.srec.util.Utils;

import javax.swing.text.JTextComponent;
import java.awt.*;

import static com.github.srec.util.Utils.createParameterMap;

/**
 * Records check text events
 *
 * @author Vivek Prahlad
 */
public class CheckTextRecorder extends AbstractCheckRecorder {

    public CheckTextRecorder(EventRecorder recorder) {
        super(recorder, JTextComponent.class);
    }

    protected void check(Component source) {
        JTextComponent component = (JTextComponent) source;
        recorder.record(new MethodCallEventCommand("assert", component, null,
                createParameterMap("locator", Utils.getLocator(component), "text", component.getText())));
    }
}
