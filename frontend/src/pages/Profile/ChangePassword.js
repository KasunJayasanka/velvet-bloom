import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import Breadcrumbs from "../../components/pageProps/Breadcrumbs";
import { FaLock } from "react-icons/fa";
import "./Profile.css";

const ChangePassword = () => {
  const location = useLocation();
  const [prevLocation, setPrevLocation] = useState("");

  useEffect(() => {
    setPrevLocation(location.state?.data || ""); // Handle state fallback if undefined
  }, [location]);

  const navigate = useNavigate();

    const handleNavigate1 = () => {
        navigate('/profile'); // Replace '/' with the home route of your application
    };

  return (
    <div className="max-w-container mx-auto px-4">
      {/* Breadcrumbs Component */}
      <Breadcrumbs title="Change Password" prevLocation={prevLocation} />

      <div className="pb-10">
        <div className="profileContainer">
          <div className="profileDetails">
            <form>
              <div className="contact">
                <input
                  type="text"
                  className="email"
                  placeholder="Email"
                />
                <input
                  type="password"
                  className="password"
                  placeholder="Old Password"
                />
                <input
                  type="password"
                  className="password"
                  placeholder="New Password"
                />
                <input
                  type="password"
                  className="password"
                  placeholder="Confirm Password"
                />
              </div>
              <button onClick={handleNavigate1} type="button" className="Editbutton">
                Update Password
              </button>
            </form>
          </div>

          <div className="profilePayment">
            <div className="profilePicture">
            <div className="avatar">
              <FaLock className="iconStyle" />
            </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ChangePassword;
