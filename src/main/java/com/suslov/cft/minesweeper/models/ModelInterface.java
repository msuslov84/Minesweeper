package com.suslov.cft.minesweeper.models;

import com.suslov.cft.minesweeper.exceptions.GameException;
import com.suslov.cft.minesweeper.frames.enums.GameType;

public interface ModelInterface {

    void startGame();

    void restartGame(GameType type);

    void openCell(int x, int y) throws GameException;

    void changeCellState(int x, int y) throws GameException;

    void openCellsAround(int x, int y) throws GameException;

    FieldSettings getFieldSettings();

    GameType getGameType();

    int receiveTimeRemainder();
}
