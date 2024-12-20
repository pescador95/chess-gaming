package com.pescador95.chess;

import com.pescador95.boardGame.Board;
import com.pescador95.boardGame.Piece;
import com.pescador95.boardGame.Position;
import com.pescador95.chess.pieces.Pawn;

public abstract class ChessPiece extends Piece {

    public static final int[] EAST = {0, 1};
    public static final int[] WEST = {0, -1};
    public static final int[] NORTH = {-1, 0};
    public static final int[] SOUTH = {1, 0};
    public static final int[] NORTH_EAST = {-1, 1};
    public static final int[] NORTH_WEST = {-1, -1};
    public static final int[] SOUTH_EAST = {1, 1};
    public static final int[] SOUTH_WEST = {1, -1};


    private final Color color;
    protected String symbol;
    private int moveCount;

    public ChessPiece(Board board, Color color) {
        super(board);
        this.color = color;
    }

    public String getSymbol() {
        return symbol;
    }

    public Color getColor() {
        return color;
    }

    public int getMoveCount() {
        return moveCount;
    }

    public void increaseMoveCount() {
        moveCount++;
    }

    public void decreaseMoveCount() {
        moveCount--;
    }

    public ChessPosition getChessPosition() {
        return ChessPosition.fromPosition(position);
    }

    protected boolean isThereOpponentPiece(Position position) {
        ChessPiece p = (ChessPiece) getBoard().piece(position);
        return p != null && p.getColor() != color;
    }


    protected boolean canPromote(Position target) {
        return this instanceof Pawn && (this.getColor() == Color.WHITE && target.getRow() == 0 || this.getColor() == Color.BLACK && target.getRow() == 7);
    }

    protected boolean canEnPassantMove(Position source, Position target) {
        return this instanceof Pawn && (target.getRow() == source.getRow() + 2 || target.getRow() == source.getRow() - 2);
    }
}
