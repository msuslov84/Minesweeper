package com.suslov.cft.minesweeper.events.game;

import com.suslov.cft.minesweeper.models.FieldSettings;
import com.suslov.cft.minesweeper.to.CellTo;

public final class GameAction {

    private final GameActionType type;
    private final Content content;

    public GameAction(GameActionType type, Content content) {
        this.type = type;
        this.content = content;
    }

    public GameActionType type() {
        return type;
    }

    public Content getContent() {
        return content;
    }

    public static class Content {
        private CellTo cellTo;

        private FieldSettings settings;

        private int timerValue;
        private int remainderMines;

        private boolean bestResult;

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

        public Content(boolean bestResult) {
            this.bestResult = bestResult;
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

        public boolean isBestResult() {
            return bestResult;
        }
    }
}
