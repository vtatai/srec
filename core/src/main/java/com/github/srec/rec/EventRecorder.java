package com.github.srec.rec;

import com.github.srec.command.MethodCallEventCommand;

/**
 * Low level event recorder.
 *
 * @author Vivek Prahlad
 */
public interface EventRecorder {
    void init();
    void shutdown();
    void record(MethodCallEventCommand event);
    MethodCallEventCommand getLastEvent();
}
