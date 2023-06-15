package Interfaces;

import Events.KeyboardEvent;


import java.util.EventListener;

public interface KeyboardListener extends EventListener {
    void directionChanged(KeyboardEvent event);
}
