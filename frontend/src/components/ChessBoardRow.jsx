import React from "react";
import PropTypes from "prop-types";
import Square from "./Square";

const ChessBoardRow = ({
  row,
  rowIndex,
  possibleMoves,
  selectedPiece,
  handleMove,
  handlePieceSelect,
}) => {
  return (
    <div className="board-row">
      <div className="row-label">{8 - rowIndex}</div>
      {row.map((piece, columnIndex) => {
        const isPossibleMove = possibleMoves[rowIndex]?.[columnIndex];
        return (
          <Square
            key={columnIndex}
            piece={piece}
            isBlack={(rowIndex + columnIndex) % 2 === 1}
            onMove={handleMove}
            onPieceSelect={handlePieceSelect}
            isSelected={
              selectedPiece &&
              selectedPiece.chessPosition?.row === piece?.chessPosition?.row &&
              selectedPiece.chessPosition?.column ===
                piece?.chessPosition?.column
            }
            isPossibleMove={isPossibleMove}
          />
        );
      })}
      <div className="row-label">{8 - rowIndex}</div>
    </div>
  );
};

ChessBoardRow.propTypes = {
  row: PropTypes.array.isRequired,
  rowIndex: PropTypes.number.isRequired,
  possibleMoves: PropTypes.array.isRequired,
  selectedPiece: PropTypes.object,
  handleMove: PropTypes.func.isRequired,
  handlePieceSelect: PropTypes.func.isRequired,
};

export default ChessBoardRow;
