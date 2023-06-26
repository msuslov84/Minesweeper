package com.suslov.cft.minesweeper.highscores;

import com.suslov.cft.minesweeper.exceptions.HighscoreException;
import com.suslov.cft.minesweeper.frames.enums.GameType;
import com.suslov.cft.minesweeper.observers.RecordObservable;
import com.suslov.cft.minesweeper.observers.RecordObserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class RecordManager implements Highscore, RecordObservable {
    public static final Logger LOG = Logger.getLogger(RecordManager.class.getName());
    private static final String SEPARATOR = "-";

    private final List<RecordObserver> observers = new ArrayList<>();
    private final Path filePath;
    private final Map<GameType, Record> recordTable = new EnumMap<>(GameType.class);

    public RecordManager(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public boolean isBestResult(Record record) {
        Record currentRecord = recordTable.get(record.getGameType());
        // The less time, the better the result
        return currentRecord == null || record.compareTo(currentRecord) < 0;
    }

    @Override
    public void read() throws HighscoreException {
        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            reader.lines().forEach(l -> {
                String[] recordValues = l.split(SEPARATOR);
                try {
                    GameType gameType = GameType.valueOf(recordValues[0]);
                    Record record = new Record(gameType, recordValues[1], Integer.parseInt(recordValues[2]));
                    recordTable.put(gameType, record);
                    notifyObservers(record);
                } catch (IllegalArgumentException exp) {
                    throw new HighscoreException("Error read record line '" + l + "' from file", exp);
                }
            });
        } catch (IOException exp) {
            throw new HighscoreException("Error read file '" + filePath + "'", exp);
        }
    }

    @Override
    public void write(Record record) throws HighscoreException {
        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
            writer.write(record.getGameType() + SEPARATOR + record.getWinnerName() + SEPARATOR + record.getTimeValue());
            notifyObservers(record);
        } catch (IOException exp) {
            throw new HighscoreException("Error write file '" + filePath + "'", exp);
        }
    }

    @Override
    public void addObserver(RecordObserver observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObservers(Record record) {
        observers.forEach(o -> o.update(record));
    }
}