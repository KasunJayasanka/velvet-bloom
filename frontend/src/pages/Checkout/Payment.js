import React, { useState } from "react";
import { Link } from "react-router-dom";

const PaymentForm = () => {
  const [paymentMethod, setPaymentMethod] = useState("card");

  return (
    <div>
          <h3 className="text-lg font-semibold mb-4">Payment Method</h3>
          <div>
            {/* Cash on Delivery */}
            <div className="flex items-center mb-4">
              <input
                type="radio"
                name="paymentMethod"
                value="cash"
                checked={paymentMethod === "cash"}
                onChange={() => setPaymentMethod("cash")}
                className="h-4 w-4 text-purple-600"
              />
              <label className="ml-2 text-sm">Cash On Delivery</label>
            </div>
            {/* Pay with Card */}
            <div className="flex items-center">
              <input
                type="radio"
                name="paymentMethod"
                value="card"
                checked={paymentMethod === "card"}
                onChange={() => setPaymentMethod("card")}
                className="h-4 w-4 text-purple-600"
              />
              <label className="ml-2 text-sm">Pay with Card</label>
            </div>
          </div>

          {paymentMethod === "card" && (
            <div className="mt-6">
              <div className="space-y-4">
                <div>
                  <label className="block text-xs font-medium text-gray-700">Card number</label>
                  <input
                    type="text"
                    placeholder="1234 5678 9101 3456"
                    className="w-full p-3 mt-2 border border-gray-300 rounded-md text-sm"
                  />
                </div>
                <div>
                  <label className="block text-xs font-medium text-gray-700">Name On Card</label>
                  <input
                    type="text"
                    className="w-full p-3 mt-2 border border-gray-300 rounded-md text-sm"
                  />
                </div>
                <div className="flex space-x-4">
                  <div className="w-1/2">
                    <label className="block text-xs font-medium text-gray-700">Expiration Date</label>
                    <input
                      type="text"
                      placeholder="MM/YY"
                      className="w-full p-3 mt-2 border border-gray-300 rounded-md text-sm"
                    />
                  </div>
                  <div className="w-1/2">
                    <label className="block text-xs font-medium text-gray-700">Card Security Code</label>
                    <input
                      type="password"
                      placeholder="***"
                      className="w-full p-3 mt-2 border border-gray-300 rounded-md text-sm"
                    />
                  </div>
                </div>
              </div>

            </div>
          )}
          <div>
         <Link
          to={{
            pathname: "/OrderConfirmation",
          }}
        >
          <button className="px-8 py-3 bg-purple-600 text-white rounded-lg hover:bg-purple-700">
            Checkout
           </button>
        </Link>
       </div>
      </div>
  );
};

export default PaymentForm;





