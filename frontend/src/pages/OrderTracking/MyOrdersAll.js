import React, { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import axios from "axios";
import "./MyOrdersAll.css";

const MyOrdersAll = () => {
  const [orders, setOrders] = useState([]);
  const [currentPage, setCurrentPage] = useState(1); // Track current page
  const ordersPerPage = 3; // Number of orders per page
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  // Retrieve user info from Redux store
  const userInfo = useSelector((state) => state.velvetReducer.userInfo);
  const email = userInfo?.email;
  const token = userInfo?.token;

  const API_URL = `http://localhost:8080/orders/customer/${email}`;

  useEffect(() => {
    const fetchOrders = async () => {
      try {
        const response = await axios.get(API_URL, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        setOrders(response.data);
      } catch (err) {
        console.error("Error fetching orders:", err.response || err.message);
        setError("Failed to fetch orders. Please try again.");
      } finally {
        setLoading(false);
      }
    };

    if (email) {
      fetchOrders();
    } else {
      setError("User email not available.");
      setLoading(false);
    }
  }, [API_URL, token, email]);

  if (loading) {
    return <p>Loading...</p>;
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
    <div className="order-history">
      {currentOrders.map((order) => (
        <div key={order.id} className="order-card">
          <div className="order-header">
            <p><strong>Order ID:</strong> {order.id}</p>
            <p><strong>Order Placed:</strong> {new Date(order.orderDate).toLocaleDateString()}</p>
            <p><strong>Total:</strong> ${order.totalAmount.toFixed(2)}</p>
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

        </div>
      ))}

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

export default MyOrdersAll;
