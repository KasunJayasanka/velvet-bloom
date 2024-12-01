import React from "react";
import ProgressBar from './ProgressBar';
import './OrderTracking.css';

import Header from '../../components/home/Header/Header';
import Footer from '../../components/home/Footer/Footer';
import FooterBottom from '../../components/home/Footer/FooterBottom';

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
        <div >
            <Header />
            <div className="tracking-content">
                <section className="title-header">
                    <h1>My Orders</h1>
                    <p>MyOrders &gt; Ongoing &gt; Track My Order </p>
                </section>
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

                    {/* <div className="contact-button">
                        <button>Contact Seller</button>
                    </div> */}
                </div>
            </div>
            <Footer />
            <FooterBottom />

        </div>
    );
};

export default OrderTracking;
