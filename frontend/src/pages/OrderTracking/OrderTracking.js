import React from 'react';
import ProgressBar from './ProgressBar';
import './OrderTracking.css';

const OrderTracking = () => {
    const orderDetails = {
        id: '#7801',
        item: 'Cotton T-Shirt',
        description: 'Full Sleeve Zipper',
        size: 'L',
        color: 'black',
        price: '$99',
        deliveryAddress: 'Sasanka Thenuwara, New Kandy Rd, Sri Lanka',
        progress: [
            { label: 'Ordered', date: '26th Oct 2024', completed: true },
            { label: 'Ready To Ship', date: '30th Oct 2024', completed: true },
            { label: 'Shipped', date: '7th Nov 2024', completed: true },
            { label: 'Estimated Delivery', date: '26th Nov 2024', completed: false },
        ],
    };

    return (
        <div className="order-tracking">
            <div className="sidebar">
                <div className="divider"></div>
                <h3>My Orders</h3>
                <div className="divider"></div>
                <link
                    href="https://fonts.googleapis.com/css2?family=Abel&family=Albert+Sans:wght@400;500;700&display=swap"
                    rel="stylesheet"
                />
            </div>
            <div className="main-content">
                <h2>My Orders</h2>
                <div className="filters">
                    <p className="filter-text">Filters</p>
                    <button className="filter-button">View All</button>
                    <button className="filter-button active">Ongoing</button>
                    <button className="filter-button">Completed</button>
                    <button className="filter-button">Cancelled</button>
                </div>
                <div>

                    <div className="ongoing-order-card">

                        <div className="ongoing-order-header">
                            <div>
                                <p>ORDER ID: <strong>{orderDetails.id}</strong></p>
                            </div>
                            <div>
                                <p>DELIVER TO: <strong>{orderDetails.deliveryAddress}</strong></p>
                            </div>
                        </div>

                        <div className="ongoing-order-body">

                            <div className="item-details">
                                <p><strong>{orderDetails.item}</strong></p>
                                <p>{orderDetails.description}</p>
                                <p>Size: {orderDetails.size}</p>
                                <div
                                    className="color-indicator"
                                    style={{ backgroundColor: orderDetails.color }}
                                ></div>
                            </div>
                            <p className="price">{orderDetails.price}</p>
                        </div>

                        <ProgressBar steps={orderDetails.progress} />

                        <div className="contact-button">
                            <button>Contact Seller</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default OrderTracking;