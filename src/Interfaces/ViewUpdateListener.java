package Interfaces;

import Model.Position;

import java.util.ArrayList;

public interface ViewUpdateListener<T> {
    void updateView(T item, ArrayList<Position> updatedPositions);
}
