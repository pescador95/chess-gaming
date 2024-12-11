import React from "react";
import "./square.css";
const Square = ({ piece, isBlack }) => {
  const className = isBlack ? "square black" : "square white";
  return (
    <div className={className}>
      {" "}
      {piece && (
        <img
          src={`/assets/${piece.color}-${piece.type}.png`}
          alt={`${piece.color} ${piece.type}`}
        />
      )}{" "}
    </div>
  );
};
export default Square;
