package com.github.srec.rec.component;

import com.github.srec.Utils;
import com.github.srec.command.MethodCallEventCommand;
import com.github.srec.rec.EventRecorder;

import javax.swing.text.JTextComponent;
import java.awt.*;

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
        recorder.record(new MethodCallEventCommand("assert", component, null, Utils.getLocator(component), component.getText()));
    }
}
