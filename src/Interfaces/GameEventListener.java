package Interfaces;


import Events.CrashEvent;
import Events.EatEvent;
import Events.TickEvent;

import java.util.EventListener;

public interface GameEventListener extends EventListener {
    void onTick(TickEvent event);
    void onEat(EatEvent event);
    void onCrash(CrashEvent event);
}
