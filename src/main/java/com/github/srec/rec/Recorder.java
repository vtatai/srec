package com.github.srec.rec;

import com.github.srec.RecorderEvent;
import com.github.srec.rec.common.DefaultComponentDecoder;
import com.github.srec.rec.component.*;
import com.github.srec.rec.RecorderEventListener;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Recorder implements EventRecorder {
    private static final Logger logger = Logger.getLogger(Recorder.class);
    private List<RecorderEvent> events = new ArrayList<RecorderEvent>();
    private List<ComponentRecorder> componentRecorders = new ArrayList<ComponentRecorder>();
    private List<RecorderEventListener> listeners = new ArrayList<RecorderEventListener>();
    private List<Container> ignoredContainers = new ArrayList<Container>();
    private boolean recording;
    private RecorderEvent lastEvent;

    public Recorder() {
        componentRecorders.add(new ButtonClickRecorder(this));
        componentRecorders.add(new WindowActivationRecorder(this));
        componentRecorders.add(new TextFieldRecorder(this, new DefaultComponentVisibility()));
        componentRecorders.add(new SelectDropDownRecorder(this, new DefaultComponentDecoder(), new DefaultComponentVisibility()));
    }

    public void init() {
        for (ComponentRecorder recorder : componentRecorders) {
            recorder.register();
        }
    }

    public void shutdown() {
        for (ComponentRecorder recorder : componentRecorders) {
            recorder.unregister();
        }
    }

    public void record(RecorderEvent event) {
        if (!recording || isIgnored(event.getComponent()) || isOnJavaConsole(event.getComponent())) return;
        if (StringUtils.isBlank(event.getComponentLocator())) {
            logger.warn("Component has no way of being located (no name or label): " + event.getComponent());
            return;
        }
        event.record(this, lastEvent);
        lastEvent = events.isEmpty() ? null : events.get(events.size() - 1);
    }

    protected boolean isIgnored(Component component) {
        for (Container container : ignoredContainers) {
            boolean matches = isParentOf(container, component);
            if (matches) return true;
        }
        return false;
    }

    private boolean isParentOf(Container container, Component component) {
        return container == component || component != null && component.getParent() != null && (component.getParent() == container || isParentOf(container, component.getParent()));
    }

    private boolean isOnJavaConsole(Component component) {
        if (component == null) return false;
        JFrame frame = getParentFrame(component);
        return frame != null && frame.getTitle().equalsIgnoreCase("Java Console");
    }

    private JFrame getParentFrame(Component component) {
        if (component == null) return null;
        while (component.getParent() != null && !(component instanceof JFrame)) {
            component = component.getParent();
        }
        if (component instanceof JFrame) {
            return (JFrame) component;
        }
        return null;
    }


    public void addEvent(RecorderEvent event) {
        logger.debug("Logging event: " + event);
        fireEventAdded(event);
        events.add(event);
    }

    public void replaceLastEvent(RecorderEvent event) {
        events.set(events.size() - 1, event);
        logger.debug("Replacing the last event with: " + event);
        fireEventUpdated(events.size() - 1);
    }

    private void fireEventUpdated(int index) {
        for (RecorderEventListener listener : listeners) {
            listener.eventsUpdated(index, events.subList(index, index + 1));
        }
    }

    private void fireEventAdded(RecorderEvent event) {
        for (RecorderEventListener listener : listeners) {
            listener.eventAdded(event);
        }
    }

    private void fireEventsRemoved() {
        for (RecorderEventListener listener : listeners) {
            listener.eventsRemoved();
        }
    }

    private void fireEventsAdded(List<RecorderEvent> events) {
        for (RecorderEventListener listener : listeners) {
            listener.eventsAdded(events);
        }
    }

    public void addIgnoredContainer(Container container) {
        ignoredContainers.add(container);
    }

    public void addListener(RecorderEventListener listener) {
        listeners.add(listener);
    }

    public boolean isRecording() {
        return recording;
    }

    public void setRecording(boolean recording) {
        this.recording = recording;
    }

    public List<RecorderEvent> getEvents() {
        return events;
    }

    public void setEvents(List<RecorderEvent> events) {
        this.events = events;
    }

    public void emptyEvents() {
        events.clear();
        fireEventsRemoved();
    }

    public void addEvents(List<RecorderEvent> events) {
        this.events.addAll(events);
        fireEventsAdded(events);
    }

    public RecorderEvent getLastEvent() {
        return lastEvent;
    }
}
