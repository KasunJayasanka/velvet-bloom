import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import Breadcrumbs from "../../components/pageProps/Breadcrumbs";
import Header from "../../components/home/Header/Header"
import Footer from "../../components/home/Footer/Footer"
import FooterBottom from "../../components/home/Footer/FooterBottom"
import { FaUser } from "react-icons/fa";
import "./Profile.css";

const Profile = () => {
  const location = useLocation();
  const [prevLocation, setPrevLocation] = useState("");

  useEffect(() => {
    setPrevLocation(location.state?.data || ""); // Fallback if location.state is undefined
  }, [location]);

  const navigate = useNavigate();

    const handleNavigate1 = () => {
        navigate('/customerdetails'); // Replace '/' with the home route of your application
    };
    const handleNavigate2 = () => {
      navigate('/changepassword'); // Replace '/' with the home route of your application
  };


  return (
    <div>
      <Header></Header>
    
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
                  placeholder="ID"
                  disabled
                />
              </div>

              <p className="addressTitle">NAME</p>
              <div className="name">
                <input
                  type="text"
                  className="fName"
                  placeholder="First Name"
                  disabled
                />
                <input
                  type="text"
                  className="lName"
                  placeholder="Last Name"
                  disabled
                />
              </div>
              <p className="addressTitle">EMAIL</p>
              <div className="contact">
                <input
                  type="text"
                  className="email"
                  placeholder="Email"
                  disabled
                />
              </div>
              <p className="addressTitle">PHONE</p>
              <div className="contact">
                <input
                  type="text"
                  className="mobile"
                  placeholder="Mobile No"
                  disabled
                />
              </div>
              <p className="addressTitle">COUNTRY & CITY</p>
              <div className="name">
                <input
                  type="text"
                  className="country"
                  placeholder="Country"
                  disabled
                />
                <input
                  type="text"
                  className="city"
                  placeholder="City"
                  disabled
                />
              </div>
              <div className="contact">
                <p className="addressTitle">SHIPPING ADDRESS</p>
                <input
                  type="text"
                  className="address"
                  placeholder="Address"
                  disabled
                />
              </div>
              <div className="contact">
                <input
                  type="text"
                  className="address"
                  placeholder="Secondary Address"
                  disabled
                />
              </div>
              <button onClick={handleNavigate1} type="button" className="Editbutton">
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
          <button onClick={handleNavigate1} type="button" className="changepp">
              Change Profile Picture
            </button>
            <button onClick={handleNavigate2} type="button" className="changepwdbtn">
              Change Password
            </button>
          </div>
        </div>
      </div>
    </div>
    <Footer></Footer>
    <FooterBottom></FooterBottom>
    </div>
  );
};

export default Profile;
