package com.github.srec.rec;

import com.github.srec.command.CallEventCommand;

/**
 * Low level event recorder.
 *
 * @author Vivek Prahlad
 */
public interface EventRecorder {
    void init();
    void shutdown();
    void record(CallEventCommand event);
    CallEventCommand getLastEvent();
}
