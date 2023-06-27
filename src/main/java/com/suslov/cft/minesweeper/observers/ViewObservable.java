package com.suslov.cft.minesweeper.observers;

import com.suslov.cft.minesweeper.events.view.ViewAction;

public interface ViewObservable {

    void addObserver(ViewObserver observer);

    void notifyObservers(ViewAction action);
}
