package com.github.srec;

import com.github.srec.rec.Recorder;

import java.awt.*;
import java.util.Arrays;

public class RecorderEvent {
    private String command;
    private String componentLocator;
    private Component component;
    private String[] args;
    /**
     * Indicates whether events with the same target should be collapsed into a single command. The real collapsing
     * though is done by the caller of {@link #record(com.github.srec.rec.Recorder, RecorderEvent)}.
     */
    private boolean collapseMultiple = true;

    public RecorderEvent(String command, String componentLocator, Component component, String... args) {
        this.componentLocator = componentLocator;
        this.command = command;
        this.component = component;
        this.args = args;
    }

    public RecorderEvent(String command, String componentLocator, Component component, boolean collapseMultiple, String... args) {
        this(command, componentLocator, component, args);
        this.collapseMultiple = collapseMultiple;
    }

    public void record(Recorder recorder, RecorderEvent lastEvent) {
        if (collapseMultiple && isSameTargetAs(lastEvent)) {
            recorder.replaceLastEvent(this);
        } else {
            recorder.addEvent(this);
        }
    }

    protected boolean isSameTargetAs(RecorderEvent lastEvent) {
        return lastEvent != null && command.equals(lastEvent.getCommand()) && componentLocator.equals(lastEvent.getComponentLocator());
    }

    public String getComponentLocator() {
        return componentLocator;
    }

    public String getCommand() {
        return command;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public String[] getArgs() {
        return args;
    }

    @Override
    public String toString() {
        return "RecorderEvent{" +
                "command='" + command + '\'' +
                ", componentLocator='" + componentLocator + '\'' +
                ", args=" + (args == null ? null : Arrays.asList(args)) +
                '}';
    }
}
