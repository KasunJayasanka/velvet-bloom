import React from 'react';
import './ProgressBar.css';

const ProgressBar = ({ steps }) => {
  return (
    <div className="progress-bar">
      {steps.map((step, index) => (
        <div key={index} className="progress-step">
          <div className={`circle ${step.completed ? 'completed' : ''}`}></div>
          <p>{step.label}</p>
          <span>{step.date}</span>
          {index < steps.length - 1 && <div className="line"></div>}
        </div>
      ))}
    </div>
  );
};

export default ProgressBar;