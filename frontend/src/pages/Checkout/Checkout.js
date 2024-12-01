import React, { useState } from "react";
import { useLocation } from "react-router-dom";
import Breadcrumbs from "../../components/pageProps/Breadcrumbs";
import ShippingForm from "./Shipping";
import InformationForm from "./Information";
import PaymentForm from "./Payment";

const Checkout = () => {
  const [activeTab, setActiveTab] = useState("information");
  const location = useLocation();
  const cartItems = location.state?.cartItems || [];
  const shippingCost = 30; 
  const subtotal = cartItems.reduce(
    (acc, item) => acc + item.price * item.quantity,
    0
  );
  const total = subtotal + shippingCost; 

  return (
    <div className="min-h-screen bg-purple-50  px-12">
      <Breadcrumbs title="Checkout" prevLocation="Cart" />
      <main className="container mx-auto flex flex-col lg:flex-row gap-48">
        {/* Left Section */}
        <section className="md:w-2/5">
          <div className="flex space-x-16 mb-10">
            <button
              className={`font-semibold ${
                activeTab === "information" ? "text-black" : "text-gray-400"
              }`}
              onClick={() => setActiveTab("information")}
            >
              INFORMATION
            </button>
            <button
              className={`font-semibold ${
                activeTab === "shipping" ? "text-black" : "text-gray-400"
              }`}
              onClick={() => setActiveTab("shipping")}
            >
              SHIPPING
            </button>
            <button
              className={`font-semibold ${
                activeTab === "payment" ? "text-black" : "text-gray-400"
              }`}
              onClick={() => setActiveTab("payment")}
            >
              PAYMENT
            </button>
          </div>
          {activeTab === "information" && <InformationForm />}
          {activeTab === "shipping" && <ShippingForm />}
          {activeTab === "payment" && <PaymentForm />}
        </section>

        {/* Right Section: Your Order */}
        <div className="flex justify-center">
          <section className="w-full max-w-lg mt-1 p-12 border border-black bg-purple-50 rounded-md">
            <h3 className="text-lg font-semibold mb-6 text-left">YOUR ORDER</h3>
            <div className="space-y-6">
              {cartItems.length > 0 ? (
                cartItems.map((item) => (
                  <div key={item._id} className="flex items-start space-x-10">
                    <img
                      src={item.image}
                      alt={item.name}
                      className="w-16 h-16 object-cover rounded-md"
                    />
                    <div className="flex-1">
                      <h4 className="font-medium text-left">{item.name}</h4>
                      <p className="text-gray-600 text-left">
                        Size: {item.size} | Color: {item.color || "Default"}
                      </p>
                      <p className="text-gray-600 text-left">Qty: {item.quantity}</p>
                    </div>
                    <p className="font-medium text-gray-700">${item.price}</p>
                  </div>
                ))
              ) : (
                <p>No items in the cart</p>
              )}
            </div>
            {cartItems.length > 0 && (
              <div className="border-t border-gray-300 mt-4 pt-4">
                <div className="flex justify-between text-gray-700">
                  <span>Subtotal</span>
                  <span>${subtotal.toFixed(2)}</span>
                </div>
                <div className="flex justify-between text-gray-700">
                  <span>Shipping</span>
                  <span>${shippingCost.toFixed(2)}</span>
                </div>
                <div className="flex justify-between text-gray-900 font-bold mt-2">
                  <span>Total</span>
                  <span>${total.toFixed(2)}</span>
                </div>
              </div>
            )}
          </section>
        </div>
      </main>
    </div>
  );
};

export default Checkout;
