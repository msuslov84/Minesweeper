package com.suslov.cft.minesweeper;

import com.suslov.cft.minesweeper.controllers.Controller;
import com.suslov.cft.minesweeper.controllers.GameController;
import com.suslov.cft.minesweeper.exceptions.GameException;
import com.suslov.cft.minesweeper.highscores.RecordManager;
import com.suslov.cft.minesweeper.models.FieldSettings;
import com.suslov.cft.minesweeper.models.GameModel;
import com.suslov.cft.minesweeper.views.GameView;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class MineSweeper {
    public static final Logger LOG = Logger.getLogger(MineSweeper.class.getName());

    static int defaultHeight = 9;
    static int defaultWidth = 9;
    static int defaultNumberMines = 10;
    static String defaultRecordFile = "record.txt";

    public static void main(String[] args) {
        try {
            initializeLogger();
            start();
        } catch (IOException exp) {
            System.err.println("Could not setup logger configuration: " + exp);
        } catch (GameException exp) {
            LOG.warning("The game is crashed:\n" + exp.getMessage());
        }
    }

    public static void initializeLogger() throws IOException {
        LogManager.getLogManager().readConfiguration(MineSweeper.class.getResourceAsStream("/logging.properties"));
    }

    public static void start() {
        RecordManager recordManager = new RecordManager(Paths.get(defaultRecordFile));
        GameModel game = new GameModel(new FieldSettings(defaultWidth, defaultHeight, defaultNumberMines), recordManager);
        GameView view = new GameView(game.getFieldSettings(), game, recordManager);
        Controller controller = new GameController(game, view, recordManager);
        controller.startGame();
    }
}
