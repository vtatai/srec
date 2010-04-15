package com.github.srec.rec;

import com.github.srec.rec.RecorderEvent;

import java.util.List;

public interface RecorderEventListener {
    void eventAdded(RecorderEvent event);
    void eventsRemoved();
    void eventsAdded(List<RecorderEvent> event);
    void eventsUpdated(int initialIndex, List<RecorderEvent> recorderEvents);
}
