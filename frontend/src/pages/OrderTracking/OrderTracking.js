import React, { useEffect, useState } from "react";
import ProgressBar from './ProgressBar';
import './OrderTracking.css';

import Header from '../../components/home/Header/Header';
import Footer from '../../components/home/Footer/Footer';
import FooterBottom from '../../components/home/Footer/FooterBottom';
import { useParams } from "react-router-dom";
import axios from "axios";

const OrderTracking = () => {
    const [orderDetails, setOrderDetails] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
    const { orderId } = useParams(); // Get order ID from route params
    const token = localStorage.getItem("token"); // Retrieve token from localStorage

    useEffect(() => {
        const fetchOrderDetails = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/orders/${orderId}`, {
                    headers: { Authorization: `Bearer ${token}` },
                });
                setOrderDetails(response.data);
            } catch (err) {
                console.error("Error fetching order details:", err.response || err.message);
                setError("Failed to fetch order details. Please try again.");
            } finally {
                setLoading(false);
            }
        };

        fetchOrderDetails();
    }, [orderId, token]);

    if (loading) {
        return <p>Loading order details...</p>;
    }

    if (error) {
        return <p className="error">{error}</p>;
    }

    return (
        <div>
            <Header />
            <div className="tracking-content">
                <section className="title-header">
                    <h1>My Orders</h1>
                    <p>MyOrders &gt; Ongoing &gt; Track My Order</p>
                </section>
                <div className="ongoing-order-card">
                    <div className="ongoing-order-header">
                        <div>
                            <p>ORDER ID: <strong>{orderDetails.id}</strong></p>
                        </div>
                        <div>
                            <p>DELIVER TO: <strong>{orderDetails.shippingAddress.addressOne}, {orderDetails.shippingAddress.city}, {orderDetails.shippingAddress.country}</strong></p>
                        </div>
                    </div>

                    <div className="ongoing-order-body">
                        <div className="item-details">
                            {orderDetails.orderItems.map((item, index) => (
                                <div key={index} className="item-card">
                                    <img src={item.mainImgUrl} alt={item.productName} className="item-image" />
                                    <div className="item-info">
                                        <p><strong>{item.productName}</strong></p>
                                        <p>{item.size}</p>
                                        <p>Color: <span style={{ backgroundColor: item.colors[0]?.color || "#ccc" }} className="color-indicator"></span></p>
                                    </div>
                                </div>
                            ))}
                        </div>
                        <p className="price">Total: ${orderDetails.totalAmount}</p>
                    </div>

                    <ProgressBar
                        steps={[
                            { label: 'Ordered', date: new Date(orderDetails.orderDate).toLocaleDateString(), completed: true },
                            { label: 'Ready To Ship', date: '2th Dec 2024', completed: orderDetails.status === "Ready To Ship" || orderDetails.status === "shipped" },
                            // { label: 'Shipped', date: '7th Nov 2024', completed: orderDetails.status === "shipped" },
                            { label: 'Estimated Delivery', date: new Date(orderDetails.deliverDate).toLocaleDateString(), completed: false },
                        ]}
                    />
                </div>
            </div>
            <Footer />
            <FooterBottom />
        </div>
    );
};

export default OrderTracking;
