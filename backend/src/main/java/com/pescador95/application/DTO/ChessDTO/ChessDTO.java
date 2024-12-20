package com.pescador95.application.DTO.ChessDTO;

public class ChessDTO {

    private int sourceRow;
    private char sourceColumn;
    private int targetRow;
    private char targetColumn;

    public ChessDTO() {
    }

    public ChessDTO(int sourceRow, char sourceColumn, int targetRow, char targetColumn) {
        this.sourceRow = sourceRow;
        this.sourceColumn = sourceColumn;
        this.targetColumn = targetColumn;
        this.targetRow = targetRow;
    }

    public int getSourceRow() {
        return sourceRow;
    }

    public int getTargetRow() {
        return targetRow;
    }

    public char getSourceColumn() {
        return Character.toLowerCase(sourceColumn);
    }

    public char getTargetColumn() {
        return Character.toLowerCase(targetColumn);
    }
}
