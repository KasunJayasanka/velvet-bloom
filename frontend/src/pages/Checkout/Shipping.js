import React, { useState } from "react";
import axios from "axios";
import { useDispatch } from "react-redux";
import { resetCart } from "../../redux/velvetSlice";

const ShippingForm = ({ orderId, cartItems, total }) => {
  const dispatch = useDispatch();
  const [shippingDetails, setShippingDetails] = useState({
    country: "",
    city: "",
    postalCode: "",
    addressOne: "",
    addressTwo: "",
    fname: "",
    lname: "",
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setShippingDetails((prevDetails) => ({
      ...prevDetails,
      [name]: value,
    }));
  };

  const handleProceedToCheckout = async () => {

    if (!shippingDetails.country || shippingDetails.country.trim() === "") {
      alert("Country is required. Please fill in the country.");
      return;
    }

    try {
      // Step 1: Construct the payload
      const payload = {
        orderId: orderId,
        notify_url: "https://79e5-2402-4000-2201-1734-2443-ce61-49cf-8235.ngrok-free.app/payhere/notify",
        return_url: "http://localhost:3000/order-confirm",
        cancel_url: "http://localhost:3000/cart",
        shippingDetails: {
          country: shippingDetails.country,
          city: shippingDetails.city,
          postalCode: shippingDetails.postalCode,
          addressOne: shippingDetails.addressOne,
          addressTwo: shippingDetails.addressTwo,
          firstName: shippingDetails.fname,
          lastName: shippingDetails.lname,
        },
        orderItems: cartItems.map((item) => ({
          productID: item.originalProductId,
          productName: item.name,
          size: item.size,
          color: item.color,
          quantity: item.quantity,
          price: item.price,
          image: item.image,
        })),
        totalAmount: total,
      };

      console.log("Payload for Checkout:", payload); // Debugging

      // Step 2: Send payload to the backend
      const response = await axios.post(
        `http://localhost:8080/payhere/redirect`,
        payload,
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      const { redirectUrl } = response.data;

      // Step 3: Reset cart and redirect to the payment gateway
      dispatch(resetCart());
      window.location.href = redirectUrl;
    } catch (error) {
      console.error("Error initiating checkout:", error.response?.data || error);
      alert("Failed to initiate checkout. Please try again.");
    }
  };

  return (
    <div className="space-y-4">
      <div className="flex space-x-4">
        <input
          type="text"
          name="fname"
          placeholder="First Name"
          value={shippingDetails.fname}
          onChange={handleChange}
          className="w-1/2 p-3"
        />
        <input
          type="text"
          name="lname"
          placeholder="Last Name"
          value={shippingDetails.lname}
          onChange={handleChange}
          className="w-1/2 p-3"
        />
      </div>
      <div>
        <input
          type="text"
          name="country"
          placeholder="Country"
          value={shippingDetails.country}
          onChange={handleChange}
          className="w-full p-3"
        />
      </div>
      <div>
        <input
          type="text"
          name="addressOne"
          placeholder="Address Line 1"
          value={shippingDetails.addressOne}
          onChange={handleChange}
          className="w-full p-3"
        />
      </div>
      <div>
        <input
          type="text"
          name="addressTwo"
          placeholder="Address Line 2 (Optional)"
          value={shippingDetails.addressTwo}
          onChange={handleChange}
          className="w-full p-3"
        />
      </div>
      <div>
        <input
          type="text"
          name="city"
          placeholder="City"
          value={shippingDetails.city}
          onChange={handleChange}
          className="w-full p-3"
        />
      </div>
      <div>
        <input
          type="text"
          name="postalCode"
          placeholder="Postal Code"
          value={shippingDetails.postalCode}
          onChange={handleChange}
          className="w-full p-3"
        />
      </div>
      <button
        onClick={handleProceedToCheckout}
        className="px-8 py-3 bg-purple-600 text-white rounded-lg hover:bg-purple-700 w-full"
      >
        Proceed to Checkout
      </button>
    </div>
  );
};

export default ShippingForm;
