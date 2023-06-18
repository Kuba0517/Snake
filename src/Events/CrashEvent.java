package Events;

import Model.Player;

import java.util.ArrayList;

public class CrashEvent extends GameEvent{
    private ArrayList<Player> scores;
    public CrashEvent(Object source, ArrayList<Player> scores){
        super(source);
        this.scores = scores;
    }
    public ArrayList<Player> getScores() {
        return scores;
    }
}
