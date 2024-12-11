package com.pescador95.application.controller;

import com.pescador95.application.DTO.ChessDTO.ChessDTO;
import com.pescador95.chess.ChessException;
import com.pescador95.chess.ChessMatch;
import com.pescador95.chess.ChessPiece;
import com.pescador95.chess.ChessPosition;
import com.pescador95.application.services.ChessService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chess")
public class ChessController {

    private ChessService chessService = new ChessService();

    @PostMapping("/new")
    public ResponseEntity<?> createNewGame() {
        ChessMatch chessMatch = chessService.createGame();
        return ResponseEntity.ok(chessMatch);
    }

    @GetMapping("/stat/{gameId}")
    public ResponseEntity<?> getGameStat(@PathVariable String gameId) {
        ChessMatch chessMatch = chessService.getGame(gameId);
        if (chessMatch == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game not found");
        }
        return ResponseEntity.ok(chessMatch);
    }

    @DeleteMapping("/stat/{gameId}")
    public ResponseEntity<?> endGame(@PathVariable String gameId) {
        chessService.removeGame(gameId);
        return ResponseEntity.ok("Game ended");
    }

    @PostMapping("/possibleMoves")
    public ResponseEntity<?> possibleMoves(@RequestBody ChessDTO request) {
        boolean[][] possiblesMoves = chessService.getPossibleMoves(new ChessPosition(request.getSourceRow(), request.getSourceColumn()));
        return ResponseEntity.ok(possiblesMoves);
    }

    @PostMapping("/move")
    public ResponseEntity<?> movePiece(@RequestBody ChessDTO request) {
        try {
            ChessPosition source = new ChessPosition(request.getSourceRow(), request.getSourceColumn());
            ChessPosition target = new ChessPosition(request.getTargetRow(), request.getTargetColumn());
            ChessPiece capturedPiece = chessService.movePiece(source, target);
            ChessMatch match = chessService.getChessMatch();
            chessService.getGames().put(match.getGameId(), match);
            return ResponseEntity.ok(match);
        } catch (ChessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/promote")
    public ResponseEntity<?> promotePiece(@RequestParam String type) {
        try {
            chessService.promotePiece(type);
            return ResponseEntity.ok("Piece promoted to: " + type);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid promotion type");
        }
    }
}