package com.suslov.cft.minesweeper.observers;

import com.suslov.cft.minesweeper.events.view.ViewAction;

public interface ViewObserver {

    void update(ViewAction action);
}
