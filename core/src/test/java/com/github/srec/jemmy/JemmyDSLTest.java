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

package com.github.srec.jemmy;

import com.github.srec.ui.TestForm;
import org.testng.annotations.Test;

import java.io.IOException;

import static com.github.srec.jemmy.JemmyDSL.*;

/**
 * @author Victor Tatai
 */
@Test
public class JemmyDSLTest {
    public void testMenu() throws IOException, InterruptedException {
        TestForm.main(new String[0]);
        init();
        frame("TestForm").activate();
        internalFrame("title=Internal Frame").close();
        menuBar().clickMenu(1, 0, 1);
        internalFrame("title=Internal Frame").assertVisible(true);
    }    
}
