package Controller;

import Events.CrashEvent;
import Events.EatEvent;
import Events.KeyboardEvent;
import Events.TickEvent;
import Interfaces.GameEventListener;
import Interfaces.KeyboardListener;
import Interfaces.Provider;
import Interfaces.ViewUpdateListener;
import Model.Board;
import Model.Position;
import Model.Segment;
import Model.Snake;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import static Model.Board.*;

public class GameController implements KeyboardListener, Provider {
    private Board board;
    private boolean gameRunning;
    private Thread gameThread;
    private Snake snake;
    private Point food;
    private Random random;
    private ViewUpdateListener<Board> view;
    private ArrayList<GameEventListener> listeners = new ArrayList<>();


    public GameController(Board board, Snake snake) {
        this.board = board;
        this.gameRunning = false;
        this.snake = snake;
        this.random = new Random();
        initializeGame();
        startGame();
    }

    private void initializeGame() {
        for (Segment segment : snake.getSegments()) {
            board.getBoard()[segment.getPosition().getY()][segment.getPosition().getX()] = STATE_SNAKE;
        }
        generateFood();
    }


    public void startGame() {
        this.gameRunning = true;
        this.gameThread = new Thread(this::gameLoop);
        this.gameThread.start();
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

        Position tailPos = null;
        if (snake.getSegments().size() > 1) {
            tailPos = snake.getTail().getPosition();
        }

        if (board.getBoardParcel(headPos) == Board.STATE_FOOD) {
            snake.move(headPos);
            snake.addSegment(headPos);
            board.setBoardParcel(headPos, STATE_EMPTY);
            generateFood();
            EatEvent eatEvent = new EatEvent(this);
                for (GameEventListener listener : listeners) {
                    listener.onEat(eatEvent);
                }
        } else {
            Position tail = snake.getSegments().getLast().getPosition();
            snake.move(headPos);
            board.setBoardParcel(tail, STATE_EMPTY);
            board.setBoardParcel(headPos, STATE_SNAKE);
        }
    }



    private void gameLoop() {
        while (gameRunning) {
            TickEvent tickEvent = new TickEvent(this, board, board.getUpdatedPositions());
            for (GameEventListener listener : listeners) {
                listener.onTick(tickEvent);
            }
            moveSnake(snake.getDirection());
//            }
//            if (snake.getHead().getPosition().getX() < 0 || snake.getHead().getPosition().getX() >= board.getBoardWidth() ||
//                    snake.getHead().getPosition().getY() < 0 || snake.getHead().getPosition().getY() >= board.getBoardHeight() ||
//                    board.getBoardParcel(snake.getHead().getPosition()) == Board.STATE_SNAKE) {
//                CrashEvent crashEvent = new CrashEvent(this);
//                for (GameEventListener listener : listeners) {
//                    listener.onCrash(crashEvent);
//                }
//                stopGame();
//                return;
//            }
            try {
                Thread.sleep(120);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void addGameEventListener(GameEventListener listener) {
        this.listeners.add(listener);
    }

    private void generateFood() {
        int x, y;
        do {
            x = random.nextInt(board.getBoardWidth());
            y = random.nextInt(board.getBoardHeight());
        } while (board.getBoard()[y][x] != STATE_EMPTY);
        board.setBoardParcel(new Position(x, y), STATE_FOOD);
    }

    @Override
    public void directionChanged(KeyboardEvent event) {
        if(!((event.getDirection() == Snake.UP && snake.getDirection() == Snake.DOWN) || (event.getDirection() == Snake.DOWN && snake.getDirection() == Snake.UP))){
            snake.setDirection(event.getDirection());
        }
    }


    @Override
    public Board getBoard() {
        return board;
    }




}
