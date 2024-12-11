package com.pescador95.chess;

import com.pescador95.boardGame.Board;
import com.pescador95.boardGame.Piece;
import com.pescador95.boardGame.Position;
import com.pescador95.chess.pieces.*;

import java.util.*;
import java.util.stream.Collectors;

import static com.pescador95.chess.pieces.King.KINGSIDE_ROOK;
import static com.pescador95.chess.pieces.King.QUEENSIDE_ROOK;
import static com.pescador95.chess.pieces.Pawn.ENPASSANT_BLACK;
import static com.pescador95.chess.pieces.Pawn.ENPASSANT_WHITE;

public class ChessMatch {

    private static final int ROWS = 8;
    private static final int COLUMNS = 8;
    public static final Set<String> PROMOTE_PIECES = new HashSet<>();

    static {
        PROMOTE_PIECES.add(new Bishop(null, null).toString());
        PROMOTE_PIECES.add(new Knight(null, null).toString());
        PROMOTE_PIECES.add(new Rook(null, null).toString());
        PROMOTE_PIECES.add(new Queen(null, null).toString());
    }

    private int turn;
    private Color currentPlayer;
    private final Board board;
    private boolean check;
    private boolean checkMate;
    private ChessPiece enPassantVulnerable;
    private ChessPiece promoted;
    private String gameId;

    private final List<Piece> piecesOnTheBoard = new ArrayList<>();
    private final List<Piece> capturedPieces = new ArrayList<>();

    public ChessMatch() {

        board = new Board(ROWS, COLUMNS);
        turn = 1;
        currentPlayer = Color.WHITE;
        gameId = UUID.randomUUID().toString();
        initialSetup();
    }

    public int getTurn() {
        return turn;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean getCheck() {
        return check;
    }

    public boolean getCheckMate() {
        return checkMate;
    }

    public ChessPiece getEnPassantVulnerable() {
        return enPassantVulnerable;
    }

    public ChessPiece getPromoted() {
        return promoted;
    }

    public ChessPiece[][] getPieces() {

        ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];

        for (int i = 0; i < board.getRows(); i++) {

            for (int j = 0; j < board.getColumns(); j++) {

                mat[i][j] = (ChessPiece) board.piece(i, j);

            }

        }
        return mat;
    }

    public String getGameId() {
        return gameId;
    }

    public boolean[][] possibleMoves(ChessPosition sourcePosition) {
        Position position = sourcePosition.toPosition();
        validateSourcePosition(position);
        return board.piece(position).possibleMoves();
    }

    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();
        validateSourcePosition(source);
        validateTargetPosition(source, target);
        Piece capturedPiece = makeMove(source, target);
        if (testCheck(currentPlayer)) {
            undoMove(source, target, capturedPiece);
            throw new ChessException("You can't put yourself in check");
        }

        ChessPiece movedPiece = (ChessPiece) board.piece(target);

        promoted = null;

        if (movedPiece.canPromote(target)) {
            promoted = (ChessPiece) board.piece(target);
            promoted = replacePromotedPiece("Q");
        }

        check = testCheck(opponent(currentPlayer));

