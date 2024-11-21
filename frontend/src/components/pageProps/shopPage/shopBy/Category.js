// src/components/filters/CategoryFilter.js
import React, { useState } from "react";
import { motion } from "framer-motion";
import NavTitle from "./NavTitle";

const CategoryFilter = ({ onCategorySelect }) => {
  const [showCategories, setShowCategories] = useState(true);

  const categories = [
    { _id: 1, title: "Men" },
    { _id: 2, title: "Women" },
    { _id: 3, title: "Kids" },
    { _id: 4, title: "Accessories" },
    { _id: 5, title: "Shoes" },
  ];

  return (
    <div>
      <div
        onClick={() => setShowCategories(!showCategories)}
        className="cursor-pointer"
      >
        <NavTitle title="Shop by Category" icons={true} />
      </div>
      {showCategories && (
        <motion.div
          initial={{ y: -20, opacity: 0 }}
          animate={{ y: 0, opacity: 1 }}
          transition={{ duration: 0.5 }}
        >
          <ul className="flex flex-col gap-4">
            {categories.map((category) => (
              <li
                key={category._id}
                className="border-b-[1px] border-b-[#F0F0F0] pb-2 cursor-pointer"
                onClick={() => onCategorySelect(category.title)}
              >
                {category.title}
              </li>
            ))}
          </ul>
        </motion.div>
      )}
    </div>
  );
};

export default CategoryFilter;
