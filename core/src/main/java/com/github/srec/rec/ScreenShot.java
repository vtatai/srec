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

import java.awt.*;

/**
 * Captures a screenshot into a specified folder.
 *
 * @author vivek
 */
public interface ScreenShot {
    /**
     * Captures the entire desktop.
     *
     * @param parentFolder The folder where to store the screenshot
     * @param robot The robot used for taking screenshots
     * @return The file name for the screenshot, a PNG inside the screenshots dir
     */
    String captureDesktop(String parentFolder, Robot robot);
    /**
     * Captures the current active frame.
     *
     * @param parentFolder The folder where to store the screenshot
     * @param robot The robot used for taking screenshots
     * @return The file name for the screenshot, a PNG inside the screenshots dir
     */
    String captureFrame(String parentFolder, Robot robot);
    /**
     * Captures the internal frame which is under the mouse.
     *
     * @param parentFolder The folder where to store the screenshot
     * @param robot The robot used for taking screenshots
     * @return The file name for the screenshot, a PNG inside the screenshots dir
     */
    String captureInternalFrame(String parentFolder, Robot robot);
    /**
     * Captures the internal frame which has the specified name. The internal frame must be visible and unobstructed. 
     *
     * @param iframeName The internal frame name
     * @param parentFolder The folder where to store the screenshot
     * @param robot The robot used for taking screenshots
     * @return The file name for the screenshot, a PNG inside the screenshots dir
     */
    String captureInternalFrame(String iframeName, String parentFolder, Robot robot);
}
