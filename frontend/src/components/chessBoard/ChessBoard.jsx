import React from "react";
import Square from "../square/index";
import "./chessBoard.css";

const ChessBoard = () => {
  const board = [];
  const rows = 8;
  const columns = 8;
  const columnsLabels = ["A", "B", "C", "D", "E", "F", "G", "H"];
  for (let i = 0; i < rows; i++) {
    const row = [];
    for (let j = 0; j < columns; j++) {
      const isBlack = (i + j) % 2 === 1;
      row.push(<Square key={`${i}-${j}`} isBlack={isBlack} />);
    }
    board.push(
      <div key={i} className="board-row">
        {" "}
        <div className="row-label">{8 - i}</div> {row}{" "}
        <div className="row-label">{8 - i}</div>{" "}
      </div>
    );
  }
  return (<div className="chess-board-center">
    <div className="chess-board-container">
      {" "}
      <div className="column-labels">
        {" "}
        <div className="corner"></div>{" "}
        {columnsLabels.map((label) => (
          <div key={label} className="column-label">
            {label}
          </div>
        ))}{" "}
        <div className="corner"></div>{" "}
      </div>{" "}
      {board}{" "}
      <div className="column-labels">
        {" "}
        <div className="corner"></div>{" "}
        {columnsLabels.map((label) => (
          <div key={label} className="column-label">
            {label}
          </div>
        ))}{" "}
        <div className="corner"></div>{" "}
      </div>{" "}
    </div>
    </div>
  );
};
export default ChessBoard;
