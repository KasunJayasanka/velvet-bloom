import React from "react";

const orders = [
  {
    id: "#7801",
    name: "Cotton T Shirt",
    description: "Full Sleeve Zipper",
    size: "L",
    color: "#000000",
    price: "$99",
    orderDate: "Wed, 30th Oct 24",
    deliveryDate: "30th Oct 24",
    status: "Ongoing",
  },
  {
    id: "#7847",
    name: "Cotton T Shirt",
    description: "Full Sleeve Zipper",
    size: "L",
    color: "#E8E6D9",
    price: "$99",
    orderDate: "Wed, 30th Oct 24",
    deliveryDate: "30th Oct 24",
    status: "Ongoing",
  },
  {
    id: "#7847",
    name: "Cotton T Shirt",
    description: "Full Sleeve Zipper",
    size: "L",
    color: "#55C1D4",
    price: "$99",
    orderDate: "Wed, 30th Oct 24",
    deliveryDate: "30th Oct 24",
    status: "Ongoing",
  },
];

const MyOrdersAll = () => {
  return (
    <div>
      <div className="order-list">
        {orders.map((order, index) => (
          <div key={index} className="order-card">
            <div className="order-info">
              <p className="order-id">{order.id}</p>
              <div className="order-product">
                <h3>{order.name}</h3>
                <p>{order.description}</p>
              </div>
              <div className="order-meta">
                <span>Size: {order.size}</span>
                <span
                  className="color-indicator"
                  style={{ backgroundColor: order.color }}
                ></span>
              </div>
            </div>
            <div className="order-details">
              <p>Order Placed: {order.orderDate}</p>
              <p>Estimated Delivery: {order.deliveryDate}</p>
            </div>
            <div className="order-price">
              <span>{order.price}</span>
              <button className="menu-btn">...</button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default MyOrdersAll;
