package com.pescador95.chess.pieces;

import com.pescador95.boardGame.Board;
import com.pescador95.boardGame.Position;
import com.pescador95.chess.ChessMatch;
import com.pescador95.chess.ChessPiece;
import com.pescador95.chess.Color;
import com.pescador95.chess.PieceSymbol;

public class Pawn extends ChessPiece {

    public static final int[] NORTH_TWO = {-2, 0};
    public static final int[] SOUTH_TWO = {2, 0};
    public static final int[] ENPASSANT_WHITE = {3, -1, 1};
    public static final int[] ENPASSANT_BLACK = {4, 1, -1};

    private final ChessMatch chessMatch;

    public Pawn(Board board, Color color, ChessMatch chessMatch) {
        super(board, color);
        this.chessMatch = chessMatch;
        symbol = toString();
    }

    @Override
    public boolean[][] possibleMoves() {

        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];

        if (getColor() == Color.WHITE) {
            checkDirection(mat, NORTH);
            if (getMoveCount() == 0) {
                checkInitialMove(mat, NORTH_TWO);
            }
            checkCapture(mat, NORTH_WEST);
            checkCapture(mat, NORTH_EAST);
            enPassantMove(mat, ENPASSANT_WHITE);
        }
        if (getColor() == Color.BLACK) {
            checkDirection(mat, SOUTH);
            if (getMoveCount() == 0) {
                checkInitialMove(mat, SOUTH_TWO);
            }
            checkCapture(mat, SOUTH_WEST);
            checkCapture(mat, SOUTH_EAST);
            enPassantMove(mat, ENPASSANT_BLACK);
        }
        return mat;
    }

    private void checkDirection(boolean[][] mat, int[] direction) {
        Position p = new Position(position.getRow(), position.getColumn());
        p.setValues(p.getRow() + direction[0], p.getColumn() + direction[1]);
        if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }
    }

    private void checkInitialMove(boolean[][] mat, int[] direction) {
        Position p = new Position(position.getRow(), position.getColumn());
        p.setValues(p.getRow() + direction[0], p.getColumn() + direction[1]);
        Position p2 = new Position(position.getRow() + direction[0] / 2, position.getColumn());
        if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p) && getBoard().positionExists(p2) && !getBoard().thereIsAPiece(p2)) {
            mat[p.getRow()][p.getColumn()] = true;
        }
    }

    private void checkCapture(boolean[][] mat, int[] direction) {
        Position p = new Position(position.getRow(), position.getColumn());
        p.setValues(p.getRow() + direction[0], p.getColumn() + direction[1]);
        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }
    }

    private void enPassantMove(boolean[][] mat, int[] direction) {
        if (position.getRow() == direction[0]) {
            Position left = new Position(position.getRow(), position.getColumn() + direction[1]);
            if (getBoard().positionExists(left) && isThereOpponentPiece(left) && getBoard().piece(left) == chessMatch.getEnPassantVulnerable()) {
                mat[left.getRow() + direction[1]][left.getColumn()] = true;
            }

            Position right = new Position(position.getRow(), position.getColumn() + direction[2]);
            if (getBoard().positionExists(right) && isThereOpponentPiece(right) && getBoard().piece(right) == chessMatch.getEnPassantVulnerable()) {
                mat[right.getRow() + direction[1]][right.getColumn()] = true;
            }
        }
    }

    @Override
    public String toString() {
        return PieceSymbol.PAWN.getSymbol();
    }
}
