package com.github.srec.command;

import com.github.srec.rec.Recorder;

import java.awt.*;
import java.util.Arrays;

/**
 * An actual event which was recorded.
 *
 * @author Victor Tatai
 */
public abstract class EventCommand implements Command {
    protected String name;
    protected String componentLocator;
    // TODO probably not needed
    protected Component component;
    protected String[] params;
    /**
     * Indicates whether events with the same target should be collapsed into a single name. The real collapsing
     * though is done by the caller of {@link #record(com.github.srec.rec.Recorder, EventCommand)}.
     */
    private boolean collapseMultiple = true;

    public EventCommand(String name, String componentLocator, Component component, String... params) {
        this.componentLocator = componentLocator;
        this.name = name;
        this.component = component;
        this.params = params;
    }

    public EventCommand(String name, String componentLocator, Component component, boolean collapseMultiple, String... params) {
        this(name, componentLocator, component, params);
        this.collapseMultiple = collapseMultiple;
    }

    public void record(Recorder recorder, EventCommand lastEvent) {
        if (collapseMultiple && isSameTargetAs(lastEvent)) {
            recorder.replaceLastEvent(this);
        } else {
            recorder.addEvent(this);
        }
    }

    protected boolean isSameTargetAs(EventCommand lastEvent) {
        return lastEvent != null && name.equals(lastEvent.getName()) && componentLocator.equals(lastEvent.getComponentLocator());
    }

    public String getComponentLocator() {
        return componentLocator;
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

    public String[] getParams() {
        return params;
    }

    @Override
    public String toString() {
        return getName();
    }
}
