package com.pescador95.application.controller;

import com.pescador95.application.DTO.ChessDTO.ChessDTO;
import com.pescador95.chess.ChessMatch;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ChessWSController {

    ChessController chessController = new ChessController();

    @MessageMapping("/init")
    @SendTo("/topic/board")
    public ChessMatch initBoard() {
        return chessController.createNewGame();
    }

    @MessageMapping("/stat/{gameId}")
    @SendTo("/topic/gameStat")
    public ChessMatch getGameStat(@PathVariable String gameId) {
        return chessController.getGameStat(gameId);
    }

    @MessageMapping("/stat/end/{gameId}")
    @SendTo("/topic/endGame")
    public String endGame(@PathVariable String gameId) {
        chessController.endGame(gameId);
        return "Game ended";
    }

    @MessageMapping("/possibleMoves")
    @SendTo("/topic/possibleMoves")
    public boolean[][] possibleMoves(@RequestBody ChessDTO request) {
        return chessController.possibleMoves(request);
    }

    @MessageMapping("/move")
    @SendTo("/topic/board")
    public ChessMatch movePiece(@RequestBody ChessDTO request) {
        return chessController.movePiece(request);
    }

    @MessageMapping("/promote")
    @SendTo("/topic/promotion")
    public String promotePiece(@RequestParam String type) {
        return chessController.promotePiece(type);
    }
}