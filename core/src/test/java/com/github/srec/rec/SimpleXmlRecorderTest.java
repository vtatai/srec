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

import nu.xom.*;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static com.github.srec.jemmy.JemmyDSL.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Test
public class SimpleXmlRecorderTest {
    public void test() {
        StringWriter writer = new StringWriter();
        SimpleXmlRecorder sr = new SimpleXmlRecorder(new PrintWriter(writer));
        sr.start("com.github.srec.ui.TestForm", new String[0]);

        button("text=Ok").click();
        slider("slider").value(10);
        tabbedPane("tabbedPane").select("Table");
        table("table").row(0).select();

        sr.stop();
        assertCommands(writer.toString(),
                new Tag("window_activate", "locator", "TestForm"),
                new Tag("click", "locator", "text=Ok"),
                new Tag("slide", "locator", "slider", "value", "10"),
                new Tag("tab", "locator", "tabbedPane", "text", "Table"),
                new Tag("row_select", "table", "table", "first", "0", "last", "0"));
    }

    private void assertCommands(String xml, Tag... tags) {
        System.out.println(xml);
        try {
            Builder parser = new Builder();
            Document doc = parser.build(new StringReader(xml));
            Element suite = doc.getRootElement();
            assertEquals(suite.getLocalName(), "suite");
            Element testCase = suite.getFirstChildElement("test_case");
            assertEquals(testCase.getLocalName(), "test_case");

            for (int i = 0; i < tags.length; i++) {
                Element element = testCase.getChildElements().get(i);
                final Tag tag = tags[i];
                assertEquals(element.getLocalName(), tag.getName());
                assertEquals(element.getAttributeCount(), tag.getAttributes().size());
                for (int j = 0; j < element.getAttributeCount(); j++) {
                    Attribute attr = element.getAttribute(j);
                    assertTrue(tag.getAttributes().containsKey(attr.getLocalName()));
                    assertEquals(attr.getValue(), tag.getAttributes().get(attr.getLocalName()));
                }
            }
            assertEquals(testCase.getChildElements().size(), tags.length);
        } catch (ParsingException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static class Tag {
        private String name;
        private Map<String, String> attributes = new HashMap<String, String>();

        private Tag(String name, String... attributes) {
            this.name = name;
            assert attributes.length % 2 == 0;
            for (int i = 0; i < attributes.length; i = i + 2) {
                String attribute = attributes[i];
                String value = attributes[i + 1];
                this.attributes.put(attribute, value);
            }
        }

        public String getName() {
            return name;
        }

        public Map<String, String> getAttributes() {
            return attributes;
        }
    }
}
