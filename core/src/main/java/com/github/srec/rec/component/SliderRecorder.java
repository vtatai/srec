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
 * @author Victor Tatai
 */
public class SliderRecorder extends AbstractComponentRecorder implements ChangeListener {
    public SliderRecorder(EventRecorder recorder) {
        super(recorder, JSlider.class);
    }

    @Override
    public void stateChanged(ChangeEvent changeEvent) {
        JSlider source = (JSlider) changeEvent.getSource();
        if (source.getValueIsAdjusting()) return;
        int value = source.getValue();
        recorder.record(new MethodCallEventCommand("slide", source, null,
                createParameterMap("locator", source.getName(), "value", value)));
    }

    void componentShown(Component component) {
        slider(component).addChangeListener(this);
    }

    private JSlider slider(Component component) {
        return (JSlider) component;
    }

    void componentHidden(Component component) {
        slider(component).removeChangeListener(this);
    }
}
