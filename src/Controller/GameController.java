package Controller;

import Events.*;
import Interfaces.*;
import Model.*;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

import static Model.Board.*;

public class GameController implements Runnable, KeyboardListener, BoardProvider, NicknameProvidedEventListener, GameResetListener {
    private Board board;
    private boolean gameRunning;
    private Thread gameThread;
    private Snake snake;
    private Random random;
    private ArrayList<TickEventListener> tickListeners = new ArrayList<>();
    private ArrayList<EatEventListener> eatListeners = new ArrayList<>();
    private ArrayList<CrashEventListener> crashListeners = new ArrayList<>();
    private ArrayList<DialogEventListener> dialogListener = new ArrayList<>();


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
            if((segment.getPosition().getX() == snake.getHead().getPosition().getX()) && (segment.getPosition().getY() == snake.getHead().getPosition().getY())){
                board.getBoard()[segment.getPosition().getY()][segment.getPosition().getX()] = STATE_HEAD;
            }
            else {
                board.getBoard()[segment.getPosition().getY()][segment.getPosition().getX()] = STATE_SNAKE;
            }
        }
        generateFood();
        loadBestPlayers();

    }


    public void startGame() {
        this.gameRunning = true;
        this.gameThread = new Thread(this);
        this.gameThread.start();
    }

    public void stopGame() {
        this.gameRunning = false;
    }

    public void moveSnake(int direction) {
        Position beforeHeadPos = new Position(snake.getHead().getPosition().getX(), snake.getHead().getPosition().getY());
        Position newHeadPos = new Position(beforeHeadPos.getX(), beforeHeadPos.getY());
        boolean crashed = false;

        switch (direction) {
            case Snake.UP -> newHeadPos.setY(newHeadPos.getY() - 1);
            case Snake.DOWN -> newHeadPos.setY(newHeadPos.getY() + 1);
            case Snake.LEFT -> newHeadPos.setX(newHeadPos.getX() - 1);
            case Snake.RIGHT -> newHeadPos.setX(newHeadPos.getX() + 1);
        }

        if (newHeadPos.getX() < 0 || newHeadPos.getX() >= board.getBoardWidth() ||
                newHeadPos.getY() < 0 || newHeadPos.getY() >= board.getBoardHeight() ||
                board.getBoardParcel(new Position(newHeadPos.getX(), newHeadPos.getY())) == STATE_SNAKE) {
            CrashEvent crashEvent = new CrashEvent(this,board.getScores());
            for (CrashEventListener listener : crashListeners) {
                listener.onCrash(crashEvent);
            }
            boolean shouldAsk = false;
            if(board.getScores().size() == 10) {
                for (Player player : board.getScores()) {
                    if (player.getScore() < board.getScore()) {
                        shouldAsk = true;
                        break;
                    }
                }
            }
            else {
                shouldAsk = true;
            }
            if(board.getScore() > 5 && shouldAsk) {
                DialogEvent dialogEvent = new DialogEvent(this, board.getScore());
                for (DialogEventListener listener : dialogListener) {
                    listener.onDialog(dialogEvent);
                }
            }
            stopGame();
            crashed = true;
        }

        if (!crashed) {
            if (snake.getSegments().size() > 1) {
                beforeHeadPos = snake.getTail().getPosition();
            }

            if (board.getBoardParcel(newHeadPos) == Board.STATE_FOOD) {
                snake.move(newHeadPos, true);
                board.setBoardParcel(newHeadPos, STATE_SNAKE);
                generateFood();
                board.incrementScore();
                EatEvent eatEvent = new EatEvent(this, board.getScore());
                for (EatEventListener listener : eatListeners) {
                    listener.onEat(eatEvent);
                }
            } else {
                snake.move(newHeadPos, false);
                board.setBoardParcel(beforeHeadPos, STATE_EMPTY);
                board.setBoardParcel(newHeadPos, STATE_SNAKE);
            }

            for(Segment seg : snake.getSegments()){
                Position segPos = new Position(seg.getPosition().getX(), seg.getPosition().getY());
                if((segPos.getX() == snake.getHead().getPosition().getX()) && (segPos.getY() == snake.getHead().getPosition().getY())){
                    board.setBoardParcel(segPos, STATE_HEAD);
                }
                else {
                    board.setBoardParcel(segPos, STATE_SNAKE);
                }
            }
        }
        board.setBoardParcel(snake.getHead().getPosition(), STATE_HEAD);
    }


    private void loadBestPlayers(){
        File file = new File("./Scores/BestScores.bin");
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
        try {
            FileInputStream fis = new FileInputStream(file);
            int index = 0;
            int nameL;
            while ((nameL = fis.read()) != -1) {
                byte[] nameB = new byte[nameL];
                fis.read(nameB);
                String name = new String(nameB);
                int score = (fis.read() << 24) | (fis.read() << 16) | (fis.read() << 8) | fis.read();
                board.getScores().add(index++, new Player(name, score));
            }
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        }
    }

    public void saveScore(String name, int score) {
        board.getScores().add(new Player(name, score));
        board.getScores().sort((pl1, pl2) -> Integer.compare(pl2.getScore(), pl1.getScore()));
        while (board.getScores().size() > 10) {
            board.getScores().remove(board.getScores().size() - 1);
        }
        try {
            FileOutputStream fos = new FileOutputStream("./Scores/BestScores.bin");
            for (Player player : board.getScores()) {
                fos.write(player.getName().length());
                fos.write(player.getName().getBytes());
                int playerScore = player.getScore();
                fos.write((playerScore >> 24) & 0xFF);
                fos.write((playerScore >> 16) & 0xFF);
                fos.write((playerScore >> 8) & 0xFF);
                fos.write(playerScore & 0xFF);
            }
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReset() {
        this.board = new Board();
        this.snake = new Snake(10,20);
        initializeGame();
        startGame();
    }


    @Override
    public void run() {
        while (gameRunning) {
            TickEvent tickEvent = new TickEvent(this, board, board.getUpdatedPositions());
            for (TickEventListener listener : tickListeners) {
                listener.onTick(tickEvent);
            }
            moveSnake(snake.getDirection());
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void addTickEventListener(TickEventListener listener) {
        this.tickListeners.add(listener);
    }
    public void addCrashEventListener(CrashEventListener listener) {
        this.crashListeners.add(listener);
    }
    public void addEatEventListener(EatEventListener listener) {
        this.eatListeners.add(listener);
    }

    public void addDialogEventListener(DialogEventListener listener){
        this.dialogListener.add(listener);
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
        if(!((event.getDirection() == Snake.UP && snake.getDirection() == Snake.DOWN) || (event.getDirection() == Snake.DOWN && snake.getDirection() == Snake.UP) ||
                (event.getDirection() == Snake.LEFT && snake.getDirection() == Snake.RIGHT) || (event.getDirection() == Snake.RIGHT && snake.getDirection() == Snake.LEFT)
                )){
            snake.setDirection(event.getDirection());
        }
    }

    @Override
    public void onNicknameProvided(NicknameProvidedEvent nicknameProvidedEvent) {
        saveScore(nicknameProvidedEvent.getNickname(), board.getScore());
    }

    @Override
    public Board getBoard() {
        return board;
    }




}
