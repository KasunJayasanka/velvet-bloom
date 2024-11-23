import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import Breadcrumbs from "../../components/pageProps/Breadcrumbs";
import { FaUser } from "react-icons/fa";
import "./Profile.css";

const CustomerDetails = () => {
  const location = useLocation();
  const [prevLocation, setPrevLocation] = useState("");

  useEffect(() => {
    setPrevLocation(location.state?.data || ""); // Fallback if location.state is undefined
  }, [location]);

  const navigate = useNavigate();

    const handleNavigate1 = () => {
        navigate('/profile'); // Replace '/' with the home route of your application
    };

  return (
    <div className="max-w-container mx-auto px-4">
      {/* Breadcrumbs Component */}
      <Breadcrumbs title="Edit Customer Details" prevLocation={prevLocation} />

      <div className="pb-10">
        <div className="profileContainer">
          <div className="profileDetails">
            <form>
              <p className="addressTitle">NAME</p>
              <div className="name">
                <input
                  type="text"
                  className="fName"
                  placeholder="First Name"
                />
                <input
                  type="text"
                  className="lName"
                  placeholder="Last Name"
                />
              </div>
              <p className="addressTitle">EMAIL</p>
              <div className="contact">
                <input
                  type="text"
                  className="email"
                  placeholder="Email"
                />
              </div>
              <p className="addressTitle">PHONE</p>
              <div className="contact">
                <input
                  type="text"
                  className="mobile"
                  placeholder="Mobile No"
                />
              </div>
              <div className="address">
                <p className="addressTitle">COUNTRY & CITY</p>
                <input
                  type="text"
                  className="country"
                  placeholder="Country"
                />
                <input
                  type="text"
                  className="city"
                  placeholder="City"
                />
              </div>
              <div className="contact">
                <p className="addressTitle">SHIPPING ADDRESS</p>
                <input
                  type="text"
                  className="address"
                  placeholder="Address"
                />
              </div>
              <div className="contact">
                <input
                  type="text"
                  className="address"
                  placeholder="Secondary Address"
                />
              </div>
              <button onClick={handleNavigate1} type="button" className="Editbutton">
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
  );
};

export default CustomerDetails;
