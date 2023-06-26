package com.suslov.cft.minesweeper.controllers;

import com.suslov.cft.minesweeper.events.ViewAction;

public interface Controller {

    void startGame();

    void onMouseClick(ViewAction.Content content);

    void onGameTypeChanged(ViewAction.Content content);

    void onRecordNameEntered(ViewAction.Content content);

    void actionPerformed(ViewAction.Content content);
}
