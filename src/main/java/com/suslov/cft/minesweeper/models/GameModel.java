package com.suslov.cft.minesweeper.models;

import com.suslov.cft.minesweeper.events.game.GameAction;
import com.suslov.cft.minesweeper.events.game.GameAction.Content;
import com.suslov.cft.minesweeper.events.game.GameActionType;
import com.suslov.cft.minesweeper.exceptions.CoordinateException;
import com.suslov.cft.minesweeper.frames.enums.GameType;
import com.suslov.cft.minesweeper.highscores.Record;
import com.suslov.cft.minesweeper.highscores.Highscore;
import com.suslov.cft.minesweeper.observers.GameObservable;
import com.suslov.cft.minesweeper.observers.GameObserver;
import com.suslov.cft.minesweeper.util.CellsUtil;

import java.util.*;
import java.util.logging.Logger;

public class GameModel implements ModelInterface, GameObservable {
    public static final Logger LOG = Logger.getLogger(GameModel.class.getName());

    private final List<GameObserver> observers = new ArrayList<>();
    private final Map<GameType, FieldSettings> settingsMap = new EnumMap<>(GameType.class);

    private GameType gameType;
    private Timer timer;
    private int timerValue;
    private Cell[][] field;
    private FieldSettings fieldSettings;
    private Highscore records;
    private int width;
    private int height;
    private int numberMines;
    private int notMarkedMines;
    private int numberMarkedFlags;
    private boolean firstStep;
    private boolean gameOver;

    {
        settingsMap.put(GameType.NOVICE, new FieldSettings(9, 9, 10));
        settingsMap.put(GameType.MEDIUM, new FieldSettings(16, 16, 40));
        settingsMap.put(GameType.EXPERT, new FieldSettings(30, 16, 99));
    }

    public GameModel(FieldSettings fieldSettings, Highscore records) {
        initializeParameters(fieldSettings, records, GameType.NOVICE);
    }

    @Override
    public FieldSettings getFieldSettings() {
        return fieldSettings;
    }

    @Override
    public void startGame() {
        startGameWithParameters(settingsMap.get(gameType), records, gameType);
    }

    @Override
    public void restartGame(GameType type) {
        if (!settingsMap.containsKey(type)) {
            LOG.warning("Unexpected game type '" + type + "' passed to restart parameter.\n " +
                    "Novice game type will be passed as default");
            startGameWithParameters(settingsMap.get(GameType.NOVICE), records, GameType.NOVICE);
        } else {
            startGameWithParameters(settingsMap.get(type), records, type);
        }
    }

    private void startGameWithParameters(FieldSettings settings, Highscore records, GameType type) {
        LOG.info(">>> The minesweeper game is started:\n" +
                "Level: " + gameType + "\n" +
                "Field: " + width + " x " + height + "\n" +
                "Number of mines: " + numberMines + "\n" +
                "Timer value: " + timerValue + "\n");
        firstStep = true;

        initializeParameters(settings, records, type);
        fillGameField();
        sendStartNotificationToObservers();
        sendCounterNotificationToObservers(GameActionType.TIMER, timerValue);
    }

    private void initializeParameters(FieldSettings settings, Highscore records, GameType type) {
        initializeField(settings);
        initializeGameProperties(type, records);
    }

    private void initializeField(FieldSettings settings) {
        this.fieldSettings = settings;
        this.width = settings.width();
        this.height = settings.height();
        this.numberMines = settings.numberMines();
        this.field = new Cell[height][width];
        this.notMarkedMines = numberMines;
        this.numberMarkedFlags = 0;
    }

    private void initializeGameProperties(GameType type, Highscore records) {
        this.firstStep = true;
        this.gameOver = false;
        this.gameType = type;
        this.records = records;
        this.timerValue = 0;
    }

