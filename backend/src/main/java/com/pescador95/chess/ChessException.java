package com.pescador95.chess;

import com.pescador95.boardGame.BoardException;

public class ChessException extends BoardException {
    private static final long serialVersionUID = 1L;

    public ChessException(String msg) {
        super(msg);
    }
}
