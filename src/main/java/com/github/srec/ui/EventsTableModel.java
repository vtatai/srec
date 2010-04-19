package com.github.srec.ui;

import com.github.srec.rec.RecorderEvent;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class EventsTableModel extends AbstractTableModel {
    private List<RecorderEvent> events = new ArrayList<RecorderEvent>();

    @Override
    public int getRowCount() {
        return events.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        RecorderEvent evt = events.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return evt.getCommand();
            case 1:
                return evt.getComponentLocator();
            case 2:
                return evt.getArgs() != null && evt.getArgs().length > 0 ? evt.getArgs()[0] : null;
            case 3:
                return evt.getArgs() != null && evt.getArgs().length > 1 ? evt.getArgs()[1] : null;
            default:
                return null;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Command";
            case 1:
                return "Target";
            case 2:
                return "Parameter 1";
            case 3:
                return "Parameter 2";
        }
        throw new IllegalArgumentException();
    }

    public void add(RecorderEvent event) {
        events.add(event);
        int index = events.size() - 1;
        fireTableRowsInserted(index, index);
    }

    public void add(List<RecorderEvent> newEvents) {
        if (newEvents == null || newEvents.isEmpty()) return;
        int startIndex = events.size();
        events.addAll(newEvents);
        fireTableRowsInserted(startIndex, startIndex + newEvents.size() - 1);
    }

    public void clear() {
        if (events.isEmpty()) return;
        final int lastIndex = events.size() - 1;
        events.clear();
        fireTableRowsDeleted(0, lastIndex);
    }

    public void update(int index, List<RecorderEvent> events) {
        assert events != null && events.size() > 0;
        for (int i = index; i < index + events.size(); i++) {
            this.events.set(i, events.get(i - index));
        }
        fireTableRowsUpdated(index, index + events.size() - 1);
    }
}