    private void fillGameField() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                field[y][x] = new Cell(x, y);
            }
        }
    }

    private void sendStartNotificationToObservers() {
        GameAction restartAction = new GameAction(GameActionType.RESTART, new Content(fieldSettings));
        notifyObservers(restartAction);
    }

    @Override
    public void openCell(int x, int y) throws CoordinateException {
        // If the cell is already opened, don't anything
        Cell cell = getCell(x, y);
        if (cell.getState() != Cell.State.CLOSED) {
            return;
        }

        handleCellOpening(cell);

        if (checkVictoryConditions(cell)) {
            return;
        }

        checkNeighbourCellsRecursively(cell);
    }

    private void handleCellOpening(Cell cell) {
        cell.open();
        if (firstStep) {
            firstStep = false;
            initializeMineField(cell);
            startTimer();
        }
        sendCellNotificationToObservers(GameActionType.CELL, cell);
    }

    private boolean checkVictoryConditions(Cell cell) {
        if (cell.isMine()) {
            gameOver = true;
            LOG.info("<<< The game is ended. Player lost!\n");
            sendCellNotificationToObservers(GameActionType.LOSE, cell);
            return true;
        }
        if (isWin()) {
            LOG.info("<<< The game is ended. Player won!\n");
            Record record = new Record(gameType, timerValue);
            sendWinNotificationToObservers(records.isBestResult(record));
            return true;
        }
        return false;
    }

    private void checkNeighbourCellsRecursively(Cell cell) {
        if (cell.getCountMinesAround() == 0) {
            getCellsAround(cell).stream()
                    .filter(c -> c.getState() == Cell.State.CLOSED)
                    .forEach(c -> openCell(c.getX(), c.getY()));
        }
    }

    private void initializeMineField(Cell excludeCell) {
        placeMines(excludeCell);
        countMinesAroundCells();
    }

    private void startTimer() {
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                sendCounterNotificationToObservers(GameActionType.TIMER, timerValue);
                timerValue++;
                if (isGameOver() || isWin()) {
                    timer.cancel();
                    timer = null;
                }
            }
        }, 0, 1000);
    }

    private boolean isGameOver() {
        return gameOver;
    }

    private boolean isWin() {
        return numberMines == numberMarkedFlags;
    }

    private void sendWinNotificationToObservers(boolean bestResult) {
        GameAction action = new GameAction(GameActionType.WIN, new Content(bestResult));
        notifyObservers(action);
    }

    private void placeMines(Cell excludeCell) {
        for (int i = 0; i < numberMines; i++) {
            placeMine(excludeCell);
        }
    }

    private void countMinesAroundCells() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Cell cell = field[y][x];
                if (!cell.isMine()) {
                    cell.setCountMinesAround((int) getCellsAround(cell).stream()
                            .filter(Cell::isMine)
                            .count());
                }
            }
        }
    }

    private void placeMine(Cell excludeCell) {
        while (true) {
            Cell cell;
            try {
                cell = getRandomCell();
            } catch (CoordinateException exp) {
                LOG.warning("Error on the placing mines on the field:\n" + exp.getMessage());
                continue;
            }
            if (cell.equals(excludeCell) || cell.isMine()) {
                continue;
            }
            cell.setMine(true);
            break;
        }
    }

    private Cell getRandomCell() {
        Random random = new Random();
        return getCell(random.nextInt(height), random.nextInt(width));
    }

    @Override
    public void changeCellState(int x, int y) throws CoordinateException {
        Cell cell = getCell(x, y);
        if (cell.nextState()) {
            sendCellNotificationToObservers(GameActionType.CELL, cell);
        }
        switch (cell.getState()) {
            case MARKED -> {
                sendCounterNotificationToObservers(GameActionType.MINE, --notMarkedMines);
                numberMarkedFlags++;
            }
            case CLOSED -> {
                sendCounterNotificationToObservers(GameActionType.MINE, ++notMarkedMines);
                numberMarkedFlags--;
            }
        }
    }

    private void sendCellNotificationToObservers(GameActionType type, Cell cell) {
        GameAction action = new GameAction(type, new Content(CellsUtil.createTo(cell)));
        notifyObservers(action);
    }

    private void sendCounterNotificationToObservers(GameActionType type, int counter) {
        GameAction action = new GameAction(type, new Content(type, counter));
        notifyObservers(action);
    }

    @Override
    public void openCellsAround(int x, int y) throws CoordinateException {
        Cell cell = getCell(x, y);
        boolean cellIsOpened = cell.getState() == Cell.State.OPENED;
        boolean allMinesAroundIsMarked = cell.getCountMinesAround() == countFlagsAround(cell);
        if (cellIsOpened && allMinesAroundIsMarked) {
            getCellsAround(cell).stream()
                    .filter(c -> c.getState() == Cell.State.CLOSED)
                    .forEach(c -> openCell(c.getX(), c.getY()));
        }
    }

    private Cell getCell(int x, int y) throws CoordinateException {
        if ((y < 0 || y >= height) || (x < 0 || x >= width)) {
            throw new CoordinateException("Invalid coordinates for cell: (" + x + ", " + y + ")");
        }
        return field[y][x];
    }

    private int countFlagsAround(Cell cell) {
        return (int) getCellsAround(cell).stream()
                .filter(c -> c.getState() == Cell.State.MARKED)
                .count();
    }

    private List<Cell> getCellsAround(Cell cell) {
        List<Cell> result = new ArrayList<>();
        for (int y = cell.getY() - 1; y <= cell.getY() + 1; y++) {
            for (int x = cell.getX() - 1; x <= cell.getX() + 1; x++) {
                if (y < 0 || y >= height || x < 0 || x >= width || field[y][x].equals(cell)) {
                    continue;
                }
                result.add(field[y][x]);
            }
        }
        return result;
    }

    @Override
    public GameType getGameType() {
        return gameType;
    }

    @Override
    public int receiveTimeRemainder() {
        return timerValue;
    }

    @Override
    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObservers(GameAction action) {
        observers.forEach(o -> o.update(action));
    }
}
