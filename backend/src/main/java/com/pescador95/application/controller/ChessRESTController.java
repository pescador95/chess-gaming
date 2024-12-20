package com.pescador95.application.controller;

import com.pescador95.application.DTO.ChessDTO.ChessDTO;
import com.pescador95.chess.ChessException;
import com.pescador95.chess.ChessMatch;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chess")
public class ChessRESTController {

    ChessController chessController = new ChessController();

    @PostMapping("/new")
    public ResponseEntity<?> createNewGame() {
        return ResponseEntity.ok(chessController.createNewGame());
    }

    @GetMapping("/stat/{gameId}")
    public ResponseEntity<?> getGameStat(@PathVariable String gameId) {
        ChessMatch chessMatch = chessController.getGameStat(gameId);
        if (chessMatch == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game not found");
        }
        return ResponseEntity.ok(chessMatch);
    }

    @DeleteMapping("/stat/{gameId}")
    public ResponseEntity<?> endGame(@PathVariable String gameId) {
        ChessMatch chessMatch = chessController.getGameStat(gameId);
        if (chessMatch == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game not found");
        }
        return ResponseEntity.ok(chessController.endGame(gameId));
    }

    @PostMapping("/possibleMoves")
    public ResponseEntity<?> possibleMoves(@RequestBody ChessDTO request) {
        return ResponseEntity.ok(chessController.possibleMoves(request));
    }

    @PostMapping("/move")
    public ResponseEntity<?> movePiece(@RequestBody ChessDTO request) {
        try {
            return ResponseEntity.ok(chessController.movePiece(request));
        } catch (ChessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/promote")
    public ResponseEntity<?> promotePiece(@RequestParam String type) {
        try {
            return ResponseEntity.ok(chessController.promotePiece(type));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid promotion type");
        }
    }
}
