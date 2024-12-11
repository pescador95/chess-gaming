package com.pescador95.application.BasicUI;

import com.pescador95.application.UI;
import com.pescador95.chess.ChessException;
import com.pescador95.chess.ChessPosition;
import com.pescador95.application.services.ChessService;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ChessTerminalUI {

    private Scanner sc = new Scanner(System.in);
    private ChessService chessService = new ChessService();

    public void runGame() {
        while (!chessService.isCheckMate()) {
            try {
                UI.clearScreen();
                UI.printMatch(chessService.getChessMatch(), chessService.getCapturedPieces());

                System.out.println();
                System.out.println("Source: ");
                ChessPosition source = UI.readChessPosition(sc);

                boolean[][] possiblesMoves = chessService.getPossibleMoves(source);
                UI.clearScreen();
                UI.printBoard(chessService.getChessMatch().getPieces(), possiblesMoves);

                System.out.println();
                System.out.println("Target: ");
                ChessPosition target = UI.readChessPosition(sc);

                chessService.movePiece(source, target);

                if (chessService.getChessMatch().getPromoted() != null) {
                    System.out.print("Enter piece for promotion (B/N/R/Q): ");
                    String type = sc.nextLine().toUpperCase();
                    chessService.promotePiece(type);
                }
            } catch (ChessException | InputMismatchException e) {
                System.out.println(e.getMessage());
                sc.nextLine();
            }
        }
        UI.clearScreen();
        UI.printMatch(chessService.getChessMatch(), chessService.getCapturedPieces());
    }
}


