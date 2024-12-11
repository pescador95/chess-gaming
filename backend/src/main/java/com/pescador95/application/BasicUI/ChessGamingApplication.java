package com.pescador95.application.BasicUI;

import com.pescador95.application.UI;
import com.pescador95.chess.ChessException;
import com.pescador95.chess.ChessMatch;
import com.pescador95.chess.ChessPiece;
import com.pescador95.chess.ChessPosition;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class ChessGamingApplication {

    public static void main(String[] args) {
        run();
    }

    @Deprecated
    public static void oldRun() {
        Scanner sc = new Scanner(System.in);
        ChessMatch chessMatch = new ChessMatch();
        List<ChessPiece> captured = new ArrayList<>();

        while (!chessMatch.getCheckMate()) {

            try {

                UI.clearScreen();
                UI.printMatch(chessMatch, captured);

                System.out.println();
                System.out.println("Source: ");
                ChessPosition source = UI.readChessPosition(sc);

                boolean[][] possiblesMoves = chessMatch.possibleMoves(source);
                UI.clearScreen();
                UI.printBoard(chessMatch.getPieces(), possiblesMoves);

                System.out.println();
                System.out.println("Target");
                ChessPosition target = UI.readChessPosition(sc);

                ChessPiece capturedPiece = chessMatch.performChessMove(source, target);

                if (capturedPiece != null) {
                    captured.add(capturedPiece);
                }

                if (chessMatch.getPromoted() != null) {
                    String msg = "Enter piece for promotion (B/N/R/Q): ";
                    System.out.print(msg);
                    String type = sc.nextLine().toUpperCase();
                    while (!ChessMatch.PROMOTE_PIECES.contains(type)) {
                        System.out.print("Invalid value! " + msg);
                        type = sc.nextLine().toUpperCase();
                    }
                    chessMatch.replacePromotedPiece(type);
                }

            } catch (ChessException | InputMismatchException e) {
                System.out.println(e.getMessage());
                sc.nextLine();
            }
        }
        UI.clearScreen();
        UI.printMatch(chessMatch, captured);
    }

    public static void run() {
        new ChessTerminalUI().runGame();
    }
}
