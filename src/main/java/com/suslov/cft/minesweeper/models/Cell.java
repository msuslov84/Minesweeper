package com.suslov.cft.minesweeper.models;

public class Cell {
    private final int x;
    private final int y;
    private State state = State.CLOSED;
    private boolean isMine;
    private int countMinesAround;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public State getState() {
        return state;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public int getCountMinesAround() {
        return countMinesAround;
    }

    public void setCountMinesAround(int countMinesAround) {
        this.countMinesAround = countMinesAround;
    }

    public boolean nextState() {
        if (state == State.MARKED) {
            state = State.CLOSED;
            return true;
        } else if (state == State.CLOSED) {
            state = State.MARKED;
            return true;
        }
        return false;
    }

    public void open() {
        if (state == State.CLOSED) {
            state = State.OPENED;
        }
    }

    public enum State {
        CLOSED,
        MARKED,
        OPENED
    }
}
