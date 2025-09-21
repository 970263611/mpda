package com.dahuaboke.mpda.core.event;

public interface ChangeEvent extends Event {

    enum Type {
        ADDED,
        REMOVED,
    }
}
