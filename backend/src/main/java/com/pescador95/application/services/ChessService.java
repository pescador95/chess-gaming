package com.pescador95.application.services;

import com.pescador95.chess.ChessException;
import com.pescador95.chess.ChessMatch;
import com.pescador95.chess.ChessPiece;
import com.pescador95.chess.ChessPosition;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChessService {

    private final Map<String, ChessMatch> games = new HashMap<>();
    private final ChessMatch chessMatch;
    private final List<ChessPiece> captured;

    public ChessService() {
        chessMatch = new ChessMatch();
        captured = new ArrayList<>();
    }

    public ChessMatch createGame() {
        ChessMatch chessMatch = new ChessMatch();
        games.put(chessMatch.getGameId(), chessMatch);
        return chessMatch;
    }

    public ChessMatch getGame(String gameId) {
        return games.get(gameId);
    }

    public void removeGame(String gameId) {
        games.remove(gameId);
    }

    public ChessMatch getChessMatch() {
        return chessMatch;
    }

    public Map<String, ChessMatch> getGames() {
        return games;
    }

    public List<ChessPiece> getCapturedPieces() {
        return captured;
    }

    public ChessPiece movePiece(ChessPosition source, ChessPosition target) throws ChessException {
        ChessPiece capturedPiece = chessMatch.performChessMove(source, target);
        if (capturedPiece != null) {
            captured.add(capturedPiece);
        }
        return capturedPiece;
    }

    public boolean[][] getPossibleMoves(ChessPosition source) throws ChessException {
        return chessMatch.possibleMoves(source);
    }

    public void promotePiece(String type) throws IllegalArgumentException {
        if (chessMatch.getPromoted() != null) {
            chessMatch.replacePromotedPiece(type);
            this.games.put(this.chessMatch.getGameId(), this.chessMatch);
        }
    }

    public boolean isCheckMate() {
        return chessMatch.getCheckMate();
    }
}