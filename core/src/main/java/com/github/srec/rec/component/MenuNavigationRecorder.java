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
import com.github.srec.util.Utils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Understands recording when the user navigating to a menu associated with a menu bar.
 *
 * @author Vivek Prahlad
 */
public class MenuNavigationRecorder extends AbstractComponentRecorder implements ActionListener, ChangeListener {
    private MenuElement[] selectedPath = null;

    public MenuNavigationRecorder(EventRecorder recorder) {
        super(recorder, JMenuItem.class);
    }

    void componentShown(Component component) {
        menuItem(component).addActionListener(this);
    }

    private JMenuItem menuItem(Component component) {
        return (JMenuItem) component;
    }

    void componentHidden(final Component component) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                menuItem(component).removeActionListener(MenuNavigationRecorder.this);
            }
        });
    }

    public void register() {
        super.register();
        MenuSelectionManager.defaultManager().addChangeListener(this);
    }

    public void unregister() {
        super.unregister();
        MenuSelectionManager.defaultManager().removeChangeListener(this);
    }

    private void menuSelected(String path) {
        recorder.record(new MethodCallEventCommand("push_menu", null, null, Utils.createParameterMap("path", path)));
    }

    public void actionPerformed(ActionEvent e) {
        menuSelected(menuItemText());
    }

    private String menuItemText() {
        String menuItemText = "";
        for (int i = 0; i < selectedPath.length; i++) {
            MenuElement menuElement = selectedPath[i];
            if (menuElement instanceof JMenuItem) {
                JMenuItem menuItem = (JMenuItem) menuElement;
                menuItemText = menuItemText + menuItem.getText() + ">";
            }
        }
        return menuItemText.substring(0, menuItemText.length() - 1);
    }

    public void stateChanged(ChangeEvent e) {
        MenuElement[] path = MenuSelectionManager.defaultManager().getSelectedPath();
        if (path != null && path.length > 0 && path[path.length - 1] instanceof JMenuItem) {
            selectedPath = MenuSelectionManager.defaultManager().getSelectedPath();
        }
    }
}
