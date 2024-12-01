import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import axios from "axios";
import Breadcrumbs from "../../components/pageProps/Breadcrumbs";
import Header from "../../components/home/Header/Header";
import Footer from "../../components/home/Footer/Footer";
import FooterBottom from "../../components/home/Footer/FooterBottom";
import { FaUser } from "react-icons/fa";
import "./Profile.css";

const BACKEND_URL = process.env.REACT_APP_BACKEND_URL || "http://localhost:8080";

const CustomerDetails = () => {
  const [customerData, setCustomerData] = useState({
    firstName: "",
    lastName: "",
    email: "",
    mobileNo: "",
    country: "",
    city: "",
    address: "",
    secondaryAddress: "",
  });
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const location = useLocation();
  const navigate = useNavigate();

  const customerId = localStorage.getItem("customerID");// Replace with dynamic logic if needed
  const token = localStorage.getItem("token");

  useEffect(() => {
    // Fetch customer details on mount
    const fetchCustomerDetails = async () => {
      try {
        const response = await axios.get(`${BACKEND_URL}/customers/${customerId}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        setCustomerData(response.data.user); // Assuming user object contains required fields
      } catch (err) {
        console.error(err);
        setError("Failed to fetch customer details.");
      }
    };

    fetchCustomerDetails();
  }, [customerId, token]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setCustomerData((prevData) => ({ ...prevData, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.patch(`${BACKEND_URL}/customers/${customerId}`, customerData, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setSuccess("Customer details updated successfully!");
      setError("");
    } catch (err) {
      console.error(err);
      setError("Failed to update customer details. Please try again.");
      setSuccess("");
    }
  };

  return (
    <div>
      <Header />
      <div className="max-w-container mx-auto px-4">
        {/* Breadcrumbs Component */}
        <Breadcrumbs title="Edit Customer Details" />

        <div className="pb-10">
          <div className="profileContainer">
            <div className="profileDetails">
              <form onSubmit={handleSubmit}>
                <p className="addressTitle">NAME</p>
                <div className="name">
                  <input
                    type="text"
                    className="fName"
                    name="firstName"
                    placeholder="First Name"
                    value={customerData.firstName}
                    onChange={handleChange}
                  />
                  <input
                    type="text"
                    className="lName"
                    name="lastName"
                    placeholder="Last Name"
                    value={customerData.lastName}
                    onChange={handleChange}
                  />
                </div>
                <p className="addressTitle">EMAIL</p>
                <div className="contact">
                  <input
                    type="text"
                    className="email"
                    name="email"
                    placeholder="Email"
                    value={customerData.email}
                    onChange={handleChange}
                    disabled
                  />
                </div>
                <p className="addressTitle">PHONE</p>
                <div className="contact">
                  <input
                    type="text"
                    className="mobile"
                    name="mobileNo"
                    placeholder="Mobile No"
                    value={customerData.mobileNo}
                    onChange={handleChange}
                  />
                </div>
                <p className="addressTitle">COUNTRY & CITY</p>
                <div className="name">
                  <input
                    type="text"
                    className="country"
                    name="country"
                    placeholder="Country"
                    value={customerData.country}
                    onChange={handleChange}
                  />
                  <input
                    type="text"
                    className="city"
                    name="city"
                    placeholder="City"
                    value={customerData.city}
                    onChange={handleChange}
                  />
                </div>
                <div className="contact">
                  <p className="addressTitle">SHIPPING ADDRESS</p>
                  <input
                    type="text"
                    className="address"
                    name="address"
                    placeholder="Address"
                    value={customerData.address}
                    onChange={handleChange}
                  />
                </div>
                <div className="contact">
                  <input
                    type="text"
                    className="address"
                    name="secondaryAddress"
                    placeholder="Secondary Address"
                    value={customerData.secondaryAddress}
                    onChange={handleChange}
                  />
                </div>
                {error && <p className="error">{error}</p>}
                {success && <p className="success">{success}</p>}
                <button type="submit" className="Editbutton">
                  Update
                </button>
              </form>
            </div>

            <div className="profilePayment">
              <div className="profilePicture">
                <div className="avatar">
                  <FaUser className="iconStyle" />
                </div>
              </div>
              <button type="button" className="changepp">
                Change Profile Picture
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

export default CustomerDetails;
