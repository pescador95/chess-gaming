import React from "react";
import PropTypes from "prop-types";

const ColumnLabels = ({ labels }) => {
  return (
    <div className="column-labels">
      {labels.map((label, index) => (
        <div key={index} className="column-label">
          {label}
        </div>
      ))}
    </div>
  );
};

ColumnLabels.propTypes = {
  labels: PropTypes.array.isRequired,
};

export default ColumnLabels;
