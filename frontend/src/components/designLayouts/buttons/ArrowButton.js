import React from 'react';

const ArrowButton = ({ direction, onClick }) => {
    return (
        <button
            onClick={onClick}
            style={{
                background: 'none',
                border: 'none',
                cursor: 'pointer',
                padding: '0',
                display: 'flex',
                alignItems: 'center',
            }}
            aria-label={`Go ${direction}`}
        >
            <svg
                xmlns="http://www.w3.org/2000/svg"
                width="24"
                height="24"
                fill="none"
                stroke="currentColor"
                strokeWidth="2"
                strokeLinecap="round"
                strokeLinejoin="round"
                className={`feather feather-arrow-${direction}`}
                style={{ transform: direction === 'left' ? 'rotate(180deg)' : '' }}
            >
                <line x1="12" y1="19" x2="12" y2="5"></line>
                <polyline points="5 12 12 5 19 12"></polyline>
            </svg>
        </button>
    );
};

export default ArrowButton;
