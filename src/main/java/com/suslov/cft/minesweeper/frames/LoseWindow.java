package com.suslov.cft.minesweeper.frames;

import javax.swing.*;

public class LoseWindow extends ResultWindow {

    public LoseWindow(JFrame owner) {
        super(owner, "Lose");
    }

    @Override
    protected JLabel getLabelWithText() {
        return new JLabel("You lose!");
    }
}
