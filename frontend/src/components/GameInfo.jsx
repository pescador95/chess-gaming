import React from "react";
import PropTypes from "prop-types";

const GameInfo = ({ turn, status }) => {
  return (
    <div className="game-info">
      <div>Turn: {turn}</div>
      <div>Status: {status}</div>
    </div>
  );
};

GameInfo.propTypes = {
  turn: PropTypes.string.isRequired,
  status: PropTypes.string.isRequired,
};

export default GameInfo;
