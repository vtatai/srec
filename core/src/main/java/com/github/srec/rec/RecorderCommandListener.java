package com.github.srec.rec;

import com.github.srec.command.Command;
import com.github.srec.command.EventCommand;

import java.util.List;

/**
 * @author Victor Tatai
 */
public interface RecorderCommandListener {
    void eventAdded(EventCommand event);
    void eventsRemoved();
    void commandsAdded(List<Command> commands);
    void eventsUpdated(int initialIndex, List<Command> commands);
}
