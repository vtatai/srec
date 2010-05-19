package com.github.srec.command;

import com.github.srec.rec.Recorder;

import java.awt.*;
import java.util.Arrays;

/**
 * An actual event which was recorded.
 *
 * @author Victor Tatai
 */
public class CallEventCommand extends CallCommand {
    // TODO probably not needed
    protected Component component;
    /**
     * Indicates whether events with the same target should be collapsed into a single name. The real collapsing
     * though is done by the caller of {@link #record(com.github.srec.rec.Recorder, CallEventCommand)}.
     */
    private boolean collapseMultiple = true;

    public CallEventCommand(String name, Component component, String... params) {
        super(name);
        this.component = component;
        addParams(params);
    }

    private void addParams(String[] params) {
        parameters.addAll(Arrays.asList(params));
    }

    public CallEventCommand(String name, Component component, boolean collapseMultiple, String... params) {
        this(name, component);
        this.collapseMultiple = collapseMultiple;
        addParams(params);
    }

    public void record(Recorder recorder, CallEventCommand lastEvent) {
        if (collapseMultiple && isSameTargetAs(lastEvent)) {
            recorder.replaceLastEvent(this);
        } else {
            recorder.addEvent(this);
        }
    }

    protected boolean isSameTargetAs(CallEventCommand lastEvent) {
        return lastEvent != null && name.equals(lastEvent.getName()) && getComponentLocator().equals(lastEvent.getComponentLocator());
    }

    public String getComponentLocator() {
        if (parameters == null || parameters.size() == 0) return null;
        return parameters.get(0);
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
        for (String parameter : parameters) {
            strb.append("\"").append(parameter + "\", ");
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
