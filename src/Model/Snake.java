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


    public void move(Position newPosition) {
        Position oldHeadPosition = new Position(getHead().getPosition().getX(), getHead().getPosition().getY());

        Segment newHead = new Segment();
        newHead.setPosition(newPosition);
        System.out.println("Moving head to the new position " + newPosition.getX() + " y: " + newPosition.getY());
        body.addFirst(newHead);
        body.removeLast();


        if (body.size() > 1) {
            body.removeLast();
            Segment newTail = new Segment();
            newTail.setPosition(oldHeadPosition);
            body.addLast(newTail);
        }
    }


    public LinkedList<Segment> getSegments() {
        return body;
    }


}

