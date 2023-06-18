import Controller.GameController;
import Model.Board;
import Model.Snake;
import View.BoardView;
import View.GameOverView;
import View.ScorePanelView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }

    public Main(){
        Board board = new Board();
        Snake snake = new Snake(10,20);

        GameController gc = new GameController(board,snake);

        GameOverView gov = new GameOverView();
        ScorePanelView sp = new ScorePanelView();
        BoardView bv = new BoardView(sp,gov);

        gov.addNicknameProvidedEventListener(gc);
        gov.addGameResetListener(gc);
        gov.addGameResetListener(bv);

        gc.addDialogEventListener(gov);
        gc.addTickEventListener(bv);
        gc.addEatEventListener(sp);
        gc.addCrashEventListener(bv);

        bv.addKeyboardListener(gc);
        bv.setProvider(gc);
    }
}