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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

import static com.github.srec.util.Utils.createParameterMap;

/**
 * Understands recording switch events
 *
 * @author Vivek Prahlad
 */
public class TabSwitchRecorder extends AbstractComponentRecorder implements ChangeListener {
    private ComponentVisibility visibility;

    public TabSwitchRecorder(EventRecorder recorder, ComponentVisibility visibility) {
        super(recorder, JTabbedPane.class);
        this.visibility = visibility;
    }

    void componentShown(Component component) {
        tabbedPane(component).addChangeListener(this);
    }

    void componentHidden(Component component) {
        tabbedPane(component).removeChangeListener(this);
    }

    private JTabbedPane tabbedPane(Component component) {
        return (JTabbedPane) component;
    }

    public void stateChanged(ChangeEvent e) {
        if (!visibility.isShowing(tabbedPane(e))) return;
        final JTabbedPane tab = tabbedPane(e);
        if (tab.getSelectedIndex() != -1) {
            String title = tab.getTitleAt(tab.getSelectedIndex());
            recorder.record(new MethodCallEventCommand("tab", tab, null,
                    createParameterMap("locator", tab.getName(), "text", title)));
        }
    }

    private JTabbedPane tabbedPane(ChangeEvent e) {
        return (JTabbedPane) e.getSource();
    }
}
