package com.github.srec.rec.component;

import com.github.srec.rec.EventRecorder;

import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.HierarchyEvent;

/**
 * Base class for all component event recorders
 *
 * @author Vivek Prahlad
 */
public abstract class AbstractComponentRecorder implements ComponentRecorder, AWTEventListener {
    protected EventRecorder recorder;
    protected Class componentClass;

    protected AbstractComponentRecorder(EventRecorder recorder, Class componentClass) {
        this.recorder = recorder;
        this.componentClass = componentClass;
    }

    public void register() {
        Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.HIERARCHY_EVENT_MASK);
    }

    public void unregister() {
        Toolkit.getDefaultToolkit().removeAWTEventListener(this);
    }

    public void eventDispatched(AWTEvent event) {
        if (event instanceof HierarchyEvent) {
            HierarchyEvent he = (HierarchyEvent) event;
            if (isComponentDisplayableEvent(he) && matchesComponentType(event)) {
                Component component = (Component) event.getSource();
                if (component.isDisplayable()) {
                    componentShown(component);
                } else {
                    componentHidden(component);
                }
            }
        }
    }

    private boolean isComponentDisplayableEvent(HierarchyEvent he) {
        return (he.getChangeFlags() & HierarchyEvent.DISPLAYABILITY_CHANGED) != 0;
    }

    protected boolean matchesComponentType(AWTEvent event) {
        return componentClass.isAssignableFrom(event.getSource().getClass());
    }

    abstract void componentShown(Component component);

    abstract void componentHidden(Component component);
}
