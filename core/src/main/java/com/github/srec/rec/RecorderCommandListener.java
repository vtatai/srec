package com.github.srec.rec;

import com.github.srec.command.CallEventCommand;
import com.github.srec.command.Command;

import java.util.List;

/**
 * @author Victor Tatai
 */
public interface RecorderCommandListener {
    void eventAdded(CallEventCommand event);
    void eventsRemoved();
    void commandsAdded(List<Command> commands);
    void eventsUpdated(int initialIndex, List<Command> commands);
}
