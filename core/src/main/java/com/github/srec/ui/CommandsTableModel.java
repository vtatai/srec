package com.github.srec.ui;

import com.github.srec.UnsupportedFeatureException;
import com.github.srec.command.CallEventCommand;
import com.github.srec.command.Command;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Victor Tatai
 */
public class CommandsTableModel extends AbstractTableModel {
    private List<Command> commands = new ArrayList<Command>();

    @Override
    public int getRowCount() {
        return commands.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (!(commands.get(rowIndex) instanceof CallEventCommand)) {
            throw new UnsupportedFeatureException("Command not supported: " + commands.get(rowIndex));
        }
        CallEventCommand evt = (CallEventCommand) commands.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return evt.getName();
            case 1:
                return evt.getComponentLocator();
            case 2:
                return evt.getParameterString(1);
            case 3:
                return evt.getParameterString(2);
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

    public void add(CallEventCommand event) {
        commands.add(event);
        int index = commands.size() - 1;
        fireTableRowsInserted(index, index);
    }

    public void add(List<Command> newEvents) {
        if (newEvents == null || newEvents.isEmpty()) return;
        int startIndex = commands.size();
        commands.addAll(newEvents);
        fireTableRowsInserted(startIndex, startIndex + newEvents.size() - 1);
    }

    public void clear() {
        if (commands.isEmpty()) return;
        final int lastIndex = commands.size() - 1;
        commands.clear();
        fireTableRowsDeleted(0, lastIndex);
    }

    public void update(int index, List<Command> events) {
        assert events != null && events.size() > 0;
        for (int i = index; i < index + events.size(); i++) {
            this.commands.set(i, events.get(i - index));
        }
        fireTableRowsUpdated(index, index + events.size() - 1);
    }
}
