package com.suslov.cft.minesweeper.observers;

import com.suslov.cft.minesweeper.events.GameAction;

public interface GameObservable {

    void addObserver(GameObserver observer);

    void notifyObservers(GameAction action);
}
