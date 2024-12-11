import { handleMove } from "./GameHandlers";

export const handleDrop = (event, piece, onMove) => {
  event.preventDefault();
  const source = JSON.parse(event.dataTransfer.getData("piece"));

  const target = {
    row: piece?.chessPosition?.row,
    column: piece?.chessPosition?.column,
  };
  onMove(source, target);
};

export const handleDragOver = (event) => {
  event.preventDefault();
};

export const handleDragStart = (event, piece) => {
  if (piece && piece.chessPosition) {
    event.dataTransfer.setData(
      "piece",
      JSON.stringify({
        row: piece.chessPosition.row,
        column: piece.chessPosition.column,
      })
    );
  }
};

export const handlePieceClick = (
  clickedTile,
  onPieceSelect,
  selectedPiece,
  stompClient,
  setPossibleMoves
) => {
  console.log("Clicked Tile:", clickedTile);
  if (selectedPiece && clickedTile) {
    if (clickedTile.isPossibleMove) {
      handleMove(
        selectedPiece,
        {
          ...clickedTile,
          chessPosition: {
            ...clickedTile.chessPosition,
            column: clickedTile.chessPosition.column,
          },
        },
        stompClient,
        setPossibleMoves
      );
    } else {
      onPieceSelect(clickedTile);
      setPossibleMoves(Array(8).fill(Array(8).fill(false)));
    }
  } else {
    onPieceSelect(clickedTile);
  }
};
