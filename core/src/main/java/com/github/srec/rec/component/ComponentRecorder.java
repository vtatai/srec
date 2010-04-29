package com.github.srec.rec.component;

/**
 * Understands listening to AWT component events.
 *
 * @author Vivek Prahlad
 */
public interface ComponentRecorder {
    void register();

    void unregister();
}
