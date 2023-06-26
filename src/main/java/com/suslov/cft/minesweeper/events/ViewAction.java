package com.suslov.cft.minesweeper.events;

import com.suslov.cft.minesweeper.frames.enums.ButtonType;
import com.suslov.cft.minesweeper.frames.enums.GameType;

import java.awt.event.ActionEvent;

public final class ViewAction {
    private final ViewActionType type;
    private final Content content;

    public ViewAction(ViewActionType type, Content content) {
        this.type = type;
        this.content = content;
    }

    public ViewActionType type() {
        return type;
    }

    public Content getContent() {
        return content;
    }

    public static class Content {
        private int x;
        private int y;
        private ButtonType buttonType;
        private GameType gameType;
        private String winnerName;
        private ActionEvent event;

        public Content(int x, int y, ButtonType buttonType) {
            this.x = x;
            this.y = y;
            this.buttonType = buttonType;
        }

        public Content(String winnerName) {
            this.winnerName = winnerName;
        }

        public Content(GameType gameType) {
            this.gameType = gameType;
        }

        public Content(ActionEvent event) {
            this.event = event;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public ButtonType getButtonType() {
            return buttonType;
        }

        public String getWinnerName() {
            return winnerName;
        }

        public GameType getGameType() {
            return gameType;
        }

        public ActionEvent getEvent() {
            return event;
        }
    }
}
