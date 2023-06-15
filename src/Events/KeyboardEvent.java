package Events;

import java.util.EventObject;

public class KeyboardEvent extends EventObject {
    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;

    private final int direction;

    public KeyboardEvent(Object source, int direction) {
        super(source);
        this.direction = direction;
    }

    public int getDirection() {
        return direction;
    }
}
