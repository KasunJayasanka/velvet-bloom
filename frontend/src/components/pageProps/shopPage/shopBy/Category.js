// Category.js
import React, { useEffect, useState } from "react";
import { motion } from "framer-motion";
import NavTitle from "./NavTitle";

const Category = ({ onCategorySelect }) => {
  const [showCategories, setShowCategories] = useState(true);
  const [categories, setCategories] = useState([]);
  const [filteredCategories, setFilteredCategories] = useState([]);
  const [searchCategory, setSearchCategory] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  // Fetch categories from API
  const fetchCategories = async () => {
    try {
      setLoading(true);
      setError(null);
      
      // For now, using sample categories
      // Replace this with your API call
      const sampleCategories = [
        { _id: 1, title: "All Categories" },
        { _id: 2, title: "Men's Clothing" },
        { _id: 3, title: "Women's Clothing" },
        { _id: 4, title: "T-Shirts" },
        { _id: 5, title: "Unisex" },
        { _id: 6, title: "Outerwear" }
      ];

      // Simulate API delay
      await new Promise(resolve => setTimeout(resolve, 500));
      
      // When implementing real API, replace with:
      // const response = await fetch('your-api-endpoint');
      // const data = await response.json();
      // setCategories(data);
      
      setCategories(sampleCategories);
      setFilteredCategories(sampleCategories);
    } catch (err) {
      setError("Failed to load categories. Please try again later.");
      console.error("Error fetching categories:", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCategories();
  }, []);

  useEffect(() => {
    const filtered = categories.filter((category) =>
      category.title.toLowerCase().includes(searchCategory.toLowerCase()) &&
      category.title !== "All Categories" // Don't filter "All Categories"
    );
    
    // Always keep "All Categories" at the top if there's a search
    if (searchCategory) {
      const allCategoriesOption = categories.find(cat => cat.title === "All Categories");
      if (allCategoriesOption) {
        setFilteredCategories([allCategoriesOption, ...filtered]);
      } else {
        setFilteredCategories(filtered);
      }
    } else {
      setFilteredCategories(categories);
    }
  }, [searchCategory, categories]);

  const handleCategoryClick = (category) => {
    const categoryValue = category === "All Categories" ? null : category;
    onCategorySelect(categoryValue);
  };

  return (
    <div className="w-full">
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
          className="w-full"
        >
          <div className="flex flex-col gap-4 mt-4">
            <input
              type="text"
              placeholder="Search Categories"
              value={searchCategory}
              onChange={(e) => setSearchCategory(e.target.value)}
              className="w-full p-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            />
            
            {loading && (
              <div className="text-center py-4 text-gray-500">
                Loading categories...
              </div>
            )}
            
            {error && (
              <div className="text-center py-4 text-red-500">
                {error}
                <button 
                  onClick={fetchCategories}
                  className="ml-2 text-blue-500 hover:text-blue-700 underline"
                >
                  Retry
                </button>
              </div>
            )}
            
            {!loading && !error && filteredCategories.map((category) => (
              <div
                key={category._id}
                onClick={() => handleCategoryClick(category.title)}
                className={`
                  py-2 px-3 cursor-pointer rounded
                  ${category.title === "All Categories" 
                    ? "font-semibold bg-gray-100 hover:bg-gray-200" 
                    : "hover:bg-gray-50"}
                  transition-all duration-200
                  border-b border-gray-100
                  flex justify-between items-center
                `}
              >
                <span>{category.title}</span>
                {category.title === "All Categories" && (
                  <span className="text-sm text-gray-500">
                    ({categories.length - 1})
                  </span>
                )}
              </div>
            ))}
            
            {!loading && !error && filteredCategories.length === 0 && (
              <div className="text-center py-4 text-gray-500">
                No categories found
              </div>
            )}
          </div>
        </motion.div>
      )}
    </div>
  );
};

export default Category;