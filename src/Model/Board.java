package Model;

import Interfaces.ViewUpdateListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Board {
    private int[][] board;
    public static final int STATE_EMPTY = 0;
    public static final int STATE_FOOD = 1;
    public static final int STATE_SNAKE = 2;
    public static final int STATE_HEAD = 3;
    private int score;
    private ArrayList<Position> updatedPositions;
    private ArrayList<Player> scores;

    private int boardWidth;
    private int boardHeight;

    public Board(){
        this.boardWidth = 16;
        this.boardHeight = 25;
        this.board = new int[boardHeight][boardWidth];
        this.score = 0;
        this.scores = new ArrayList<>();
        this.updatedPositions = new ArrayList<>();
    }

    public ArrayList<Player> getScores(){
        return scores;
    }

    public void setScores(ArrayList<Player> scores){
        this.scores = scores;
    }

    public void setBoardParcel(Position position, int state){
        board[position.getY()][position.getX()] = state;
        updatedPositions.add(position);
    }

    public int getBoardParcel(Position position){
        return board[position.getY()][position.getX()];
    }

    public int[][] getBoard() {
        return board;
    }

    public int getBoardWidth() {
        return boardWidth;
    }

    public int getBoardHeight() {
        return boardHeight;
    }

    public ArrayList<Position> getUpdatedPositions(){
        return updatedPositions;
    }

    public int getScore(){ return score;}

    public void incrementScore(){
        score++;
    }

}
