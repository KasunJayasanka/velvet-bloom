import React, { useState, useEffect } from 'react';
import { Link, Outlet, useLocation } from 'react-router-dom';

import './MyOrdersAll.css';
import Header from '../../components/home/Header/Header';
import Footer from '../../components/home/Footer/Footer';
import FooterBottom from '../../components/home/Footer/FooterBottom';

const MyOrders = () => {
    const [showAllOrders, setShowAllOrders] = useState(true);  // Default to show all orders
    const location = useLocation();

    // Update the state based on the current route
    useEffect(() => {
        if (location.pathname === '/MyOrders/view-all') {
            setShowAllOrders(true);
        } else if (location.pathname === '/MyOrders/ongoing') {
            setShowAllOrders(false);
        }
    }, [location]);

    // Button activation logic
    const isViewAllActive = location.pathname === '/MyOrders/view-all';
    const isOngoingActive = location.pathname === '/MyOrders/ongoing';

    return (
        <div className="my-orders-page">
            <Header />
            <div className="order-history">
                <div>
                    <link
                        href="https://fonts.googleapis.com/css2?family=Abel&family=Albert+Sans:wght@400;500;700&display=swap"
                        rel="stylesheet"
                    />
                </div>
                <div>
                    <section className="title-header">
                        <h1>My Orders</h1>
                        {/* Dynamically update breadcrumb text */}
                        <p>MyOrders &gt; {isViewAllActive ? 'View-All' : isOngoingActive ? 'Ongoing' : ''}</p>
                    </section>
                    <div className="filtersOrder">
                        <p className="filterOrder-text">Filters</p>
                        {/* View All button */}
                        <Link
                            to="/MyOrders/view-all"
                            className={`filterOrder-btn ${isViewAllActive ? 'active' : ''}`}
                            onClick={() => setShowAllOrders(true)}
                        >
                            View All
                        </Link>
                        {/* Ongoing button */}
                        <Link
                            to="/MyOrders/ongoing"
                            className={`filterOrder-btn ${isOngoingActive ? 'active' : ''}`}
                            onClick={() => setShowAllOrders(false)}
                        >
                            Ongoing
                        </Link>
                    </div>

                    <Outlet />
                </div>
            </div>

            <Footer />
            <FooterBottom />
        </div>
    );
};

export default MyOrders;
