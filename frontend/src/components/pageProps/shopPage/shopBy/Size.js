import React, { useState } from "react";
import { motion } from "framer-motion";
import NavTitle from "./NavTitle";

const Size = ({ onSizeSelect }) => {
  const [showSizes, setShowSizes] = useState(true);
  const [selectedSize, setSelectedSize] = useState(null);

  const sizes = [
    { _id: 1, title: "XS", label: "Extra Small" },
    { _id: 2, title: "S", label: "Small" },
    { _id: 3, title: "M", label: "Medium" },
    { _id: 4, title: "L", label: "Large" },
    { _id: 5, title: "XL", label: "Extra Large" },
    { _id: 6, title: "2XL", label: "Double Extra Large" },
    { _id: 7, title: "3XL", label: "Triple Extra Large" },
  ];

  const handleSizeClick = (size) => {
    const newSize = selectedSize === size ? null : size;
    setSelectedSize(newSize);
    onSizeSelect(newSize);
  };

  return (
    <div>
      <div
        onClick={() => setShowSizes(!showSizes)}
        className="cursor-pointer"
      >
        <NavTitle title="Shop by Size" icons={true} />
      </div>
      {showSizes && (
        <motion.div
          initial={{ y: -20, opacity: 0 }}
          animate={{ y: 0, opacity: 1 }}
          transition={{ duration: 0.5 }}
        >
          <div className="flex flex-col gap-4 mt-4">
            <div 
              className={`
                py-2 px-3 cursor-pointer rounded
                ${!selectedSize ? 'bg-blue-500 text-white' : 'hover:bg-gray-50'}
                transition-all duration-200 border border-gray-200
              `}
              onClick={() => handleSizeClick(null)}
            >
              All Sizes
            </div>
            
            <div className="grid grid-cols-2 gap-4">
              {sizes.map((size) => (
                <div
                  key={size._id}
                  onClick={() => handleSizeClick(size.title)}
                  className={`
                    py-2 px-3 cursor-pointer rounded
                    ${selectedSize === size.title 
                      ? 'bg-blue-500 text-white' 
                      : 'hover:bg-gray-50'
                    }
                    transition-all duration-200
                    border border-gray-200
                    text-center
                  `}
                  title={size.label}
                >
                  {size.title}
                </div>
              ))}
            </div>
          </div>
        </motion.div>
      )}
    </div>
  );
};

export default Size;