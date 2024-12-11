package com.pescador95.chess.pieces;

import com.pescador95.boardGame.Board;
import com.pescador95.boardGame.Position;
import com.pescador95.chess.ChessPiece;
import com.pescador95.chess.Color;
import com.pescador95.chess.PieceSymbol;

public class Knight extends ChessPiece {

    public static final int[] NORTH_NORTH_WEST = {-2, -1};
    public static final int[] NORTH_NORTH_EAST = {-2, 1};
    public static final int[] NORTH_WEST_WEST = {-1, -2};
    public static final int[] NORTH_EAST_EAST = {-1, 2};
    public static final int[] SOUTH_SOUTH_WEST = {2, -1};
    public static final int[] SOUTH_SOUTH_EAST = {2, 1};
    public static final int[] SOUTH_WEST_WEST = {1, -2};
    public static final int[] SOUTH_EAST_EAST = {1, 2};

    public Knight(Board board, Color color) {
        super(board, color);
        symbol = toString();
    }

    @Override
    public String toString() {
        return PieceSymbol.KNIGHT.getSymbol();
    }

    private boolean canMove(Position position) {
        ChessPiece p = (ChessPiece) getBoard().piece(position);
        return p == null || p.getColor() != getColor();
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];

        checkDirection(mat, NORTH_NORTH_WEST);
        checkDirection(mat, NORTH_NORTH_EAST);
        checkDirection(mat, NORTH_WEST_WEST);
        checkDirection(mat, NORTH_EAST_EAST);
        checkDirection(mat, SOUTH_SOUTH_WEST);
        checkDirection(mat, SOUTH_SOUTH_EAST);
        checkDirection(mat, SOUTH_WEST_WEST);
        checkDirection(mat, SOUTH_EAST_EAST);
        return mat;
    }

    private void checkDirection(boolean[][] mat, int[] direction) {

        Position p = new Position(position.getRow(), position.getColumn());
        p.setValues(p.getRow() + direction[0], p.getColumn() + direction[1]);

        if (getBoard().positionExists(p) && canMove(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }
    }
}
