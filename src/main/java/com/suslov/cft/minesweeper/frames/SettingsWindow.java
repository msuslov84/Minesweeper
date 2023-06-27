package com.suslov.cft.minesweeper.frames;

import com.suslov.cft.minesweeper.exceptions.GameException;
import com.suslov.cft.minesweeper.frames.enums.GameType;
import com.suslov.cft.minesweeper.frames.listeners.GameTypeListener;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class SettingsWindow extends JDialog {
    private final Map<GameType, JRadioButton> radioButtonsMap = new HashMap<>(3);
    private final ButtonGroup radioGroup = new ButtonGroup();

    private GameTypeListener gameTypeListener;
    private GameType gameType;

    public SettingsWindow(JFrame owner) {
        super(owner, "Settings", true);

        GridBagLayout layout = new GridBagLayout();
        Container contentPane = getContentPane();
        contentPane.setLayout(layout);

        int gridY = -1;
        contentPane.add(createRadioButton("Novice (10 mines, 9х9)", GameType.NOVICE, layout, ++gridY));
        contentPane.add(createRadioButton("Medium (40 mines, 16х16)", GameType.MEDIUM, layout, ++gridY));
        contentPane.add(createRadioButton("Expert (99 mines, 16х30)", GameType.EXPERT, layout, ++gridY));

        contentPane.add(createOkButton(layout));
        contentPane.add(createCloseButton(layout));

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(300, 180));
        setResizable(false);
        pack();
        setLocationRelativeTo(null);

        setGameType(GameType.NOVICE);
    }

    public void setGameType(GameType gameType) {
        JRadioButton radioButton = radioButtonsMap.get(gameType);
        if (radioButton == null) {
            throw new GameException("Unknown game type installed: " + gameType);
        }
        radioGroup.setSelected(radioButton.getModel(), true);

        this.gameType = gameType;
    }

    public void setGameTypeListener(GameTypeListener gameTypeListener) {
        this.gameTypeListener = gameTypeListener;
    }

    private JRadioButton createRadioButton(String radioButtonText, GameType gameType, GridBagLayout layout, int gridY) {
        JRadioButton radioButton = new JRadioButton(radioButtonText);
        radioButton.addActionListener(e -> this.gameType = gameType);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy = gridY;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridheight = 1;
        gbc.insets = new Insets(0, 20, 0, 0);
        layout.setConstraints(radioButton, gbc);

        radioButtonsMap.put(gameType, radioButton);
        radioGroup.add(radioButton);

        return radioButton;
    }

    private JButton createOkButton(GridBagLayout layout) {
        JButton okButton = new JButton("OK");
        okButton.setPreferredSize(new Dimension(80, 25));
        okButton.addActionListener(e -> {
            dispose();
            if (gameTypeListener != null) {
                gameTypeListener.onGameTypeChanged(gameType);
            }
        });

        layout.setConstraints(okButton, createGridBagConstraints(GridBagConstraints.EAST, 1, 0));

        return okButton;
    }

    private JButton createCloseButton(GridBagLayout layout) {
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(80, 25));
        cancelButton.addActionListener(e -> dispose());

        layout.setConstraints(cancelButton, createGridBagConstraints(GridBagConstraints.WEST, 2, 5));

        return cancelButton;
    }

    private GridBagConstraints createGridBagConstraints(int gbcAnchor, int gridX, int leftInset) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = gbcAnchor;
        gbc.gridx = gridX;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.5;
        gbc.insets = new Insets(15, leftInset, 0, 0);

        return gbc;
    }
}
