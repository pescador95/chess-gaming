package com.pescador95.application.DTO.ChessDTO;

import com.pescador95.chess.ChessMatch;
import com.pescador95.chess.Color;

public class ChessDTO {

    private char sourceRow;
    private char targetRow;
    private int sourceColumn;
    private int targetColumn;

    public ChessDTO() {
    }

    public ChessDTO(char sourceRow, int sourceColumn, char targetRow, int targetColumn) {
        this.sourceRow = sourceRow;
        this.sourceColumn = sourceColumn;
        this.targetColumn = targetColumn;
        this.targetRow = targetRow;
    }

    public char getSourceRow() {
        return Character.toLowerCase(sourceRow);
    }

    public char getTargetRow() {
        return Character.toLowerCase(targetRow);
    }

    public int getSourceColumn() {
        return sourceColumn;
    }

    public int getTargetColumn() {
        return targetColumn;
    }
}
