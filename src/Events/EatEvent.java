package Events;

public class EatEvent extends GameEvent {
    private int score;

    public EatEvent(Object source, int score){
        super(source);
        this.score = score;
    }

    public int getScore() {
        return score;
    }
}

