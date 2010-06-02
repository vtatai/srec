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

package com.github.srec.rec;

import com.github.srec.SRecException;
import com.github.srec.rec.component.ComponentRecorder;
import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Shortcuts for recording:
 * <ul>
 * <li>Ctrl-Alt-S: Take a screenshot of the entire screen</li>
 * <li>Ctrl-Shift-S: Take a screenshot of the current open frame</li>
 * <li>Ctrl-Alt-Shift-S: Take a screenshot of the current open internal frame</li>
 * </ul>
 *
 * @author Victor Tatai
 */
public class ScreenShotRecorder implements ComponentRecorder, KeyEventDispatcher {
    private static final Logger log = Logger.getLogger(ScreenShotRecorder.class);
    private ScreenShot screenShot;

    public ScreenShotRecorder() {
        screenShot = new DefaultScreenShot();
    }

    public void register() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
    }

    public void unregister() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        if (keyEvent.getID() != KeyEvent.KEY_PRESSED) return false;
        if (!keyEvent.isControlDown()) return false;
        if (keyEvent.getKeyCode() == KeyEvent.VK_S) {
            String filename = null;
            if (keyEvent.isAltDown() && keyEvent.isShiftDown()) {
                log.debug("Capturing internal frame screenshot");
                try {
                    filename = screenShot.captureInternalFrame("", new Robot());
                } catch (AWTException e) {
                    throw new SRecException(e);
                }                
            } else if (keyEvent.isShiftDown()) {
                log.debug("Capturing frame screenshot");
                try {
                    filename = screenShot.captureFrame("", new Robot());
                } catch (AWTException e) {
                    throw new SRecException(e);
                }
            } else if (keyEvent.isAltDown()) {
                log.debug("Capturing entire desktop screenshot");
                try {
                    filename = screenShot.capture("", new Robot());
                } catch (AWTException e) {
                    throw new SRecException(e);
                }
            }
            if (filename == null) {
                log.debug("Cannot capture screenshot");
            }
        }
        return false;
    }
}
