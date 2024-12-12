import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import Breadcrumbs from "../../components/pageProps/Breadcrumbs";
import Header from "../../components/home/Header/Header";
import Footer from "../../components/home/Footer/Footer";
import FooterBottom from "../../components/home/Footer/FooterBottom";
import { FaUser } from "react-icons/fa";
import axios from "axios";
import "./Profile.css";

const Profile = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const [prevLocation, setPrevLocation] = useState("");
  const [customerData, setCustomerData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const customerId = localStorage.getItem("customerID");
  const API_URL = `http://localhost:8080/customers/${customerId}`;
  const token = localStorage.getItem("token");

  useEffect(() => {
    setPrevLocation(location.state?.data || ""); // Fallback if location.state is undefined

    // Fetch customer details
    const fetchCustomerDetails = async () => {
      try {
        console.log("Fetching customer details...");
        const response = await axios.get(API_URL, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        console.log("Response:", response.data);
        setCustomerData(response.data);
      } catch (err) {
        console.error("Error fetching customer details:", err.response || err.message);
        setError("Failed to fetch customer details. Please try again.");
      } finally {
        setLoading(false);
      }
    };

    fetchCustomerDetails();
  }, [location, API_URL, token]);

  const handleNavigate1 = () => {
    navigate("/customerdetails");
  };

  const handleNavigate2 = () => {
    navigate("/changepassword");
  };

  if (loading) {
    return <p>Loading...</p>;
  }

  if (error) {
    return <p className="error">{error}</p>;
  }

  return (
    <div>
      <Header />
      <div className="max-w-container mx-auto px-4">
        {/* Breadcrumbs Component */}
        <Breadcrumbs title="Customer Details" prevLocation={prevLocation} />

        <div className="pb-10">
          <div className="profileContainer">
            <div className="profileDetails">
              <form>
                <p className="addressTitle">ID</p>
                <div className="name">
                  <input
                    type="text"
                    className="id"
                    value={customerData.id || ""}
                    disabled
                  />
                </div>

                <p className="addressTitle">NAME</p>
                <div className="name">
                  <input
                    type="text"
                    className="fName"
                    value={customerData.user.firstName || ""}
                    disabled
                  />
                  <input
                    type="text"
                    className="lName"
                    value={customerData.user.lastName || ""}
                    disabled
                  />
                </div>
                <p className="addressTitle">EMAIL</p>
                <div className="contact">
                  <input
                    type="text"
                    className="email"
                    value={customerData.user.email || ""}
                    disabled
                  />
                </div>
                <p className="addressTitle">PHONE</p>
                <div className="contact">
                  <input
                    type="text"
                    className="mobile"
                    value={customerData.user.mobileNo || ""}
                    disabled
                  />
                </div>
                <p className="addressTitle">COUNTRY & CITY</p>
                <div className="name">
                  <input
                    type="text"
                    className="country"
                    value={customerData.user.country || ""}
                    disabled
                  />
                  <input
                    type="text"
                    className="city"
                    value={customerData.user.city || ""}
                    disabled
                  />
                </div>
                <div className="contact">
                  <p className="addressTitle">SHIPPING ADDRESS</p>
                  <input
                    type="text"
                    className="address"
                    value={customerData.user.address || ""}
                    disabled
                  />
                </div>
                <div className="contact">
                  <input
                    type="text"
                    className="address"
                    value={customerData.user.secondaryAddress || ""}
                    disabled
                  />
                </div>
                <button
                  onClick={handleNavigate1}
                  type="button"
                  className="Editbutton"
                >
                  Edit Details
                </button>
              </form>
            </div>

            <div className="profilePayment">
              <div className="profilePicture">
                <div className="avatar">
                  <FaUser className="iconStyle" />
                </div>
              </div>
              <button
                onClick={handleNavigate1}
                type="button"
                className="changepp"
              >
                Change Profile Picture
              </button>
              <button
                onClick={handleNavigate2}
                type="button"
                className="changepwdbtn"
              >
                Change Password
              </button>
            </div>
          </div>
        </div>
      </div>
      <Footer />
      <FooterBottom />
    </div>
  );
};

export default Profile;
