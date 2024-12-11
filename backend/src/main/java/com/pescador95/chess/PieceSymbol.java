package com.pescador95.chess;

public enum PieceSymbol {
    BISHOP("B"),
    KING("K"),
    QUEEN("Q"),
    ROOK("R"),
    KNIGHT("N"),
    PAWN("P");

    private final String symbol;

    PieceSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}