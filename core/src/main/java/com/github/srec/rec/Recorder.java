package com.github.srec.rec;

import com.github.srec.command.CallEventCommand;
import com.github.srec.command.Command;
import com.github.srec.command.ExecutionContext;
import com.github.srec.rec.common.DefaultComponentDecoder;
import com.github.srec.rec.component.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The recorder used to record the user's interactions with the Swing application.
 *
 * @author Victor Tatai
 */
public class Recorder implements EventRecorder {
    private static final Logger logger = Logger.getLogger(Recorder.class);
    private ExecutionContext executionContext;
    private List<ComponentRecorder> componentRecorders = new ArrayList<ComponentRecorder>();
    private List<RecorderCommandListener> listeners = new ArrayList<RecorderCommandListener>();
    /**
     * The list of ignored containers, such as the recording frame itself.
     */
    private List<Container> ignoredContainers = new ArrayList<Container>();
    private boolean recording;
    private CallEventCommand lastEvent;

    public Recorder(ExecutionContext context) {
        this.executionContext = context;
        componentRecorders.add(new ButtonClickRecorder(this));
        componentRecorders.add(new WindowActivationRecorder(this));
        componentRecorders.add(new TextFieldRecorder(this, new DefaultComponentVisibility()));
        componentRecorders.add(new SelectDropDownRecorder(this, new DefaultComponentDecoder(), new DefaultComponentVisibility()));
        componentRecorders.add(new CheckTextRecorder(this));
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

    public void record(CallEventCommand event) {
        if (!recording || isIgnored(event.getComponent()) || isOnJavaConsole(event.getComponent())) return;
        if (StringUtils.isBlank(event.getComponentLocator())) {
            logger.warn("Component has no way of being located (no name or label): " + event.getComponent());
            return;
        }
        event.record(this, lastEvent);
        lastEvent = extractLastEvent();
    }

    private CallEventCommand extractLastEvent() {
        int lastIndex = getCommands().size() - 1;
        return getCommands().isEmpty() || !(getCommands().get(lastIndex) instanceof CallEventCommand) ? null : (CallEventCommand) getCommands().get(lastIndex);
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


    public void addEvent(CallEventCommand event) {
        logger.debug("Logging event: " + event);
        executionContext.addCommand(event);
        fireEventAdded(event);
    }

    public void replaceLastEvent(CallEventCommand event) {
        getCommands().set(getCommands().size() - 1, event);
        logger.debug("Replacing the last event with: " + event);
        fireEventUpdated(getCommands().size() - 1);
    }

    private void fireEventUpdated(int index) {
        for (RecorderCommandListener listener : listeners) {
            listener.eventsUpdated(index, getCommands().subList(index, index + 1));
        }
    }

    private void fireEventAdded(CallEventCommand event) {
        for (RecorderCommandListener listener : listeners) {
            listener.eventAdded(event);
        }
    }

    private void fireEventsRemoved() {
        for (RecorderCommandListener listener : listeners) {
            listener.eventsRemoved();
        }
    }

    private void fireEventsAdded(List<Command> commands) {
        for (RecorderCommandListener listener : listeners) {
            listener.commandsAdded(commands);
        }
    }

    public void addIgnoredContainer(Container container) {
        ignoredContainers.add(container);
    }

    public void addListener(RecorderCommandListener listener) {
        listeners.add(listener);
    }

    public boolean isRecording() {
        return recording;
    }

    public void setRecording(boolean recording) {
        this.recording = recording;
    }

    public List<Command> getCommands() {
        return executionContext.getCommands();
    }

    public void emptyCommands() {
        getCommands().clear();
        fireEventsRemoved();
    }

    public void addCommands(List<Command> commands) {
        this.executionContext.addCommand(commands.toArray(new Command[commands.size()]));
        fireEventsAdded(commands);
    }

    public CallEventCommand getLastEvent() {
        return lastEvent;
    }

    public ExecutionContext getExecutionContext() {
        return executionContext;
    }
}
