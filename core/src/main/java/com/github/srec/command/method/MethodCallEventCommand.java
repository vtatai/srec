package com.github.srec.command.method;

import com.github.srec.Location;
import com.github.srec.command.ValueCommand;
import com.github.srec.command.exception.CommandExecutionException;
import com.github.srec.command.value.StringValue;
import com.github.srec.command.value.Value;
import com.github.srec.rec.Recorder;

import java.awt.*;
import java.util.Map;

/**
 * An actual event which was recorded.
 *
 * @author Victor Tatai
 */
public class MethodCallEventCommand extends MethodCallCommand {
    // TODO probably not needed
    protected Component component;
    /**
     * Indicates whether events with the same target should be collapsed into a single name. The real collapsing
     * though is done by the caller of {@link #record(com.github.srec.rec.Recorder, MethodCallEventCommand)}.
     */
    private boolean collapseMultiple = true;

    public MethodCallEventCommand(String name, Component component, Location location, Map<String, ValueCommand> params) {
        super(name, location);
        this.component = component;
        parameters.putAll(params);
    }

    public MethodCallEventCommand(String name, Component component, Location location,
                                  Map<String, ValueCommand> params, boolean collapseMultiple) {
        this(name, component, location, params);
        this.collapseMultiple = collapseMultiple;
    }

    public void record(Recorder recorder, MethodCallEventCommand lastEvent) {
        if (collapseMultiple && isSameTargetAs(lastEvent)) {
            recorder.replaceLastEvent(this);
        } else {
            recorder.addEvent(this);
        }
    }

    protected boolean isSameTargetAs(MethodCallEventCommand lastEvent) {
        return lastEvent != null && name.equals(lastEvent.getName()) && getComponentLocator().equals(lastEvent.getComponentLocator());
    }

    public String getComponentLocator() {
        if (parameters == null || parameters.size() == 0 || parameters.get("locator") == null) return null;
        Value value =  parameters.get("locator").getValue(null);
        if (!(value instanceof StringValue)) {
            throw new CommandExecutionException("First parameter is not a string, cannot be component locator");
        }
        return ((StringValue) value).get();
    }

    public String getName() {
        return name;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public String print() {
        StringBuilder strb = new StringBuilder(name).append(" ");
        for (ValueCommand parameter : parameters.values()) {
            strb.append("\"").append(parameter.toString()).append("\", ");
        }
        final String str = strb.toString();
        if (str.endsWith(", ")) return str.substring(0, str.length() - 2);
        return str;
    }

    @Override
    public String toString() {
        return getName();
    }
}
