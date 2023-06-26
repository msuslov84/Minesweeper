package com.suslov.cft.minesweeper.observers;

import com.suslov.cft.minesweeper.events.GameAction;

public interface GameObserver {

    void update(GameAction action);
}