        if (testCheckMate(opponent(currentPlayer))) {
            checkMate = true;
        }
        if (!testCheckMate(opponent(currentPlayer))) {
            nextTurn();
        }
        if (movedPiece.canEnPassantMove(source, target)) {
            enPassantVulnerable = movedPiece;
        }
        return (ChessPiece) capturedPiece;
    }

    private void validateSourcePosition(Position position) {
        if (!board.thereIsAPiece(position)) {
            throw new ChessException("There is no piece on source position");
        }
        if (currentPlayer != ((ChessPiece) board.piece(position)).getColor()) {
            throw new ChessException("The chosen piece is not yours");
        }
        if (!board.piece(position).isThereAnyPossibleMove()) {
            throw new ChessException("There is no possible moves for the chosen piece.");
        }
    }

    private void validateTargetPosition(Position source, Position target) {
        if (!board.piece(source).possibleMove(target)) {
            throw new ChessException("The chosen piece can't move to target position");
        }
    }

    private void nextTurn() {
        turn++;
        currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    private Piece makeMove(Position source, Position target) {
        ChessPiece p = (ChessPiece) board.removePiece(source);
        p.increaseMoveCount();
        Piece capturedPiece = board.removePiece(target);
        board.placePiece(p, target);

        if (capturedPiece != null) {
            piecesOnTheBoard.remove(capturedPiece);
            capturedPieces.add(capturedPiece);
        }

        castlingMove(p, source, target, KINGSIDE_ROOK);
        castlingMove(p, source, target, QUEENSIDE_ROOK);
        enPassantMove(p, source, target, capturedPiece);
        return capturedPiece;
    }

    public ChessPiece replacePromotedPiece(String type) {
        if (promoted == null) {
            throw new IllegalStateException("There is no peice to be promoted");
        }

        if (!PROMOTE_PIECES.contains(type)) {
            return promoted;
        }

        Position pos = promoted.getChessPosition().toPosition();

        Piece p = board.removePiece(pos);
        piecesOnTheBoard.remove(p);

        ChessPiece newPiece = newPiece(type, promoted.getColor());

        board.placePiece(newPiece, pos);
        piecesOnTheBoard.add(newPiece);

        return newPiece;
    }

    private ChessPiece newPiece(String type, Color color) {
        ChessPiece newPiece;
        switch (type) {
            case "B":
                newPiece = new Bishop(board, color);
                break;
            case "N":
                newPiece = new Knight(board, color);
                break;
            case "R":
                newPiece = new Rook(board, color);
                break;
            case "Q":
                newPiece = new Queen(board, color);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
        return newPiece;
    }

    private void castlingMove(ChessPiece p, Position source, Position target, int[] direction) {
        if (p instanceof King && target.getColumn() == source.getColumn() + direction[2]) {
            Position sourceT = new Position(source.getRow(), source.getColumn() + direction[0]);
            Position targetT = new Position(source.getRow(), source.getColumn() + direction[1]);
            ChessPiece rook = (ChessPiece) board.removePiece(sourceT);
            board.placePiece(rook, targetT);
            rook.increaseMoveCount();
        }
    }

    private void undoCastlingMove(ChessPiece p, Position source, Position target, int[] direction) {
        if (p instanceof King && target.getColumn() == source.getColumn() + direction[2]) {
            Position sourceT = new Position(source.getRow(), source.getColumn() + direction[0]);
            Position targetT = new Position(source.getRow(), source.getColumn() + direction[1]);
            ChessPiece rook = (ChessPiece) board.removePiece(targetT);
            board.placePiece(rook, sourceT);
            rook.decreaseMoveCount();
        }
    }

    private void enPassantMove(ChessPiece p, Position source, Position target, Piece capturedPiece) {

        if (p instanceof Pawn) {
            if (source.getColumn() != target.getColumn() && capturedPiece == null) {
                Position pawnPosition = null;
                if (p.getColor() == Color.WHITE) {
                    pawnPosition = new Position(target.getRow() + ENPASSANT_WHITE[2], target.getColumn());
                }
                if (p.getColor() == Color.BLACK) {
                    pawnPosition = new Position(target.getRow() + ENPASSANT_BLACK[2], target.getColumn());
                }
                capturedPiece = board.removePiece(pawnPosition);
                capturedPieces.add(capturedPiece);
                piecesOnTheBoard.remove(capturedPiece);
            }
        }
    }

    private void undoEnPassantMove(ChessPiece p, Position source, Position target, Piece capturedPiece) {

        if (p instanceof Pawn) {
            if (source.getColumn() != target.getColumn() && capturedPiece == enPassantVulnerable) {
                ChessPiece pawn = (ChessPiece) board.removePiece(target);
                Position pawnPosition = null;
                if (p.getColor() == Color.WHITE) {
                    pawnPosition = new Position(ENPASSANT_WHITE[0], target.getColumn());
                }
                if (p.getColor() == Color.BLACK) {
                    pawnPosition = new Position(ENPASSANT_BLACK[0], target.getColumn());
                }
                board.placePiece(pawn, pawnPosition);
            }
        }
    }

    private void undoMove(Position source, Position target, Piece capturedPiece) {
        ChessPiece p = (ChessPiece) board.removePiece(target);
        p.decreaseMoveCount();
        board.placePiece(p, source);

        if (capturedPiece != null) {
            board.placePiece(p, target);
            capturedPieces.remove(capturedPiece);
            piecesOnTheBoard.add(capturedPiece);
        }

        undoCastlingMove(p, source, target, KINGSIDE_ROOK);
        undoCastlingMove(p, source, target, QUEENSIDE_ROOK);
        undoEnPassantMove(p, source, target, capturedPiece);
    }

    private Color opponent(Color color) {
        return (color == Color.WHITE ? Color.BLACK : Color.WHITE);
    }

    private ChessPiece king(Color color) {
        List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == color).collect(Collectors.toList());

        for (Piece p : list) {
            if (p instanceof King) {
                return (ChessPiece) p;
            }
        }
        throw new IllegalStateException("There is no " + color + " king on the board");
    }

    private boolean testCheck(Color color) {

        Position kingPosition = king(color).getChessPosition().toPosition();

        List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == opponent(color)).collect(Collectors.toList());

        for (Piece p : opponentPieces) {
            boolean[][] mat = p.possibleMoves();

            if (mat[kingPosition.getRow()][kingPosition.getColumn()]) {
                return true;
            }
        }
        return false;
    }

    public boolean testCheckMate(Color color) {
        if (!testCheck(color)) {
            return false;
        }

        List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == opponent(color)).collect(Collectors.toList());

        for (Piece p : list) {
            boolean[][] mat = p.possibleMoves();
            for (int i = 0; i < board.getRows(); i++) {
                for (int j = 0; j < board.getColumns(); j++) {
                    if (mat[i][j]) {
                        Position source = ((ChessPiece) p).getChessPosition().toPosition();
                        Position target = new Position(i, j);
                        Piece capturedPiece = makeMove(source, target);
                        boolean testCheck = testCheck(color);
                        undoMove(source, target, capturedPiece);
                        if (!testCheck) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private void placeNewPiece(char column, int row, ChessPiece piece) {
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
        piecesOnTheBoard.add(piece);
    }

    private void initialSetup() {
        placeWhitePieces();
        placeBlackPieces();
    }

    private void placeWhitePieces() {
        placeNewPiece('a', 1, new Rook(board, Color.WHITE));
        placeNewPiece('b', 1, new Knight(board, Color.WHITE));
        placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('d', 1, new Queen(board, Color.WHITE));
        placeNewPiece('e', 1, new King(board, Color.WHITE, this));
        placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('g', 1, new Knight(board, Color.WHITE));
        placeNewPiece('h', 1, new Rook(board, Color.WHITE));
        placeNewPiece('a', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('b', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('c', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('d', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('e', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('f', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('g', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('h', 2, new Pawn(board, Color.WHITE, this));
    }

    private void placeBlackPieces() {
        placeNewPiece('a', 8, new Rook(board, Color.BLACK));
        placeNewPiece('b', 8, new Knight(board, Color.BLACK));
        placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('d', 8, new Queen(board, Color.BLACK));
        placeNewPiece('e', 8, new King(board, Color.BLACK, this));
        placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('g', 8, new Knight(board, Color.BLACK));
        placeNewPiece('h', 8, new Rook(board, Color.BLACK));
        placeNewPiece('a', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('b', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('c', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('d', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('e', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('f', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('g', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('h', 7, new Pawn(board, Color.BLACK, this));
    }
}
