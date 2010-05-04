package com.github.srec.rec.component;

import com.github.srec.command.CallEventCommand;
import com.github.srec.command.jemmy.WindowActivateCommand;
import com.github.srec.rec.EventRecorder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.WindowEvent;

/**
 * Understands recording window activation events.
 *
 * @author Vivek Prahlad
 */
public class WindowActivationRecorder implements ComponentRecorder, AWTEventListener {
    private EventRecorder recorder;

    public WindowActivationRecorder(EventRecorder recorder) {
        this.recorder = recorder;
    }

    public void register() {
        Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.WINDOW_EVENT_MASK);
    }

    public void unregister() {
        Toolkit.getDefaultToolkit().removeAWTEventListener(this);
    }

    public void eventDispatched(AWTEvent event) {
        if (event instanceof WindowEvent) {
            WindowEvent windowEvent = (WindowEvent) event;
            if (windowEvent.getID() == WindowEvent.WINDOW_ACTIVATED) {
                if (windowEvent.getWindow() instanceof JFrame) {
                    JFrame frame = (JFrame) windowEvent.getWindow();
                    recorder.record(new CallEventCommand("window_activate", frame, null, frame.getTitle()));
                }
            }
        }
    }
}
