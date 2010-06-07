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

package com.github.srec.command.ext;

import com.github.srec.command.ExecutionContext;
import com.github.srec.command.SRecCommand;
import com.github.srec.command.exception.CommandExecutionException;
import com.github.srec.command.method.MethodCommand;
import com.github.srec.command.value.Type;
import com.github.srec.command.value.Value;
import com.github.srec.rec.DefaultScreenShot;
import com.github.srec.rec.ScreenShot;

import java.awt.*;
import java.util.Map;

import static org.apache.commons.lang.StringUtils.isBlank;

/**
 * Takes a screenshot.
 *
 * @author Victor Tatai
 */
@SRecCommand
public class ScreenshotCommand extends MethodCommand {
    public static final String MODE = "mode";
    public static final String IFRAME = "iframe";
    private ScreenShot screenshot = new DefaultScreenShot();

    public enum Mode {
        DESKTOP,
        FRAME,
        INTERNAL_FRAME
    }

    public ScreenshotCommand() {
        super("screenshot", param(MODE), param(IFRAME, Type.STRING, true, null));
    }

    @Override
    protected Value internalCallMethod(ExecutionContext context, Map<String, Value> params) {
        String modeString = asString(MODE, params, context);
        try {
            Mode mode = Mode.valueOf(modeString);
            Robot robot = new Robot();
            switch (mode) {
                case DESKTOP:
                    screenshot.captureDesktop(null, robot);
                    break;
                case FRAME:
                    screenshot.captureFrame(null, robot);
                    break;
                case INTERNAL_FRAME:
                    String iframeName = asString(IFRAME, params, context);
                    if (isBlank(iframeName)) throw new CommandExecutionException("iframe parameter cannot be blank");
                    screenshot.captureInternalFrame(iframeName, null, robot);
                    break;
            }
        } catch (IllegalArgumentException e) {
            throw new CommandExecutionException("Invalid mode specified for screenshot command: " + modeString);
        } catch (AWTException e) {
            throw new CommandExecutionException(e);
        }
        return null;
    }

    @Override
    public boolean isNative() {
        return true;
    }

    @Override
    public String toString() {
        return "screenshot";
    }
}