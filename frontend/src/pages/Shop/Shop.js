import React, { useState, useEffect } from "react";
import Breadcrumbs from "../../components/pageProps/Breadcrumbs";
import Pagination from "../../components/pageProps/shopPage/Pagination";
import ProductBanner from "../../components/pageProps/shopPage/ProductBanner";
import ShopSideNav from "../../components/pageProps/shopPage/ShopSideNav";

const Shop = () => {
  const [itemsPerPage, setItemsPerPage] = useState(12);
  const [selectedCategory, setSelectedCategory] = useState(null);
  const [selectedSize, setSelectedSize] = useState(null);
  const [priceRange, setPriceRange] = useState({ min: 0, max: 1000 });
  const [paginationItems, setPaginationItems] = useState([]); // Data from API
  const [filteredItems, setFilteredItems] = useState([]);

  const itemsPerPageFromBanner = (itemsPerPage) => {
    setItemsPerPage(itemsPerPage);
  };

  // Fetch items from the API
  const fetchPaginationItems = async () => {
    try {
        const response = await fetch("http://localhost:8080/products");
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.json();
        console.log("Fetched data:", data);
        setPaginationItems(data);
    } catch (error) {
        console.error("Failed to fetch pagination items:", error);
    }
};

  

  // Combined filtering function
  const filterProducts = (category, size, price) => {
    let filtered = [...paginationItems];

    // Apply price filter
    filtered = filtered.filter((item) => {
      const itemPrice = Number(item.unitPrice);
      const minPrice = Number(price.min);
      const maxPrice = Number(price.max);
      return itemPrice >= minPrice && itemPrice <= maxPrice;
    });

    // Apply category filter
    if (category) {
      filtered = filtered.filter((item) =>
        item.categories?.some((cat) =>
          cat.toLowerCase().includes(category.toLowerCase())
        )
      );
    }

    // Apply size filter
    if (size) {
      filtered = filtered.filter((item) =>
        item.variety?.some((v) => v.size.toUpperCase() === size.toUpperCase())
      );
    }

    return filtered;
  };

  // Update filters when filters change
  useEffect(() => {
    const filtered = filterProducts(selectedCategory, selectedSize, priceRange);
    setFilteredItems(filtered);
  }, [selectedCategory, selectedSize, priceRange, paginationItems]);

  // Fetch items when component mounts
  useEffect(() => {
    fetchPaginationItems();
  }, []);

  const handleCategorySelect = (category) => {
    setSelectedCategory(category);
  };

  const handleSizeSelect = (size) => {
    setSelectedSize(size);
  };

  const handlePriceRangeSelect = (range) => {
    setPriceRange({ min: Number(range.min), max: Number(range.max) });
  };

  return (
    <div className="max-w-container mx-auto px-4">
      <Breadcrumbs title="Products" />
      <div className="w-full h-full flex pb-20 gap-10">
        <div className="w-[20%] lgl:w-[25%] hidden mdl:inline-flex h-full">
          <ShopSideNav
            onCategorySelect={handleCategorySelect}
            onSizeSelect={handleSizeSelect}
            onPriceRangeSelect={handlePriceRangeSelect}
          />
        </div>
        <div className="w-full mdl:w-[80%] lgl:w-[75%] h-full flex flex-col gap-10">
          <ProductBanner
            itemsPerPageFromBanner={itemsPerPageFromBanner}
            selectedCategory={selectedCategory}
            selectedSize={selectedSize}
            priceRange={priceRange}
          />
          <Pagination
            itemsPerPage={itemsPerPage}
            paginationItems={filteredItems}
          />
        </div>
      </div>
    </div>
  );
};

export default Shop;
