package com.suslov.cft.minesweeper.highscores;

import com.suslov.cft.minesweeper.exceptions.HighscoreException;

public interface Highscore {

    boolean isBestResult(Record record);

    void read() throws HighscoreException;

    void write(Record record) throws HighscoreException;
}
