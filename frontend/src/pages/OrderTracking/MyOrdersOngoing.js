import React from "react";
import './MyOrdersOngoing.css';
import { Link } from "react-router-dom";

const orders = [
  {
    id: "#7801",
    image: "./Michelle Blazer - Oversized Plunge Neck Button Up Blazer in White.jpg",
    name: "Cotton T Shirt",
    description: "Full Sleeve Zipper",
    size: "L",
    color: "Black",
    quantity: 1,
    price: "$99",
    orderDate: "Wed, 26th Oct 24",
    deliveryDate: "10th Nov 2024",
    status: "Shipped",
  },
  {
    id: "#7802",
    image: "image2-url",
    name: "Cotton T Shirt",
    description: "Basic Slim Fit T-Shirt",
    size: "L",
    color: "Black",
    quantity: 1,
    price: "$99",
    orderDate: "Wed, 30th Oct 24",
    deliveryDate: "15th Nov 2024",
    status: "Ready To Ship",
  },
];

const OrderCard = ({ order }) => (
  <div key={order.id} className="order-card">
    <div className="order-image">
      <img src={order.image} alt={order.name} />
    </div>
    <div className="order-details">
      <div className="order-header">
        <p>ORDER ID: {order.id}</p>
        <p>Order Placed: {order.orderDate}</p>
      </div>
      <h3>{order.name}</h3>
      <p>{order.description}</p>
      <div className="order-info">
        <p>Size: {order.size}</p>
        <p>Color: {order.color}</p>
        <p>Quantity: {order.quantity}</p>
        <p>Price: {order.price}</p>
      </div>
      <p>ESTIMATED DELIVERY: {order.deliveryDate}</p>
      <p>Status: {order.status}</p>
      <Link to="/orderTracking" className="track-order-button">Track My Order</Link>  {/* Link to OrderTracking page */}
    </div>
  </div>
);


const MyOrdersAll = () => {
  return (
    <div className = "viewall">
<div className="order-list">
          {orders.map((order) => (
            <OrderCard order={order} key={order.id} />
          ))}
        </div>
        </div>
  );
};

export default MyOrdersAll;
