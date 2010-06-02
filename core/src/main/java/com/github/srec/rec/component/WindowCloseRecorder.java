/*
 * Copyright 2010 Victor Tatai
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
 * the specific language governing permissions and limitations under the License.
 */

package com.github.srec.rec.component;

import com.github.srec.command.method.MethodCallEventCommand;
import com.github.srec.rec.EventRecorder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.WindowEvent;

import static com.github.srec.util.Utils.createParameterMap;

/**
 * Understands recording window close events.
 *
 * @author Victor Tatai
 */
public class WindowCloseRecorder implements ComponentRecorder, AWTEventListener {
    private EventRecorder recorder;

    public WindowCloseRecorder(EventRecorder recorder) {
        this.recorder = recorder;
    }
                                                             
    public void register() {
        Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.WINDOW_EVENT_MASK);
    }

    public void unregister() {
        Toolkit.getDefaultToolkit().removeAWTEventListener(this);
    }

    public void eventDispatched(AWTEvent event) {
        if (!(event instanceof WindowEvent)) return;
        WindowEvent windowEvent = (WindowEvent) event;
        if (windowEvent.getID() != WindowEvent.WINDOW_CLOSING || !(windowEvent.getWindow() instanceof JFrame)) return;
        JFrame frame = (JFrame) windowEvent.getWindow();
        recorder.record(new MethodCallEventCommand("window_close", frame, null, createParameterMap("locator", frame.getTitle())));
    }
}