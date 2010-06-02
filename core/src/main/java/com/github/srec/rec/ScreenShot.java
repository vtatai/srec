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
    String capture(String parentFolder, Robot robot);
    String captureFrame(String parent, Robot robot);
    String captureInternalFrame(String parent, Robot robot);
}
