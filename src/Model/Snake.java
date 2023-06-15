package Model;

import Interfaces.ViewUpdateListener;

import java.util.ArrayList;
import java.util.LinkedList;

public class Snake {

    private LinkedList<Segment> body;
    private int direction;
    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int RIGHT = 2;
    public static final int LEFT = 3;

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

    public void addSegment() {
        Segment seg = new Segment();
        seg.setPosition(new Position(body.getLast().getPosition().getX(), body.getLast().getPosition().getY()));
        body.addLast(seg);
    }


    public void move(Position newPosition) {
        // Save old head position
        Position oldHeadPosition = new Position(getHead().getPosition().getX(), getHead().getPosition().getY());

        // Move the head
        Segment newHead = new Segment();
        newHead.setPosition(newPosition);
        System.out.println("Moving head to the new position " + newPosition.getX() + " y: " + newPosition.getY());
        body.addFirst(newHead);
        body.removeLast();

        // Move the tail to the old head position if the body size is more than 1
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

    public boolean checkCollision(Segment segment) {
        return body.contains(segment);
    }

}

