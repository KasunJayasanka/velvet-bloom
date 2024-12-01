import React, { useState } from "react";

const PaymentForm = ({ formData, setFormData, handleCheckout }) => {
  const [paymentMethod, setPaymentMethod] = useState(formData.paymentMethod || "card");

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  return (
    <div className="space-y-4">
      {/* Payment Method Options */}
      <div className="flex items-center">
        <input
          type="radio"
          name="paymentMethod"
          value="cash"
          checked={paymentMethod === "cash"}
          onChange={() => setPaymentMethod("cash")}
        />
        <label className="ml-2">Cash on Delivery</label>
      </div>
      <div className="flex items-center">
        <input
          type="radio"
          name="paymentMethod"
          value="card"
          checked={paymentMethod === "card"}
          onChange={() => setPaymentMethod("card")}
        />
        <label className="ml-2">Pay with Card</label>
      </div>

      {/* If 'Pay with Card' is selected, show card details fields */}
      {paymentMethod === "card" && (
        <div className="space-y-4">
          <input
            type="text"
            name="cardNumber"
            placeholder="Card Number"
            onChange={handleInputChange}
            className="w-full p-3 mt-2 border border-gray-300 rounded-md"
          />
          <input
            type="text"
            name="cardName"
            placeholder="Name on Card"
            onChange={handleInputChange}
            className="w-full p-3 mt-2 border border-gray-300 rounded-md"
          />
          <div className="flex space-x-4">
            <input
              type="text"
              name="expiry"
              placeholder="MM/YY"
              onChange={handleInputChange}
              className="w-1/2 p-3 mt-2 border border-gray-300 rounded-md"
            />
            <input
              type="text"
              name="cvv"
              placeholder="CVV"
              onChange={handleInputChange}
              className="w-1/2 p-3 mt-2 border border-gray-300 rounded-md"
            />
          </div>
        </div>
      )}

      {/* Checkout Button */}
      <button
        onClick={handleCheckout}
        className="w-full py-3 bg-purple-600 text-white rounded-lg hover:bg-purple-700 mt-4"
      >
        Checkout
      </button>
    </div>
  );
};

export default PaymentForm;
