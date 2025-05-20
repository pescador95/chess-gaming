import { useEffect, useState } from "react";
import {
  initializeWebSocket,
  handlePromotion,
  handlePieceSelection,
  handleMove,
} from "../handlers/GameHandlers";
import initialBoardMock from "../InitialBoardMock";

export const useChessGame = () => {
  const [board, setBoard] = useState(initialBoardMock);
  const [turn, setTurn] = useState("WHITE");
  const [possibleMoves, setPossibleMoves] = useState(
    Array(8).fill(Array(8).fill(false))
  );
  const [selectedPiece, setSelectedPiece] = useState(null);
  const [status, setStatus] = useState("AGUARDANDO SERVIDOR");
  const [stompClient, setStompClient] = useState(null);
  const [gameId, setGameId] = useState(null);
  const [check, setCheck] = useState(false);
   const [checkMate, setCheckMate] = useState(false);

  const columnsLabels = ["A", "B", "C", "D", "E", "F", "G", "H"];

  useEffect(() => {
    const client = initializeWebSocket(
      setBoard,
      setTurn,
      setStatus,
      setPossibleMoves,
      setGameId,
      setCheck,
      setCheckMate
    );
    setStompClient(client);

    return () => {
      client?.deactivate();
    };
  }, []);

  return {
    board,
    turn,
    gameId,
    check,
    checkMate,
    status,
    possibleMoves,
    selectedPiece,
    handleMove: (source, target) =>
      handleMove(source, target, stompClient, setStatus),
    handlePieceSelect: (piece) =>
      handlePieceSelection(
        piece,
        stompClient,
        setSelectedPiece,
        setPossibleMoves,
        setStatus
      ),
    columnsLabels,
  };
};
