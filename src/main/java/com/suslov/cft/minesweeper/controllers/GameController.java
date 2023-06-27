package com.suslov.cft.minesweeper.controllers;

import com.suslov.cft.minesweeper.events.view.ViewAction;
import com.suslov.cft.minesweeper.events.view.ViewAction.Content;
import com.suslov.cft.minesweeper.exceptions.CoordinateException;
import com.suslov.cft.minesweeper.exceptions.HighscoreException;
import com.suslov.cft.minesweeper.highscores.Record;
import com.suslov.cft.minesweeper.highscores.Highscore;
import com.suslov.cft.minesweeper.models.ModelInterface;
import com.suslov.cft.minesweeper.observers.ViewObservable;
import com.suslov.cft.minesweeper.observers.ViewObserver;

import java.awt.event.ActionEvent;
import java.util.logging.Logger;

public class GameController implements Controller, ViewObserver {
    public static final Logger LOG = Logger.getLogger(GameController.class.getName());

    private final ModelInterface game;
    private final Highscore records;

    public GameController(ModelInterface game, ViewObservable view, Highscore records) {
        this.game = game;
        this.records = records;
        view.addObserver(this);
    }

    @Override
    public void update(ViewAction action) {
        switch (action.type()) {
            case CLICK -> onMouseClick(action.getContent());
            case SETTINGS -> onGameTypeChanged(action.getContent());
            case NEW_GAME -> actionPerformed(action.getContent());
            case RECORD -> onRecordNameEntered(action.getContent());
        }
    }

    @Override
    public void onMouseClick(Content content) {
        switch (content.getButtonType()) {
            case LEFT_BUTTON -> onLeftMouseClick(content.getX(), content.getY());
            case MIDDLE_BUTTON -> onMiddleMouseClick(content.getX(), content.getY());
            case RIGHT_BUTTON -> onRightMouseClick(content.getX(), content.getY());
        }
    }

    @Override
    public void onGameTypeChanged(Content content) {
        game.restartGame(content.getGameType());
    }

    @Override
    public void actionPerformed(Content content) {
        ActionEvent event = content.getEvent();
        if (event.getActionCommand().equalsIgnoreCase("NEW GAME")) {
            startGame();
        }
    }

    @Override
    public void onRecordNameEntered(Content content) {
        String winnerName = content.getWinnerName();
        try {
            records.write(new Record(game.getGameType(), winnerName, game.receiveTimeRemainder()));
        } catch (HighscoreException exp) {
            LOG.warning("Error on record name entered:\n" + exp.getMessage());
        }

    }

    private void onLeftMouseClick(int x, int y) {
        try {
            game.openCell(x, y);
        } catch (CoordinateException exp) {
            LOG.warning("Error on the left mouse click:\n" + exp.getMessage());
        }
    }

    private void onMiddleMouseClick(int x, int y) {
        try {
            game.openCellsAround(x, y);
        } catch (CoordinateException exp) {
            LOG.warning("Error on the middle mouse click:\n" + exp.getMessage());
        }
    }

    private void onRightMouseClick(int x, int y) {
        try {
            game.changeCellState(x, y);
        } catch (CoordinateException exp) {
            LOG.warning("Error on the right mouse click:\n" + exp.getMessage());
        }
    }

    @Override
    public void startGame() {
        try {
            records.read();
        } catch (HighscoreException exp) {
            LOG.warning("Error on read highscores:\n" + exp.getMessage());
        }
        game.startGame();
    }
}