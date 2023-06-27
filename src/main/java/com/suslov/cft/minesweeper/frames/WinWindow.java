package com.suslov.cft.minesweeper.frames;

import javax.swing.*;

public class WinWindow extends ResultWindow {

    public WinWindow(JFrame owner) {
        super(owner, "Win");
    }

    @Override
    protected JLabel getLabelWithText() {
        return new JLabel("You win!");
    }
}
