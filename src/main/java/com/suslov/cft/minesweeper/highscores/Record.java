package com.suslov.cft.minesweeper.highscores;

import com.suslov.cft.minesweeper.exceptions.HighscoreException;
import com.suslov.cft.minesweeper.frames.enums.GameType;

import java.util.Objects;

public class Record implements Comparable<Record> {

    private final GameType gameType;
    private final int timeValue;
    private String winnerName;

    public Record(GameType gameType, String winnerName, int timeValue) {
        this(gameType, timeValue);
        this.winnerName = winnerName;
    }

    public Record(GameType gameType, int timeValue) {
        this.gameType = gameType;
        this.timeValue = timeValue;
    }

    public GameType getGameType() {
        return gameType;
    }

    public String getWinnerName() {
        return Objects.requireNonNullElse(winnerName, "");
    }

    public int getTimeValue() {
        return timeValue;
    }

    @Override
    public int compareTo(Record o) {
        if (o.getGameType() != getGameType()) {
            throw new HighscoreException("Incorrect comparison of different game type records");
        }
        return timeValue - o.getTimeValue();
    }
}
