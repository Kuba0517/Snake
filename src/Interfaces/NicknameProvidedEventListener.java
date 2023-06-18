package Interfaces;

import Events.NicknameProvidedEvent;

import java.util.EventListener;

public interface NicknameProvidedEventListener extends EventListener {
    void onNicknameProvided(NicknameProvidedEvent nicknameProvidedEvent);
}
