import React from "react";
import "../styles/Square.css";
import {
  handleDrop,
  handleDragOver,
  handleDragStart,
  handlePieceClick,
} from "../handlers/SquareHandlers";

const Square = ({
  piece,
  isBlack,
  onMove,
  onPieceSelect,
  isSelected,
  isPossibleMove,
  selectedPiece,
  stompClient,
  setPossibleMoves,
}) => {
  let className = isBlack ? "square black" : "square white";

  if (isSelected) {
    className += " selected";
  }

  if (isPossibleMove) {
    className += " possible-move";
  }

  return (
    <div
      className={className}
      onDrop={(event) => handleDrop(event, piece, onMove)}
      onDragOver={handleDragOver}
      onClick={() =>
        handlePieceClick(
          piece,
          onPieceSelect,
          selectedPiece,
          stompClient,
          setPossibleMoves
        )
      }
    >
      {piece && piece.color && piece.symbol ? (
        <img
          src={`/assets/${piece.color?.toUpperCase()}-${piece.symbol}.png`}
          alt={`${piece.color} ${piece.symbol}`}
          draggable
          onDragStart={(event) => handleDragStart(event, piece)}
        />
      ) : null}
    </div>
  );
};

export default Square;
