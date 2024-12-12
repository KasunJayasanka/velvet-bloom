import React from 'react';
import { useNavigate } from 'react-router-dom';
import CustomArrow from '../../../assets/icons/CustomArrows';

const ArrowButtonToHome = () => {
    const navigate = useNavigate();

    const handleNavigateHome = () => {
        navigate('/'); // Replace '/' with the home route of your application
    };

    return (
        <button 
            onClick={handleNavigateHome}
            style={{
                background: 'none', 
                border: 'none', 
                cursor: 'pointer', 
                padding: '0'
            }}
            aria-label="Go to Home"
        >
            <CustomArrow />
        </button>
    );
};

export default ArrowButtonToHome;
