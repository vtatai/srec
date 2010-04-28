package com.github.srec.rec;

import com.github.srec.command.EventCommand;

/**
 * Low level event recorder.
 *
 * @author Vivek Prahlad
 */
public interface EventRecorder {
    void init();
    void shutdown();
    void record(EventCommand event);
    EventCommand getLastEvent();
}
