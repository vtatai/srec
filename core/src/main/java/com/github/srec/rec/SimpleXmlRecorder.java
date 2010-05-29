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

import com.github.srec.command.ValueCommand;
import com.github.srec.command.method.MethodCallEventCommand;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * A simple recorder which records XML.
 *
 * @author Victor Tatai
 */
public class SimpleXmlRecorder implements RecorderEventCallback {
    private PrintWriter writer;
    private Recorder recorder = new Recorder(this);
    private MethodCallEventCommand lastEvent;

    public SimpleXmlRecorder(PrintWriter writer) {
        this.writer = writer;
    }

    public void start(String className) {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent keyEvent) {
                if (keyEvent.getID() != KeyEvent.KEY_PRESSED) return false;
                if (!keyEvent.isControlDown()) return false;
                if (keyEvent.getKeyCode() == KeyEvent.VK_D) {
                    if (recorder.isRecording()) {
                        recorder.setRecording(false);
                        writeLastEvent();
                    } else {
                        recorder.setRecording(true);
                    }
                }
                return false;
            }
        });
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                SimpleXmlRecorder.this.stop();
            }
        });
        // Print test suite and test case declarations
        writer.println("<suite>");
        writer.println("  <test_case>");

        recorder.init();

        try {
            Class<?> cl = Class.forName(className);
            Method m = cl.getMethod("main", String[].class);
            m.invoke(null, new Object[] {new String[] {}});
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        recorder.setRecording(true);
    }

    public void stop() {
        recorder.shutdown();
        writeLastEvent();

        // Print test suite and test case end tags
        writer.println("  </test_case>");
        writer.println("</suite>");
        writer.flush();

        writer.close();
    }

    private void writeLastEvent() {
        if (lastEvent != null) {
            writer.println(serialize(lastEvent));
            lastEvent = null;
        }
        writer.flush();
    }

    @Override
    public void addEvent(MethodCallEventCommand event) {
        System.out.println("Event: " + event);
        if (lastEvent != null) {
            writer.println(serialize(lastEvent));
        }
        lastEvent = event;
    }

    private String serialize(MethodCallEventCommand event) {
        return "<" + event.getName() + " " + serialize(event.getParameters()) + "/>";
    }

    private String serialize(Map<String, ValueCommand> parameters) {
        StringBuilder strb = new StringBuilder();
        for (Map.Entry<String, ValueCommand> entry : parameters.entrySet()) {
            strb.append(entry.getKey()).append("=\"").append(entry.getValue().getValue(null)).append("\" ");
        }
        return strb.toString();
    }

    @Override
    public void replaceLastEvent(MethodCallEventCommand event) {
        lastEvent = event;
    }

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        SimpleXmlRecorder sr = new SimpleXmlRecorder(new PrintWriter(System.out, true));
        sr.start(args[0]);
    }
}
