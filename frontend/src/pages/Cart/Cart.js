import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Link, useNavigate } from "react-router-dom";
import { motion } from "framer-motion";
import Breadcrumbs from "../../components/pageProps/Breadcrumbs";
import { resetCart } from "../../redux/velvetSlice";
import { emptyCart } from "../../assets/images/index";
import ItemCard from "./ItemCard";
import axios from "axios";

const Cart = () => {
  const dispatch = useDispatch();
  const products = useSelector((state) => state.velvetReducer.products);
  const customerId = localStorage.getItem("customerID"); // Replace with correct state
  // Replace with correct state
  const navigate = useNavigate();

  const [totalAmt, setTotalAmt] = useState(0);
  const [shippingCharge, setShippingCharge] = useState(0);

  useEffect(() => {
    let price = 0;
    products.forEach((item) => {
      price += item.price * item.quantity;
    });
    setTotalAmt(price);
  }, [products]);

  useEffect(() => {
    if (totalAmt <= 200) {
      setShippingCharge(30);
    } else if (totalAmt <= 400) {
      setShippingCharge(25);
    } else {
      setShippingCharge(20);
    }
  }, [totalAmt]);

  const handleCheckout = async () => {
    if (!customerId) {
      console.error("Customer ID is undefined. Ensure the user is logged in.");
      return;
    }
  
    try {
      // Fetch customer details
      const customerResponse = await axios.get(
        `http://localhost:8080/customers/${customerId}`,
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`, // Replace with your token logic
          },
        }
      );
  
      const customer = customerResponse.data;
      const user = customer.user;
  
      // Construct the payload
      const orderDetails = {
        contactName: `${user.firstName} ${user.lastName}`, // Use customer's name
        contactMail: user.email, // Use customer's email
        contactNumber: user.mobileNo, // Use customer's mobile number
        shippingAddress: {
          street: user.address || "123 Main Street", // Use customer's address
          city: user.city || "Default City",
          state: user.country || "Default State",
          zipCode: "12345", // Add zipCode if available in your data
        },
        payMethod: "Card", // Replace with actual payment method, e.g., Card, PayPal
        deliverDate: null, // Replace with actual delivery date if applicable
        orderItems: products.map((item) => ({
          productID: item.originalProductId,
          productName: item.name,
          size: item.size,
          colors: [
            {
              color: item.color,
              count: item.quantity,
            },
          ],
          mainImgUrl: item.image, // Assuming the image URL is stored in `image`
        })),
      };
  
      console.log("Order Details Payload:", orderDetails); // Debug the payload
  
      // Send the order creation request
      const response = await axios.post(
        `http://localhost:8080/orders/${customerId}`,
        orderDetails,
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
            "Content-Type": "application/json",
          },
        }
      );
  
      navigate("/checkout", { state: { orderId: response.data.id, cartItems: products } }); // Navigate to the success page
    } catch (error) {
      console.error("Error creating order:", error.response?.data || error);
    }
  };
  

  return (
    <div className="max-w-container mx-auto px-4">
      <Breadcrumbs title="Cart" />
      {products.length > 0 ? (
        <div className="pb-20">
          <div className="w-full h-20 bg-[#F5F7F7] text-primeColor hidden lgl:grid grid-cols-8 place-content-center px-6 text-lg font-titleFont font-semibold">
            <h2 className="col-span-3">Product</h2>
            <h2>Size</h2>
            <h2>Color</h2>
            <h2>Price</h2>
            <h2>Quantity</h2>
            <h2>Sub Total</h2>
          </div>
          <div className="mt-6">
            {products.map((item) => (
              <div key={item._id}>
                <ItemCard item={item} />
              </div>
            ))}
          </div>

          <button
            onClick={() => dispatch(resetCart())}
            className="py-2 px-10 bg-red-500 text-white font-semibold uppercase mb-4 hover:bg-red-700 duration-300"
          >
            Reset cart
          </button>

          <div className="max-w-7xl gap-4 flex justify-end mt-4">
            <div className="w-96 flex flex-col gap-4">
              <h1 className="text-2xl font-semibold text-right">Cart totals</h1>
              <div>
                <p className="flex items-center justify-between border-[1px] border-gray-400 border-b-0 py-1.5 text-lg px-4 font-medium">
                  Subtotal
                  <span className="font-semibold tracking-wide font-titleFont">
                    ${totalAmt}
                  </span>
                </p>
                <p className="flex items-center justify-between border-[1px] border-gray-400 border-b-0 py-1.5 text-lg px-4 font-medium">
                  Shipping Charge
                  <span className="font-semibold tracking-wide font-titleFont">
                    ${shippingCharge}
                  </span>
                </p>
                <p className="flex items-center justify-between border-[1px] border-gray-400 py-1.5 text-lg px-4 font-medium">
                  Total
                  <span className="font-bold tracking-wide text-lg font-titleFont">
                    ${totalAmt + shippingCharge}
                  </span>
                </p>
              </div>
              <div className="flex justify-end">
                <button
                  onClick={handleCheckout}
                  className="w-52 h-10 bg-primeColor text-white hover:bg-black duration-300"
                >
                  Proceed to Checkout
                </button>
              </div>
            </div>
          </div>
        </div>
      ) : (
        <motion.div
          initial={{ y: 30, opacity: 0 }}
          animate={{ y: 0, opacity: 1 }}
          transition={{ duration: 0.4 }}
          className="flex flex-col mdl:flex-row justify-center items-center gap-4 pb-20"
        >
          <div>
            <img
              className="w-80 rounded-lg p-4 mx-auto"
              src={emptyCart}
              alt="emptyCart"
            />
          </div>
          <div className="max-w-[500px] p-4 py-8 bg-white flex gap-4 flex-col items-center rounded-md shadow-lg">
            <h1 className="font-titleFont text-xl font-bold uppercase">
              Your Cart feels lonely.
            </h1>
            <p className="text-sm text-center px-10 -mt-2">
              Your Shopping cart lives to serve. Give it purpose - fill it with
              books, electronics, videos, etc. and make it happy.
            </p>
            <Link to="/shop">
              <button className="bg-primeColor rounded-md cursor-pointer hover:bg-black active:bg-gray-900 px-8 py-2 font-titleFont font-semibold text-lg text-gray-200 hover:text-white duration-300">
                Continue Shopping
              </button>
            </Link>
          </div>
        </motion.div>
      )}
    </div>
  );
};

export default Cart;
