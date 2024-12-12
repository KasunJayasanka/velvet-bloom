import React, { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { addToCart } from "../../../redux/velvetSlice";

const ProductInfo = ({ productInfo }) => {
  const dispatch = useDispatch();
  const [selectedSize, setSelectedSize] = useState("");
  const [selectedColor, setSelectedColor] = useState("");
  const [error, setError] = useState("");

  // Get current cart state
  const currentCart = useSelector((state) => state.velvet?.cart || []);

  const handleAddToCart = () => {
    if (!selectedSize) {
      setError("Please select a size before adding to the cart.");
      return;
    }

    if (!selectedColor) {
      setError("Please select a color before adding to the cart.");
      return;
    }

    if (productInfo.productCount <= 0) {
      setError("This item is currently out of stock.");
      return;
    }

    setError(""); // Clear previous errors
    const productId = productInfo.id || "undefined";

    const cartItem = {
      _id: `${productId}-${selectedSize}-${selectedColor || "default"}`,
      name: productInfo.productName,
      quantity: 1,
      image: productInfo.img,
      size: selectedSize,
      price: productInfo.price,
      color: selectedColor || null,
      originalProductId: productId,
    };

    dispatch(addToCart(cartItem));

    setSelectedSize("");
    setSelectedColor("");
    setError("");
  };

  // Parse available sizes
  const availableSizes = (() => {
    if (!productInfo.Size) return [];

    if (typeof productInfo.Size === "string") {
      return productInfo.Size.split(",").map((size) => size.trim());
    }

    return Array.isArray(productInfo.Size) ? productInfo.Size : [];
  })();

  // Parse unique colors
  const uniqueColors = productInfo.color
    ? Array.from(new Set(productInfo.color.split(",").map((color) => color.trim())))
    : [];

  // Disable button if conditions are not met
  const isAddToCartDisabled =
    !selectedSize || !selectedColor || productInfo.productCount <= 0;

  return (
    <div className="flex flex-col gap-10 bg-softpurple">
      <h2 className="text-4xl font-semibold">{productInfo.productName}</h2>
      <p className="text-xl font-semibold">${productInfo.price}</p>

      {/* Size Selection */}
      <div>
        <p className="font-medium text-lg mb-2">Select Size:</p>

        {availableSizes.length === 0 && (
          <p className="text-red-500">No sizes available</p>
        )}

        {availableSizes.length > 0 && (
          <select
            value={selectedSize}
            onChange={(e) => setSelectedSize(e.target.value)}
            className="w-full px-4 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-primeColor"
          >
            <option value="">Select Size</option>
            {availableSizes.map((size) => (
              <option key={size} value={size}>
                {size}
              </option>
            ))}
          </select>
        )}
      </div>

      {/* Color Selection */}
      {uniqueColors.length > 0 && (
        <div>
          <p className="font-medium text-lg mb-2">Select Color:</p>
          <div className="flex gap-2">
            {uniqueColors.map((color) => (
              <button
                key={color}
                onClick={() => setSelectedColor(color)}
                className={`
                  w-8 h-8 rounded-full border-2 transition-all duration-300
                  ${selectedColor === color 
                    ? 'ring-2 ring-primeColor scale-110' 
                    : 'hover:border-primeColor'}
                `}
                style={{
                  backgroundColor: color.toLowerCase(),
                  borderColor: selectedColor === color ? color : "#E5E7EB",
                }}
                title={color}
              />
            ))}
          </div>
        </div>
      )}

      <p className="text-s">{productInfo.productCount} left</p>

      {/* Error Display */}
      {error && <div className="text-red-500 text-sm mt-2">{error}</div>}

      {/* Add to Cart Button */}
      <button
        onClick={handleAddToCart}
        disabled={isAddToCartDisabled}
        className={`w-full py-4 text-center text-purple-700 text-lg font-titleFont
          ${
            isAddToCartDisabled
              ? "bg-gray-300 cursor-not-allowed"
              : "bg-purple-300 hover:bg-purple-400"
          }
          rounded-full duration-300`}
      >
        Add to Cart
      </button>
    </div>
  );
};

export default ProductInfo;