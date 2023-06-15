package Events;

import Model.Board;
import Model.Position;
import java.util.ArrayList;

public class TickEvent extends GameEvent {
    private Board board;
    private ArrayList<Position> updatedPositions;

    public TickEvent(Object source, Board board, ArrayList<Position> updatedPositions){
        super(source);
        this.board = board;
        this.updatedPositions = updatedPositions;
    }

    public Board getBoard() {
        return this.board;
    }

    public ArrayList<Position> getUpdatedPositions() {
        return this.updatedPositions;
    }
}
