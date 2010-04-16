package com.github.srec.rec.component;

import com.github.srec.Utils;
import com.github.srec.rec.EventRecorder;
import com.github.srec.rec.RecorderEvent;

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
        recorder.record(new RecorderEvent("assert", Utils.getLocator(component), component, component.getText()));
    }
}
