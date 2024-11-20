import React from 'react';

const CustomArrow = () => {
  return (
    <svg
      width="80" // Adjust width for arrow length
      height="40" // Adjust height for arrow size
      viewBox="0 0 50 20"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
    >
      <path
        d="M45 10 H5 L10 5 M5 10 L10 15" // Defines the arrow shape
        stroke="#333" // Line color
        strokeWidth="1" // Adjust for thinner/thicker line
        strokeLinecap="round" // Makes arrow line ends round
      />
    </svg>
  );
};

export default CustomArrow;
