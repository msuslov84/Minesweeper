package com.suslov.cft.minesweeper.observers;

import com.suslov.cft.minesweeper.highscores.Record;

public interface RecordObservable {

    void addObserver(RecordObserver observer);

    void notifyObservers(Record record);
}
