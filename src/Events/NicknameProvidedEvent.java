package Events;

import java.util.EventObject;

public class NicknameProvidedEvent extends EventObject {
    String nickname;

    public NicknameProvidedEvent(Object source, String nickname){
        super(source);
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }
}
