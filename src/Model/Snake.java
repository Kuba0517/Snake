package Model;

import Interfaces.ViewUpdateListener;

import java.util.ArrayList;
import java.util.LinkedList;

public class Snake {

    private LinkedList<Segment> body;
    private int direction;
    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;


    public Snake(int initialX, int initialY) {
        body = new LinkedList<>();
        Segment seg = new Segment();
        seg.setPosition(new Position(initialX, initialY));
        body.addFirst(seg);
        direction = LEFT;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getDirection() {
        return direction;
    }

    public Segment getHead() {
        return body.getFirst();
    }

    public Segment getTail(){return body.getLast();}

    public void addSegment(Position pos) {
        Segment seg = new Segment();
        seg.setPosition(pos);
        body.addLast(seg);
    }


    public void move(Position newPosition, boolean isEating) {
        Segment newHead = new Segment();
        newHead.setPosition(newPosition);
        body.addFirst(newHead);
        if (!isEating) {
            body.removeLast();
        }
    }


    public LinkedList<Segment> getSegments() {
        return body;
    } // zebym mogl korzystac z addFirst, addLast itp


}

