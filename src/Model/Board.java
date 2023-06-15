package Model;

import Interfaces.ViewUpdateListener;

import java.util.ArrayList;

public class Board {
    private int[][] board;
    public static final int STATE_EMPTY = 0;
    public static final int STATE_FOOD = 1;
    public static final int STATE_SNAKE = 2;
    private ArrayList<Position> updatedPositions;

    private int boardWidth;
    private int boardHeight;

    public Board(){
        this.boardWidth = 16;
        this.boardHeight = 25;
        this.board = new int[boardHeight][boardWidth];
        updatedPositions = new ArrayList<>();
    }

    public void washBoard(){
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[i].length; j++){
                board[i][j] = STATE_EMPTY;
            }
        }
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

}
