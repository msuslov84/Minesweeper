package com.suslov.cft.minesweeper.frames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public abstract class ResultWindow extends JDialog {
    protected ActionListener newGameListener;
    protected ActionListener exitListener;

    protected abstract JLabel getLabelWithText();

    public ResultWindow(JFrame owner, String title) {
        super(owner, title, true);

        GridBagLayout layout = new GridBagLayout();
        Container contentPane = getContentPane();
        contentPane.setLayout(layout);

        contentPane.add(createLoseLabel(layout));
        contentPane.add(createNewGameButton(layout));
        contentPane.add(createExitButton(layout));

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(300, 130));
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(false);
    }

    public void setNewGameListener(ActionListener newGameListener) {
        this.newGameListener = newGameListener;
    }

    public void setExitListener(ActionListener exitListener) {
        this.exitListener = exitListener;
    }

    private JLabel createLoseLabel(GridBagLayout layout) {
        JLabel label = getLabelWithText();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        layout.setConstraints(label, gbc);
        return label;
    }

    private JButton createNewGameButton(GridBagLayout layout) {
        JButton newGameButton = createNewButton("New game", newGameListener);
        layout.setConstraints(newGameButton, createGridBagConstraints(GridBagConstraints.EAST, 0));

        return newGameButton;
    }

    private JButton createExitButton(GridBagLayout layout) {
        JButton exitButton = createNewButton("Exit", exitListener);
        layout.setConstraints(exitButton, createGridBagConstraints(GridBagConstraints.WEST, 5));

        return exitButton;
    }

    private JButton createNewButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(100, 25));

        button.addActionListener(e -> {
            dispose();
            if (listener != null) {
                listener.actionPerformed(e);
            }
        });

        return button;
    }

    private GridBagConstraints createGridBagConstraints(int gbcAnchor, int leftInset) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = gbcAnchor;
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.5;
        gbc.insets = new Insets(15, leftInset, 0, 0);

        return gbc;
    }
}
