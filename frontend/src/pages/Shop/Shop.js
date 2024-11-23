import React, { useState, useEffect } from "react";
import Breadcrumbs from "../../components/pageProps/Breadcrumbs";
import Pagination from "../../components/pageProps/shopPage/Pagination";
import ProductBanner from "../../components/pageProps/shopPage/ProductBanner";
import ShopSideNav from "../../components/pageProps/shopPage/ShopSideNav";
import { paginationItems } from "../../constants";

const Shop = () => {
  const [itemsPerPage, setItemsPerPage] = useState(12);
  const [selectedCategory, setSelectedCategory] = useState(null);
  const [selectedSize, setSelectedSize] = useState(null);
  const [priceRange, setPriceRange] = useState({ min: 0, max: 1000 });
  const [filteredItems, setFilteredItems] = useState(paginationItems);

  const itemsPerPageFromBanner = (itemsPerPage) => {
    setItemsPerPage(itemsPerPage);
  };

  // Combined filtering function with fixed price filter
  const filterProducts = (category, size, price) => {
    let filtered = [...paginationItems];

    // Apply price filter first
    filtered = filtered.filter(item => {
      // Ensure we're working with numbers
      const itemPrice = Number(item.unitPrice);
      const minPrice = Number(price.min);
      const maxPrice = Number(price.max);
      
      // Add some logging to debug price filtering
      console.log(`Item: ${item.productName}, Price: ${itemPrice}, Range: ${minPrice}-${maxPrice}`);
      
      return itemPrice >= minPrice && itemPrice <= maxPrice;
    });

    // Apply category filter
    if (category) {
      filtered = filtered.filter(item =>
        item.categories && item.categories.some(cat =>
          cat.toLowerCase().includes(category.toLowerCase())
        )
      );
    }

    // Apply size filter
    if (size) {
      filtered = filtered.filter(item =>
        item.variety && item.variety.some(v =>
          v.size.toUpperCase() === size.toUpperCase()
        )
      );
    }

    // Log the number of items after each filter
    console.log(`Items after filtering - Price: ${filtered.length}`);
    return filtered;
  };

  // Update filters when any filter changes
  useEffect(() => {
    console.log("Price range changed:", priceRange); // Debug log
    const filtered = filterProducts(selectedCategory, selectedSize, priceRange);
    setFilteredItems(filtered);
  }, [selectedCategory, selectedSize, priceRange]);

  const handleCategorySelect = (category) => {
    setSelectedCategory(category);
  };

  const handleSizeSelect = (size) => {
    setSelectedSize(size);
  };

  const handlePriceRangeSelect = (range) => {
    // Ensure we're working with numbers
    const newRange = {
      min: Number(range.min),
      max: Number(range.max)
    };
    console.log("Setting new price range:", newRange); // Debug log
    setPriceRange(newRange);
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