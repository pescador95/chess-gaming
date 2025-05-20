import React from "react";
import PropTypes from "prop-types";

const GameInfo = ({ turn, status, gameId, check, checkMate }) => {
  return (
    <div className="game-info">
      
      <div>Turn: {turn}</div>
      <div>Check: {check ? 'true' : 'false'}</div>
      <div>Check Mate: {checkMate ? 'true' : 'false'}</div>
      <div>Game Id: {gameId}</div>
      <div>Status: {status}</div>

    </div>

  );
};

GameInfo.propTypes = {
  turn: PropTypes.string.isRequired,
  check: Boolean,
  status: PropTypes.string.isRequired,
  gameId: PropTypes.string.isRequired,
  checkMate: Boolean
};

export default GameInfo;
