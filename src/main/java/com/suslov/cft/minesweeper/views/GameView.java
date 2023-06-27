package com.suslov.cft.minesweeper.views;

import com.suslov.cft.minesweeper.events.game.GameAction;
import com.suslov.cft.minesweeper.events.view.ViewAction;
import com.suslov.cft.minesweeper.events.view.ViewActionType;
import com.suslov.cft.minesweeper.frames.*;
import com.suslov.cft.minesweeper.highscores.Record;
import com.suslov.cft.minesweeper.models.FieldSettings;
import com.suslov.cft.minesweeper.observers.*;
import com.suslov.cft.minesweeper.to.CellTo;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class GameView implements ViewInterface, GameObserver, RecordObserver, ViewObservable {
    private final List<ViewObserver> observers = new ArrayList<>();
    private final MainWindow mainWindow;
    private final SettingsWindow settingsWindow;
    private final HighScoresWindow highScoresWindow;
    private final LoseWindow loseWindow;
    private final WinWindow winWindow;
    private final RecordsWindow recordsWindow;

    public GameView(FieldSettings settings, GameObservable model, RecordObservable records) {
        model.addObserver(this);
        records.addObserver(this);

        mainWindow = new MainWindow();
        settingsWindow = new SettingsWindow(mainWindow);
        highScoresWindow = new HighScoresWindow(mainWindow);
        loseWindow = new LoseWindow(mainWindow);
        winWindow = new WinWindow(mainWindow);
        recordsWindow = new RecordsWindow(mainWindow);

        initializeWindowActions();
        createNewField(settings);
    }

    private void initializeWindowActions() {
        mainWindow.setSettingsMenuAction(e -> settingsWindow.setVisible(true));
        mainWindow.setHighScoresMenuAction(e -> highScoresWindow.setVisible(true));
        mainWindow.setExitMenuAction(e -> mainWindow.dispose());
        mainWindow.setCellListener((x, y, buttonType) -> {
            ViewAction clickAction = new ViewAction(ViewActionType.CLICK, new ViewAction.Content(x, y, buttonType));
            notifyObservers(clickAction);
        });
        settingsWindow.setGameTypeListener(gameType -> {
            ViewAction gameTypeAction = new ViewAction(ViewActionType.SETTINGS, new ViewAction.Content(gameType));
            notifyObservers(gameTypeAction);
        });
        mainWindow.setNewGameMenuAction(this::sendNewGameNotificationToObservers);

        loseWindow.setNewGameListener(this::sendNewGameNotificationToObservers);
        loseWindow.setExitListener(e -> mainWindow.dispose());
        loseWindow.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        winWindow.setNewGameListener(this::sendNewGameNotificationToObservers);
        winWindow.setExitListener(e -> mainWindow.dispose());
        winWindow.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        recordsWindow.setNameListener(name -> {
            ViewAction recordAction = new ViewAction(ViewActionType.RECORD, new ViewAction.Content(name));
            notifyObservers(recordAction);
        });
    }

    private void sendNewGameNotificationToObservers(ActionEvent event) {
        notifyObservers(new ViewAction(ViewActionType.NEW_GAME, new ViewAction.Content(event)));
    }

    @Override
    public void update(GameAction action) {
        GameAction.Content content = action.getContent();
        switch (action.type()) {
            case CELL -> setCellImage(content.getCellTo());
            case TIMER -> updateTimerCounter(content.getTimerValue());
            case MINE -> updateMineCounter(content.getRemainderMines());
            case LOSE -> presentLoseWindow();
            case WIN -> {
                if (content.isBestResult()) {
                    presentRecordsWindow();
                }
                presentWinWindow();
            }
            case RESTART -> createNewField(content.getSettings());
        }
    }

    @Override
    public void setCellImage(CellTo cellTo) {
        mainWindow.setCellImage(cellTo.x(), cellTo.y(), cellTo.image());
    }

    @Override
    public void updateTimerCounter(int counter) {
        mainWindow.setTimerValue(counter);
    }

    @Override
    public void updateMineCounter(int counter) {
        mainWindow.setBombsCount(counter);
    }

    @Override
    public void presentLoseWindow() {
        loseWindow.setVisible(true);
    }

    @Override
    public void presentRecordsWindow() {
        recordsWindow.setVisible(true);
    }

    @Override
    public void presentWinWindow() {
        winWindow.setVisible(true);
    }

    @Override
    public void createNewField(FieldSettings settings) {
        mainWindow.createGameField(settings.height(), settings.width());
        mainWindow.setVisible(true);
        mainWindow.setBombsCount(settings.numberMines());
    }

    @Override
    public void update(Record record) {
        switch (record.getGameType()) {
            case NOVICE -> highScoresWindow.setNoviceRecord(record.getWinnerName(), record.getTimeValue());
            case MEDIUM -> highScoresWindow.setMediumRecord(record.getWinnerName(), record.getTimeValue());
            case EXPERT -> highScoresWindow.setExpertRecord(record.getWinnerName(), record.getTimeValue());
        }
    }

    @Override
    public void addObserver(ViewObserver observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObservers(ViewAction action) {
        observers.forEach(o -> o.update(action));
    }
}
