import Controller.GameController;
import Model.Board;
import Model.Snake;
import View.BoardView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }

    public Main(){
        Board board = new Board();
        Snake snake = new Snake(13,24);
        GameController gc = new GameController(board,snake);
        BoardView bv = new BoardView();
        gc.addGameEventListener(bv);
        bv.addKeyboardListener(gc);
        bv.setProvider(gc);
    }
}