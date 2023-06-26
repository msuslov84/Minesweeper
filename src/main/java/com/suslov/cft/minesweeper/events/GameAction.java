package com.suslov.cft.minesweeper.events;

import com.suslov.cft.minesweeper.frames.enums.GameType;
import com.suslov.cft.minesweeper.models.FieldSettings;
import com.suslov.cft.minesweeper.to.CellTo;

public final class GameAction {
    private final Content content;
    private final GameActionType type;

    public GameAction(GameActionType type, Content content) {
        this.content = content;
        this.type = type;
    }

    public Content getContent() {
        return content;
    }

    public GameActionType type() {
        return type;
    }

    public static class Content {
        private CellTo cellTo;
        private FieldSettings settings;
        private int timerValue;
        private int remainderMines;
        private GameType gameType;
        private boolean bestResult;

        public Content(GameType gameType, boolean bestResult) {
            this.gameType = gameType;
            this.bestResult = bestResult;
        }

        public Content(CellTo cellTo) {
            this.cellTo = cellTo;
        }

        public Content(FieldSettings settings) {
            this.settings = settings;
        }

        public Content(GameActionType type, int counter) {
            switch (type) {
                case TIMER -> this.timerValue = counter;
                case MINE -> this.remainderMines = counter;
            }
        }

        public CellTo getCellTo() {
            return cellTo;
        }

        public FieldSettings getSettings() {
            return settings;
        }

        public int getTimerValue() {
            return timerValue;
        }

        public int getRemainderMines() {
            return remainderMines;
        }

        public GameType getGameType() {
            return gameType;
        }

        public boolean isBestResult() {
            return bestResult;
        }
    }
}
