package com.github.srec.rec;

import com.github.srec.rec.RecorderEvent;

/**
 * Low level event recorder.
 *
 * @author Vivek Prahlad
 */
public interface EventRecorder {
    void init();
    void shutdown();
    void record(RecorderEvent event);
    RecorderEvent getLastEvent();
}
