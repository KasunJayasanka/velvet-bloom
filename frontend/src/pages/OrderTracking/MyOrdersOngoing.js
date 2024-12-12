import React, { useEffect, useState } from "react";
import './MyOrdersOngoing.css';
import { Link } from "react-router-dom";
import { useSelector } from "react-redux";
import axios from "axios";

const OrderCard = ({ order }) => (
  <div className="order-card">
    <div className="order-header">
      <p><strong>Order ID:</strong> {order.id}</p>
      <p><strong>Total:</strong> ${order.totalAmount}</p>
      <p><strong>Order Placed:</strong> {new Date(order.orderDate).toLocaleDateString()}</p>
      <p><strong>Estimated Delivery:</strong> {new Date(order.deliverDate).toLocaleDateString()}</p>
    </div>
    <div className="order-items-grid">
      {order.orderItems.map((item, index) => (
        <div key={index} className="order-item">
          <div className="item-image">
            <img src={item.mainImgUrl || "default-image-url"} alt={item.productName || "Product"} />
          </div>
          <div className="item-info">
            <h3>{item.productName}</h3>
            <p>Size: {item.size}</p>
            <div className="color-row">
              <strong>Colors:</strong>
              {item.colors.map((colorObj, colorIndex) => (
                <span
                  key={colorIndex}
                  className="color-indicator"
                  style={{
                    backgroundColor: colorObj.color || "#ccc",
                  }}
                ></span>
              ))}
            </div>
          </div>
        </div>
      ))}
    </div>
    <Link to={`/orderTracking/${order.id}`} className="track-order-button">Track My Order</Link>
  </div>
);

const MyOrdersOngoing = () => {
  const [orders, setOrders] = useState([]);
  const [currentPage, setCurrentPage] = useState(1); // Track current page
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const ordersPerPage = 3; // Set limit to `s` orders per page

  // Retrieve user info from Redux store
  const userInfo = useSelector((state) => state.velvetReducer.userInfo);
  const email = userInfo?.email;
  const token = userInfo?.token;

  const API_URL = `http://localhost:8080/orders/customer/${email}`;

  useEffect(() => {
    const fetchOrders = async () => {
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get(API_URL, {
          headers: { Authorization: `Bearer ${token}` },
        });
        const allOrders = response.data;

        // Filter ongoing orders based on their status
        const ongoingOrders = allOrders.filter(
          (order) =>
            order.status === "Ready To Ship" ||
            order.status === "new"
        );

        setOrders(ongoingOrders);
      } catch (err) {
        console.error("Error fetching orders:", err);
        setError("Failed to fetch orders. Please try again.");
      } finally {
        setLoading(false);
      }
    };

    fetchOrders();
  }, []);

  if (loading) {
    return <p>Loading ongoing orders...</p>;
  }

  if (error) {
    return <p className="error">{error}</p>;
  }

  // Pagination logic
  const indexOfLastOrder = currentPage * ordersPerPage;
  const indexOfFirstOrder = indexOfLastOrder - ordersPerPage;
  const currentOrders = orders.slice(indexOfFirstOrder, indexOfLastOrder);

  const totalPages = Math.ceil(orders.length / ordersPerPage);

  const handleNextPage = () => {
    if (currentPage < totalPages) {
      setCurrentPage((prevPage) => prevPage + 1);
    }
  };

  const handlePrevPage = () => {
    if (currentPage > 1) {
      setCurrentPage((prevPage) => prevPage - 1);
    }
  };

  return (
    <div className="viewall">
      <h1>Ongoing Orders</h1>
      <div className="order-list">
        {currentOrders.length > 0 ? (
          currentOrders.map((order) => <OrderCard order={order} key={order.id} />)
        ) : (
          <p>No ongoing orders found.</p>
        )}
      </div>
      {/* Pagination Controls */}
      <div className="pagination">
        <button
          onClick={handlePrevPage}
          disabled={currentPage === 1}
          className="pagination-btn"
        >
          Previous
        </button>
        <span>
          Page {currentPage} of {totalPages}
        </span>
        <button
          onClick={handleNextPage}
          disabled={currentPage === totalPages}
          className="pagination-btn"
        >
          Next
        </button>
      </div>
    </div>
  );
};

export default MyOrdersOngoing;
