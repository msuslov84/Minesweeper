package com.suslov.cft.minesweeper.exceptions;

public class HighscoreException extends GameException {

    public HighscoreException(String message, Throwable cause) {
        super(message, cause);
    }

    public HighscoreException(String message) {
        super(message);
    }
}
