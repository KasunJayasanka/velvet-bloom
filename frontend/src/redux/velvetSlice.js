import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  userInfo: [],
  products: [], 
  cart:[],
  isLoggedIn: false,
};

export const velvetSlice = createSlice({
  name: "velvet",
  initialState,
  reducers: {
    setLoggedIn: (state, action) => {
      state.isLoggedIn = true;
      state.userInfo = action.payload; // Store user info if needed
    },
    logout: (state) => {
      state.isLoggedIn = false;
      state.userInfo = null;
      state.cart = [];
    },
    addToCart: (state, action) => {
      const item = state.products.find(
        (item) => item._id === action.payload._id
      );
      if (item) {
        item.quantity += action.payload.quantity;
      } else {
        state.products.push(action.payload);
      }
    },
    increaseQuantity: (state, action) => {
      const item = state.products.find(
        (item) => item._id === action.payload._id
      );
      if (item) {
        item.quantity++;
      }
    },
    decreaseQuantity: (state, action) => {
      const item = state.products.find(
        (item) => item._id === action.payload._id
      );
      if (item.quantity === 1) {
        item.quantity = 1;
      } else {
        item.quantity--;
      }
    },
    deleteItem: (state, action) => {
      state.products = state.products.filter(
        (item) => item._id !== action.payload
      );
    },
    resetCart: (state) => {
      state.products = [];
    },
  },
});

export const {
  setLoggedIn,
  logout,
  addToCart,
  increaseQuantity,
  decreaseQuantity,  
  deleteItem,
  resetCart,
} = velvetSlice.actions;

export default velvetSlice.reducer;