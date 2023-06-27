package com.suslov.cft.minesweeper.observers;

import com.suslov.cft.minesweeper.events.game.GameAction;

public interface GameObserver {

    void update(GameAction action);
}
