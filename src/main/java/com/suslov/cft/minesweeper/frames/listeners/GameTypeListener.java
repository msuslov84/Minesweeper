package com.suslov.cft.minesweeper.frames.listeners;

import com.suslov.cft.minesweeper.frames.enums.GameType;

@FunctionalInterface
public interface GameTypeListener {

    void onGameTypeChanged(GameType gameType);
}
