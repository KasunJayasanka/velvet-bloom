import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  userInfo: [],
  products: [], 
  cart: []  
};

export const velvetSlice = createSlice({
  name: "velvet",
  initialState,
  reducers: {
    addToCart: (state, action) => {
      // Find if an exact match exists (including unique ID)
      const existingItemIndex = state.cart.findIndex(
        (item) => item._id === action.payload._id
      );
      
      if (existingItemIndex > -1) {
        // If exact item exists, increment quantity
        state.cart[existingItemIndex].quantity += 1;
      } else {
        // If no match, add as new item
        state.cart.push(action.payload);
      }
    },
    increaseQuantity: (state, action) => {
      // Update to work with cart instead of products
      const itemIndex = state.cart.findIndex(
        (item) => item._id === action.payload._id
      );
      if (itemIndex > -1) {
        state.cart[itemIndex].quantity++;
      }
    },
    decreaseQuantity: (state, action) => {

      // Update to work with cart instead of products
      const itemIndex = state.cart.findIndex(
        (item) => item._id === action.payload._id
      );
      if (itemIndex > -1) {
        if (state.cart[itemIndex].quantity > 1) {
          state.cart[itemIndex].quantity--;
        }
      }
    },
    deleteItem: (state, action) => {
      // Update to work with cart instead of products
      state.cart = state.cart.filter(
        (item) => item._id !== action.payload
      );
    },
    resetCart: (state) => {
      state.cart = [];
    },
  },
});

export const {
  addToCart,
  increaseQuantity,
  decreaseQuantity,  
  deleteItem,
  resetCart,
} = velvetSlice.actions;

export default velvetSlice.reducer;