import { createSlice } from '@reduxjs/toolkit';

const initialState = {
  information: {
    fullName: '',
    email: '',
    phoneNumber: '',
  },
  shipping: {
    firstName: '',
    lastName: '',
    country: '',
    state: '',
    address: '',
    city: '',
    postalCode: '',
  },
  payment: {
    method: '', // 'cashOnDelivery' or 'card'
    cardDetails: {
      cardNumber: '',
      nameOnCard: '',
      expirationDate: '',
      securityCode: '',
    },
  },
};

const checkoutSlice = createSlice({
  name: 'checkout',
  initialState,
  reducers: {
    setInformation(state, action) {
      state.information = action.payload;
    },
    setShipping(state, action) {
      state.shipping = action.payload;
    },
    setPayment(state, action) {
      state.payment = action.payload;
    },
    clearCheckout(state) {
      return initialState;
    },
  },
});

export const { setInformation, setShipping, setPayment, clearCheckout } = checkoutSlice.actions;
export default checkoutSlice.reducer;
