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

import com.github.srec.command.exception.CommandExecutionException;
import com.github.srec.command.method.MethodCallEventCommand;
import com.github.srec.rec.EventRecorder;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static com.github.srec.util.Utils.createParameterMap;

/**
 * Records row selections. It may NOT work for all applications.
 *
 * @author Victor Tatai
 */
public class TableRowSelectionRecorder extends AbstractComponentRecorder implements ListSelectionListener {
    private Map<ListSelectionModel, JTable> tableMap = new HashMap<ListSelectionModel, JTable>();
    
    public TableRowSelectionRecorder(EventRecorder recorder) {
        super(recorder, JTable.class);
    }

    @Override
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        ListSelectionModel model = (ListSelectionModel) listSelectionEvent.getSource();
        JTable table = tableMap.get(model);
        recorder.record(new MethodCallEventCommand("row_select", table, null,
                createParameterMap(
                        "table", table.getName(),
                        "first", listSelectionEvent.getFirstIndex(),
                        "last", listSelectionEvent.getLastIndex())));
    }

    void componentShown(Component component) {
        JTable previous = tableMap.get(table(component).getSelectionModel());
        if (previous != null && previous != component) {
            throw new CommandExecutionException("Same selection model for different tables.");
        }
        tableMap.put(table(component).getSelectionModel(), table(component));
        table(component).getSelectionModel().addListSelectionListener(this);
    }

    void componentHidden(Component component) {
        tableMap.remove(table(component).getSelectionModel());
        table(component).getSelectionModel().removeListSelectionListener(this);
    }

    private JTable table(Component component) {
        return ((JTable) component);
    }
}
