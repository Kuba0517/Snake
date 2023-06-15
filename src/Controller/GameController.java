package Controller;

import Events.KeyboardEvent;
import Interfaces.KeyboardListener;
import Interfaces.ViewUpdateListener;
import Model.Board;
import Model.Position;
import Model.Segment;
import Model.Snake;

import java.awt.*;
import java.util.Random;

import static Model.Board.*;

public class GameController implements KeyboardListener, ViewUpdateListener<Board> {
    private Board board;
    private boolean gameRunning;
    private Thread gameThread;
    private Snake snake;
    private Point food;
    private Random random;
    private ViewUpdateListener<Board> view;

    public GameController(Board board, Snake snake) {
        this.board = board;
        this.gameRunning = false;
        this.snake = snake;
        this.random = new Random();
        initializeGame();
        moveSnake(Snake.RIGHT);
        moveSnake(Snake.RIGHT);
        moveSnake(Snake.DOWN);
        moveSnake(Snake.DOWN);
        moveSnake(Snake.DOWN);
        moveSnake(Snake.DOWN);
        moveSnake(Snake.DOWN);
        startGame();
    }

    private void initializeGame() {
        for (Segment segment : snake.getSegments()) {
            board.getBoard()[segment.getPosition().getY()][segment.getPosition().getX()] = STATE_SNAKE;
        }
        generateFood();
    }

    public void setView(ViewUpdateListener<Board> view) {
        this.view = view;
        board.addViewUpdateListener(view);
    }

    public void startGame() {
        this.gameRunning = true;
        this.gameThread = new Thread(this::gameLoop);
        this.gameThread.start();
        System.out.println("Started");
    }

    public void stopGame() {
        this.gameRunning = false;
        try {
            this.gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void moveSnake(int direction) {
        Position headPos = new Position(snake.getHead().getPosition().getX(), snake.getHead().getPosition().getY());

        switch (direction) {
            case Snake.UP -> headPos.setY(headPos.getY() - 1);
            case Snake.DOWN -> headPos.setY(headPos.getY() + 1);
            case Snake.LEFT -> headPos.setX(headPos.getX() - 1);
            case Snake.RIGHT -> headPos.setX(headPos.getX() + 1);
        }

        // Sprawdzenie, czy nowa pozycja jest poza granicami planszy
        if (headPos.getX() < 0 || headPos.getX() >= board.getBoardWidth() ||
                headPos.getY() < 0 || headPos.getY() >= board.getBoardHeight()) {
            System.out.println("If out of board");
            //stopGame();
            return; // Game over
        }

        if (board.getBoardParcel(headPos) == Board.STATE_SNAKE) {
            stopGame();
            System.out.println("If snakebod");
            return; // Game over
        }

        if (board.getBoardParcel(headPos) == Board.STATE_FOOD) {
            System.out.println("If food");
            snake.addSegment();
            generateFood();
        } else {
            System.out.println("Else food");
            // Jeżeli wąż nie zjadł jedzenia, usuwamy ostatni segment
            Position tail = snake.getSegments().getLast().getPosition();
            snake.move(headPos);
            board.setBoardParcel(tail, Board.STATE_EMPTY);
        }
    }


    private void gameLoop() {
        while (gameRunning) {
            moveSnake(snake.getDirection());
            try {
                Thread.sleep(200);  // Czekamy, zanim wykonamy kolejny ruch – to wartość możemy dostosować do prędkości gry
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void generateFood() {
        int x, y;
        do {
            x = random.nextInt(board.getBoardWidth());
            y = random.nextInt(board.getBoardHeight());
        } while (board.getBoard()[y][x] != STATE_EMPTY);
        board.getBoard()[y][x] = STATE_FOOD;
        food = new Point(x, y);
    }

    @Override
    public void directionChanged(KeyboardEvent event) {
        System.out.println("Direction changed to: " + event.getDirection());
        if (Math.abs(snake.getDirection() - event.getDirection()) != 2) {
            snake.setDirection(event.getDirection());
        }
    }

    @Override
    public void updateView(Board item) {
        if(view != null){
            view.updateView(item);
            System.out.println("Aktualizacja widoku");
        }
    }
}

