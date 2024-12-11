package com.pescador95.chess.pieces;

import com.pescador95.boardGame.Board;
import com.pescador95.boardGame.Position;
import com.pescador95.chess.ChessMatch;
import com.pescador95.chess.ChessPiece;
import com.pescador95.chess.Color;
import com.pescador95.chess.PieceSymbol;

public class King extends ChessPiece {

    public static final int[] KINGSIDE_ROOK = {3, 1, 2};
    public static final int[] QUEENSIDE_ROOK = {-4, -1, -2, -3};

    private final ChessMatch chessMatch;

    public King(Board board, Color color, ChessMatch chessMatch) {
        super(board, color);
        this.chessMatch = chessMatch;
        symbol = toString();
    }

    @Override
    public String toString() {
        return PieceSymbol.KING.getSymbol();
    }

    private boolean canMove(Position position) {
        ChessPiece p = (ChessPiece) getBoard().piece(position);
        return p == null || p.getColor() != getColor();
    }

    private boolean testRookCastling(Position position) {
        ChessPiece p = (ChessPiece) getBoard().piece(position);
        return p != null & p instanceof Rook && p.getColor() == getColor() && p.getMoveCount() == 0;
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];

        checkDirection(mat, NORTH);
        checkDirection(mat, SOUTH);
        checkDirection(mat, WEST);
        checkDirection(mat, EAST);
        checkDirection(mat, NORTH_WEST);
        checkDirection(mat, NORTH_EAST);
        checkDirection(mat, SOUTH_WEST);
        checkDirection(mat, SOUTH_EAST);
        checkSpecialMoveCastling(mat, KINGSIDE_ROOK);
        checkSpecialMoveCastling(mat, QUEENSIDE_ROOK);
        return mat;
    }

    private void checkDirection(boolean[][] mat, int[] direction) {

        Position p = new Position(position.getRow(), position.getColumn());
        p.setValues(p.getRow() + direction[0], p.getColumn() + direction[1]);

        if (getBoard().positionExists(p) && canMove(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }
    }

    private void checkSpecialMoveCastling(boolean[][] mat, int[] direction) {
        if (getMoveCount() == 0 && !chessMatch.getCheck()) {
            Position posT = new Position(position.getRow(), position.getColumn() + direction[0]);
            if (testRookCastling(posT)) {
                if (areCastlingSquaresEmpty(direction)) {
                    mat[position.getRow()][position.getColumn() + Math.abs(direction[2])] = true;
                }
            }
        }
    }


    private boolean areCastlingSquaresEmpty(int[] direction) {
        for (int i = 1; i < direction.length; i++) {
            Position p = new Position(position.getRow(), position.getColumn() + direction[i]);
            if (getBoard().piece(p) != null) {
                return false;
            }
        }
        return true;
    }
}
