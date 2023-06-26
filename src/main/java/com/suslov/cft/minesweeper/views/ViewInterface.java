package com.suslov.cft.minesweeper.views;

import com.suslov.cft.minesweeper.models.FieldSettings;
import com.suslov.cft.minesweeper.to.CellTo;

public interface ViewInterface {

    void setCellImage(CellTo cell);

    void presentLoseWindow();

    void presentWinWindow();

    void presentRecordsWindow();

    void createNewField(FieldSettings settings);

    void updateMineCounter(int counter);

    void updateTimerCounter(int counter);
}