import React from "react";
import ChessBoardRow from "./ChessBoardRow";
import ColumnLabels from "./ColumnLabels";
import GameInfo from "./GameInfo";
import { useChessGame } from "../hooks/useChessGame";
import "../styles/ChessBoard.css";

const ChessBoard = () => {
  const {
    board,
    turn,
    status,
    gameId,
    check,
    checkMate,
    possibleMoves,
    selectedPiece,
    handleMove,
    handlePieceSelect,
    columnsLabels,
  } = useChessGame();

  return (
    <div className="chess-board-container">
      <ColumnLabels labels={columnsLabels} />
      {Array.isArray(board) ? (
        board.map((row, i) => (
          <ChessBoardRow
            key={i}
            row={row}
            rowIndex={i}
            possibleMoves={possibleMoves}
            selectedPiece={selectedPiece}
            handleMove={handleMove}
            handlePieceSelect={handlePieceSelect}
          />
        ))
      ) : (
        <div>Error: Board data is not an array</div>
      )}
      <ColumnLabels labels={columnsLabels} />
      <GameInfo
       turn={turn}
       status={status}
       gameId={gameId}
       check={check}
       checkMate={checkMate}
       />
    </div>
  );
};

export default ChessBoard;
