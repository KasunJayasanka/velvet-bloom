// Price.js
import React, { useState } from "react";
import { motion } from "framer-motion";
import NavTitle from "./NavTitle";

const Price = ({ onPriceRangeSelect }) => {
  const [showPrice, setShowPrice] = useState(true);
  // Define absolute min and max prices
  const PRICE_MIN = 0;
  const PRICE_MAX = 1000;
  
  const [priceRange, setPriceRange] = useState({
    min: PRICE_MIN,
    max: PRICE_MAX
  });

  // Format price to display with dollar sign
  const formatPrice = (value) => `$${parseInt(value).toLocaleString()}`;

  // Calculate the percentage for slider background
  const getBackgroundSize = (value, type) => {
    const min = type === 'min' ? 
      ((value - PRICE_MIN) / (PRICE_MAX - PRICE_MIN)) * 100 : 
      ((priceRange.min - PRICE_MIN) / (PRICE_MAX - PRICE_MIN)) * 100;
    
    const max = type === 'max' ? 
      ((value - PRICE_MIN) / (PRICE_MAX - PRICE_MIN)) * 100 : 
      ((priceRange.max - PRICE_MIN) / (PRICE_MAX - PRICE_MIN)) * 100;
    
    return { background: `linear-gradient(to right, #e5e7eb ${min}%, #000 ${min}%, #000 ${max}%, #e5e7eb ${max}%)` };
  };

  // Handle slider change
  const handleRangeChange = (e) => {
    const { name, value } = e.target;
    const newValue = parseInt(value);
    
    setPriceRange(prev => {
      const newRange = {
        ...prev,
        [name]: newValue
      };
      
      // Ensure min doesn't exceed max and vice versa
      if (name === 'min' && newValue > prev.max) {
        newRange.min = prev.max;
      } else if (name === 'max' && newValue < prev.min) {
        newRange.max = prev.min;
      }
      
      // Convert to numbers before sending to parent
      const rangeToSend = {
        min: Number(newRange.min),
        max: Number(newRange.max)
      };
      
      // Log the range being sent
      console.log("Sending price range:", rangeToSend);
      
      // Notify parent component of changes
      onPriceRangeSelect(rangeToSend);
      return newRange;
    });
  };

  const handleReset = () => {
    const defaultRange = { min: PRICE_MIN, max: PRICE_MAX };
    setPriceRange(defaultRange);
    onPriceRangeSelect(defaultRange);
  };

  return (
    <div className="w-full">
      <div
        onClick={() => setShowPrice(!showPrice)}
        className="cursor-pointer"
      >
        <NavTitle title="Shop by Price" icons={true} />
      </div>
      
      {showPrice && (
        <motion.div
          initial={{ y: -20, opacity: 0 }}
          animate={{ y: 0, opacity: 1 }}
          transition={{ duration: 0.5 }}
          className="w-full"
        >
          <div className="flex flex-col gap-4 mt-4">
            {/* Price display */}
            <div className="flex justify-between items-center">
              <span className="text-sm font-medium">
                {formatPrice(priceRange.min)}
              </span>
              <span className="text-sm font-medium">
                {formatPrice(priceRange.max)}
              </span>
            </div>

            {/* Slider container */}
            <div className="relative h-2 mt-4">
              <input
                type="range"
                name="min"
                min={PRICE_MIN}
                max={PRICE_MAX}
                value={priceRange.min}
                onChange={handleRangeChange}
                className="absolute w-full h-1 bg-gray-200 rounded-lg appearance-none cursor-pointer"
                style={getBackgroundSize(priceRange.min, 'min')}
              />
              <input
                type="range"
                name="max"
                min={PRICE_MIN}
                max={PRICE_MAX}
                value={priceRange.max}
                onChange={handleRangeChange}
                className="absolute w-full h-1 bg-transparent rounded-lg appearance-none cursor-pointer"
                style={getBackgroundSize(priceRange.max, 'max')}
              />
            </div>

            {/* Manual input fields */}
            <div className="flex gap-4 mt-2">
              <div className="flex-1">
                <input
                  type="number"
                  name="min"
                  value={priceRange.min}
                  onChange={handleRangeChange}
                  min={PRICE_MIN}
                  max={priceRange.max}
                  className="w-full p-2 text-sm border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              <div className="flex-1">
                <input
                  type="number"
                  name="max"
                  value={priceRange.max}
                  onChange={handleRangeChange}
                  min={priceRange.min}
                  max={PRICE_MAX}
                  className="w-full p-2 text-sm border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
            </div>

            {/* Reset button */}
            <button
              onClick={handleReset}
              className="w-full mt-2 px-4 py-2 text-sm text-gray-600 border border-gray-300 rounded hover:bg-gray-50 transition-colors"
            >
              Reset Price Filter
            </button>
          </div>

          {/* Custom styles for range inputs */}
          <style jsx>{`
            input[type='range'] {
              -webkit-appearance: none;
              pointer-events: none;
            }

            input[type='range']::-webkit-slider-thumb {
              height: 1rem;
              width: 1rem;
              border-radius: 50%;
              background-color: black;
              -webkit-appearance: none;
              pointer-events: auto;
              cursor: pointer;
            }

            input[type='range']::-moz-range-thumb {
              height: 1rem;
              width: 1rem;
              border-radius: 50%;
              background-color: black;
              border: none;
              pointer-events: auto;
              cursor: pointer;
            }

            input[type='range']::-ms-thumb {
              height: 1rem;
              width: 1rem;
              border-radius: 50%;
              background-color: black;
              pointer-events: auto;
              cursor: pointer;
            }

            input[type='range']:focus {
              outline: none;
            }
          `}</style>
        </motion.div>
      )}
    </div>
  );
};

export default Price;