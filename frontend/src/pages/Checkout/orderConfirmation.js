import React, { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import Breadcrumbs from "../../components/pageProps/Breadcrumbs";
import { Link } from "react-router-dom";

const OrderConfirmation = () => {
  const location = useLocation();
  const [prevLocation, setPrevLocation] = useState("");
  // const cartItems = location.state?.cartItems || []; 

  useEffect(() => {
    setPrevLocation(location.state?.data || "Checkout");
  }, [location]);

  return (
    <div className="min-h-screen bg-purple-50  px-10">
      <Breadcrumbs title="Order Confirmation" prevLocation={prevLocation} />
      <div className="max-w-6xl mx-auto">
        <div className="text-center mb-10">
          <h1 className="text-3xl font-bold text-gray-800">THANK YOU FOR YOUR PURCHASE!</h1>
          <p className="mt-4 text-lg text-gray-600 font-bold">Hi Jhone,</p>
          <p className="mt-2 text-gray-500">
            Thank you for shopping with us! We’ve received your order. We will notify you when we send it.
          </p>
        </div>
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-10">
          {/* Shipping Address Section */}
          <div className="bg-white shadow-md rounded-md p-20">
            <h2 className="text-xl font-semibold text-gray-800 mb-6">Shipping Address</h2>
            <div className="text-gray-600">
              <p><strong>Name:</strong> Jhone Smith</p>
              <p><strong>Address:</strong> 50, Galle Road, Dehiwala</p>
              <p><strong>Phone:</strong> 077 056 1245</p>
              <p><strong>Email:</strong> jhone23@gmail.com</p>
            </div>
          </div>

          {/* Order Summary Section */}
          <div className="bg-white shadow-md rounded-md p-6">
            <h2 className="text-xl font-semibold text-gray-800 mb-4">Your Order</h2>
            <div className="space-y-4">

              <div className="flex items-start space-x-4">
                <img src="https://thehouseofrare.com/cdn/shop/files/masco-mens-t-shirt-black27624_a0fa7484-fb65-4d22-8857-e1676fa3f5c6.jpg?v=1719483725" alt="Item 1" className="w-20 h-20 object-cover" />
                <div className="flex-1">
                  <p className="font-medium text-left">Basic Heavy T-Shirt</p>
                  <p className="text-gray-600 text-left">Black/L</p>
                  <p className="text-gray-600 text-left">(1)</p>
                </div>
                <div className="text-right">
                  <p className="font-medium text-gray-700">$99</p>
                  <a href="/" className="text-blue-500 text-sm">Change</a>
                </div>
              </div>

              <div className="flex items-start space-x-4">
                <img src="https://juvia-b2c-cloud-production.imgix.net/product/202204/0/juvia-t-shirt-110-front-00917982d1.jpg?auto=format&bg=%23f2f1f0&w=2000" alt="Item 2" className="w-20 h-20 object-cover" />
                <div className="flex-1">
                  <p className="font-medium text-left">Basic Fit T-Shirt</p>
                  <p className="text-gray-600 text-left">Black/L</p>
                  <p className="text-gray-600 text-left">(1)</p>
                </div>
                <div className="text-right">
                  <p className="font-medium text-gray-700">$99</p>
                  <a href="/" className="text-blue-500 text-sm">Change</a>
                </div>
              </div>
            </div>

            <div className="mt-4 border-t border-gray-300 pt-4 space-y-2">
              <div className="flex justify-between text-gray-700">
                <span>Subtotal</span>
                <span>$180.00</span>
              </div>
              <div className="flex justify-between text-gray-700">
                <span>Shipping</span>
                <span>Calculated at next step</span>
              </div>
              <div className="flex justify-between font-bold text-lg mt-2">
                <span>Total</span>
                <span>$180.00</span>
              </div>
            </div>
          </div>
        </div>

        {/* Track Order Button */}
        <div className="mt-10 text-center">
          <Link to="/OrderTracking">
            <button className="px-8 py-3 bg-purple-600 text-white rounded-lg hover:bg-purple-700">
              Track Your Order
            </button>
          </Link>
        </div>
      </div>
    </div>
  );
};

export default OrderConfirmation;


