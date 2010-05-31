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
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import java.awt.*;

import static com.github.srec.util.Utils.createParameterMap;

/**
 * Understands recording internal frame events.
 *
 * @author Vivek Prahlad
 */
public class InternalFrameRecorder extends AbstractComponentRecorder implements InternalFrameListener {

    public InternalFrameRecorder(EventRecorder recorder) {
        super(recorder, JInternalFrame.class);
    }

    void componentShown(Component component) {
        frame(component).addInternalFrameListener(this);
    }

    private JInternalFrame frame(Component component) {
        return (JInternalFrame) component;
    }

    void componentHidden(Component component) {
        frame(component).removeInternalFrameListener(this);
    }

    public void internalFrameOpened(InternalFrameEvent e) {
    }

    public void internalFrameClosing(InternalFrameEvent e) {
    }

    public void internalFrameClosed(InternalFrameEvent e) {
    }

    public void internalFrameIconified(InternalFrameEvent e) {
    }

    public void internalFrameDeiconified(InternalFrameEvent e) {
    }

    public void internalFrameActivated(InternalFrameEvent e) {
        JInternalFrame iframe = e.getInternalFrame();
        recorder.record(new MethodCallEventCommand("iframe_activate", iframe, null, createParameterMap("title", iframe.getTitle())));
    }

    public void internalFrameDeactivated(InternalFrameEvent e) {
    }
}
