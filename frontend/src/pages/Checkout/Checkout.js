import React, { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import Breadcrumbs from "../../components/pageProps/Breadcrumbs";
import ShippingForm from "./Shipping";
import InformationForm from "./Information";
import PaymentForm from "./Payment";
import axios from "axios";

const Checkout = () => {
  const [activeTab, setActiveTab] = useState("information");
  const [formData, setFormData] = useState({
    fullName: "",
    email: "",
    phone: "",
    shipping: {
      firstName: "",
      lastName: "",
      country: "",
      state: "",
      address: "",
      city: "",
      postalCode: "",
    },
    paymentMethod: "card",
    cardNumber: "",
    cardName: "",
    expiry: "",
    cvv: "",
  });

  const location = useLocation();
  const cartItems = location.state?.cartItems || [];
  const shippingCost = 30; // Fixed shipping cost for now
  const subtotal = cartItems.reduce(
    (acc, item) => acc + item.price * item.quantity,
    0
  );
  const total = subtotal + shippingCost;

  const navigate = useNavigate();

  const handleCheckout = async () => {
    try {
      const data = {
        userData: formData,
        cartItems: cartItems,
        paymentMethod: formData.paymentMethod,
      };
  
      console.log('Data being sent to backend:', data); // Log data here
  
      const response = await axios.post(
        "http://localhost:8080/carts/673dfe5d2096f953e47ab8e3/checkout",
        data
      );
  
      if (response.status === 200) {
        navigate("/order-confirmation");
      } else {
        console.error("Checkout failed:", response);
      }
    } catch (error) {
      console.error("Error during checkout:", error);
    }
  };
  
  return (
    <div className="min-h-screen bg-purple-50 px-12">
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

          {activeTab === "information" && (
            <InformationForm formData={formData} setFormData={setFormData} />
          )}
          {activeTab === "shipping" && (
            <ShippingForm formData={formData} setFormData={setFormData} />
          )}
          {activeTab === "payment" && (
            <PaymentForm
              formData={formData}
              setFormData={setFormData}
              handleCheckout={handleCheckout}
            />
          )}
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
                      <p className="text-gray-600 text-left">
                        Qty: {item.quantity}
                      </p>
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
                <div className="flex justify-between mb-2">
                  <p className="font-medium">Subtotal:</p>
                  <p className="font-medium">${subtotal}</p>
                </div>
                <div className="flex justify-between mb-2">
                  <p className="font-medium">Shipping:</p>
                  <p className="font-medium">${shippingCost}</p>
                </div>
                <div className="flex justify-between mt-4 mb-6">
                  <p className="font-semibold">Total:</p>
                  <p className="font-semibold">${total}</p>
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
