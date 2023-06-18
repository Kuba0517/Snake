package Events;

public class DialogEvent extends GameEvent{
    private int score;
    public DialogEvent(Object source, int score){
        super(source);
        this.score = score;
    }

    public int getScore() {
        return score;
    }
}
