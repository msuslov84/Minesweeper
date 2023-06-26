package com.suslov.cft.minesweeper.frames.listeners;

import com.suslov.cft.minesweeper.frames.enums.ButtonType;

@FunctionalInterface
public interface CellEventListener {

    void onMouseClick(int x, int y, ButtonType buttonType);
}
