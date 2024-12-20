package com.pescador95.application.controller;

import com.pescador95.application.DTO.ChessDTO.ChessDTO;
import com.pescador95.application.services.ChessService;
import com.pescador95.chess.ChessException;
import com.pescador95.chess.ChessMatch;
import com.pescador95.chess.ChessPosition;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ChessController {

    private final ChessService chessService = new ChessService();

    public ChessMatch createNewGame() {
        return chessService.createGame();
    }

    public ChessMatch getGameStat(@PathVariable String gameId) {
        return chessService.getGame(gameId);
    }

    public String endGame(@PathVariable String gameId) {
        chessService.removeGame(gameId);
        return "Game ended";
    }

    public boolean[][] possibleMoves(@RequestBody ChessDTO request) {
        return chessService.getPossibleMoves(new ChessPosition(request.getSourceColumn(), request.getSourceRow()));
    }

    public ChessMatch movePiece(@RequestBody ChessDTO request) {
        ChessMatch returnMatch = new ChessMatch();
        try {
            ChessPosition source = new ChessPosition(request.getSourceColumn(), request.getSourceRow());
            ChessPosition target = new ChessPosition(request.getTargetColumn(), request.getTargetRow());
            chessService.movePiece(source, target);
            ChessMatch match = chessService.getChessMatch();
            chessService.getGames().put(match.getGameId(), match);
            returnMatch = match;
        } catch (ChessException e) {
            e.getMessage();
        } finally {
            return returnMatch;
        }
    }

    public String promotePiece(@RequestParam String type) {
        try {
            chessService.promotePiece(type);
            return "Piece promoted to: " + type;
        } catch (IllegalArgumentException e) {
            return "Invalid promotion type";
        }
    }
}