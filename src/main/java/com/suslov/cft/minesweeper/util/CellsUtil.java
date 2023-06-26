package com.suslov.cft.minesweeper.util;

import com.suslov.cft.minesweeper.exceptions.CellPropertyException;
import com.suslov.cft.minesweeper.frames.GameImage;
import com.suslov.cft.minesweeper.models.Cell;
import com.suslov.cft.minesweeper.to.CellTo;

public class CellsUtil {

    public static CellTo createTo(Cell cell) {
        GameImage image;
        switch (cell.getState()) {
            case CLOSED -> image = GameImage.CLOSED;
            case MARKED -> image = GameImage.MARKED;
            case OPENED -> image = inspectOpenedCell(cell);
            default -> throw new CellPropertyException("Unsupported state '" + cell.getState() + "' " +
                    "of the cell (" + cell.getX() + ", " + cell.getY() + ")");
        }
        return new CellTo(cell.getX(), cell.getY(), image);
    }

    private static GameImage inspectOpenedCell(Cell cell) {
        if (cell.isMine()) {
            return GameImage.BOMB;
        } else if (cell.getCountMinesAround() == 0) {
            return GameImage.EMPTY;
        } else {
            GameImage image;
            switch (cell.getCountMinesAround()) {
                case 1 -> image = GameImage.NUM_1;
                case 2 -> image = GameImage.NUM_2;
                case 3 -> image = GameImage.NUM_3;
                case 4 -> image = GameImage.NUM_4;
                case 5 -> image = GameImage.NUM_5;
                case 6 -> image = GameImage.NUM_6;
                case 7 -> image = GameImage.NUM_7;
                case 8 -> image = GameImage.NUM_8;
                default -> throw new CellPropertyException("Invalid calculate number mines (" + cell.getCountMinesAround() + ")" +
                        " around the cell (" + cell.getX() + ", " + cell.getY() + ")");
            }
            return image;
        }
    }
}
