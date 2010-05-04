package com.github.srec.command;

import com.github.srec.rec.Recorder;
import org.antlr.runtime.tree.CommonTree;

import java.awt.*;

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

    public CallEventCommand(String name, Component component, CommonTree tree, String... params) {
        super(name, tree, params);
        this.component = component;
    }

    public CallEventCommand(String name, Component component, CommonTree tree, boolean collapseMultiple, String... params) {
        this(name, component, tree, params);
        this.collapseMultiple = collapseMultiple;
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
        return parameters == null || parameters.length == 0 ? null : parameters[0];
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

    @Override
    public String toString() {
        return getName();
    }
}
