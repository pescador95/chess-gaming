package com.pescador95.chess.pieces;

import com.pescador95.boardGame.Board;
import com.pescador95.boardGame.Position;
import com.pescador95.chess.ChessPiece;
import com.pescador95.chess.Color;
import com.pescador95.chess.PieceSymbol;

public class Bishop extends ChessPiece {

    public Bishop(Board board, Color color) {
        super(board, color);
        symbol = toString();
    }

    @Override
    public String toString() {
        return PieceSymbol.BISHOP.getSymbol();
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];

        checkDirection(mat, NORTH_EAST);
        checkDirection(mat, SOUTH_EAST);
        checkDirection(mat, NORTH_WEST);
        checkDirection(mat, SOUTH_WEST);
        return mat;
    }

    private void checkDirection(boolean[][] mat, int[] direction) {

        Position p = new Position(position.getRow(), position.getColumn());
        p.setValues(p.getRow() + direction[0], p.getColumn() + direction[1]);

        while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {

            mat[p.getRow()][p.getColumn()] = true;
            p.setValues(p.getRow() + direction[0], p.getColumn() + direction[1]);

        }
        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }
    }
}
