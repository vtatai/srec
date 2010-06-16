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

package com.github.srec.command.parser.xml;

import com.github.srec.command.ExecutionContext;
import com.github.srec.command.ExecutionContextFactory;
import com.github.srec.command.TestCase;
import com.github.srec.command.TestSuite;
import com.github.srec.command.method.MethodScriptCommand;
import com.github.srec.command.value.Type;
import org.testng.annotations.Test;

import java.io.File;

import static org.testng.Assert.assertEquals;

/**
 * @author Victor Tatai
 */
@Test
public class XmlParserTest {
    public void testForm() {
        XmlParser p = new XmlParser();
        final File file = new File("src/test/resources/test_form.xml");
        final ExecutionContext context = ExecutionContextFactory.getInstance().create(null, null, file);
        TestSuite suite = p.parse(context, file);
        assertEquals(p.getErrors().size(), 0);
        assertEquals(suite.getTestCases().size(), 1);
        TestCase tc = suite.getTestCases().get(0);
        assertEquals(tc.getExecutionContext().getCommands().size(), 37);
    }

    public void testMethod() {
        XmlParser p = new XmlParser();
        final File file = new File("src/test/resources/test_form_method_call.xml");
        final ExecutionContext context = ExecutionContextFactory.getInstance().create(null, null, file);
        TestSuite suite = p.parse(context, file);
        assertEquals(p.getErrors().size(), 0);
        assertEquals(suite.getTestCases().size(), 1);
        TestCase tc = suite.getTestCases().get(0);
        assertEquals(tc.getExecutionContext().getCommands().size(), 10);
        MethodScriptCommand method = (MethodScriptCommand) context.findSymbol("close_window");
        assertEquals(method.getCommands().size(), 1);
        assertEquals(method.getParameters().size(), 1);
        assertEquals(method.getParameters().get("name").getType(), Type.STRING);
    }
}
