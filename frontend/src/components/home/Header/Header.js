import React, { useState } from "react";
import { Link, NavLink, useLocation } from "react-router-dom";
import { HiMenuAlt2 } from "react-icons/hi";
import { FaUser, FaCaretDown, FaShoppingCart } from "react-icons/fa";
import { motion } from "framer-motion";
import { logo } from "../../../assets/images";
import Image from "../../designLayouts/Image";
import { navBarList } from "../../../constants";
import { useSelector } from "react-redux";

const Header = () => {
  const products = useSelector((state) => state.velvetReducer.products);
  const [showUser, setShowUser] = useState(false);
  const [sidenav, setSidenav] = useState(false);
  const routerLocation = useLocation();

  return (
    <div className="w-full h-20 bg-white sticky top-0 z-50 border-b-[1px] border-b-gray-200">
      <nav className="h-full ">
        <div className="flex justify-between items-center h-full px-4">
          {/* Left Section - Navigation Menu */}
          <div className="flex items-center">
            <HiMenuAlt2
              onClick={() => setSidenav(!sidenav)}
              className="cursor-pointer w-8 h-6 md:hidden"
            />
            <div className="hidden md:flex">
              <motion.ul
                initial={{ y: 30, opacity: 0 }}
                animate={{ y: 0, opacity: 1 }}
                transition={{ duration: 0.5 }}
                className="flex items-center gap-2"
              >
                {navBarList.map(({ _id, title, link }) => (
                  <NavLink
                    key={_id}
                    className="flex font-normal hover:font-bold w-20 h-6 justify-center items-center px-4 text-base text-[#767676] hover:underline underline-offset-[4px] decoration-[1px] hover:text-[#262626] hoverEffect"
                    to={link}
                    state={{ data: routerLocation.pathname.split("/")[1] }}
                  >
                    <li>{title}</li>
                  </NavLink>
                ))}
              </motion.ul>
            </div>
          </div>

          {/* Center Section - Logo */}
          <div className="absolute left-1/2 transform -translate-x-1/2">
            <Link to="/">
              <Image className="w-28 " imgSrc={logo} />
            </Link>
          </div>

          {/* Right Section - Profile and Cart */}
          <div className="flex items-center gap-4 ml-auto">
            <div className="relative">
              <div
                onClick={() => setShowUser(!showUser)}
                className="flex items-center cursor-pointer"
              >
                <FaUser />
                <FaCaretDown />
              </div>
              {showUser && (
                <motion.ul
                  initial={{ y: 30, opacity: 0 }}
                  animate={{ y: 0, opacity: 1 }}
                  transition={{ duration: 0.5 }}
                  className="absolute top-6 right-0 z-50 bg-primeColor w-44 text-[#767676] h-auto p-4 pb-6"
                >
                  <Link to="/signin">
                    <li className="text-gray-400 px-4 py-1 border-b-[1px] border-b-gray-400 hover:border-b-white hover:text-white duration-300 cursor-pointer">
                      Login
                    </li>
                  </Link>
                  <Link to="/signup">
                    <li className="text-gray-400 px-4 py-1 border-b-[1px] border-b-gray-400 hover:border-b-white hover:text-white duration-300 cursor-pointer">
                      Sign Up
                    </li>
                  </Link>
                  <Link to="/profile">
                  <li className="text-gray-400 px-4 py-1 border-b-[1px] border-b-gray-400 hover:border-b-white hover:text-white duration-300 cursor-pointer">
                    Profile
                  </li>
                  </Link>
                  <Link to="/MyOrders">
                  <li className="text-gray-400 px-4 py-1 border-b-[1px] border-b-gray-400 hover:border-b-white hover:text-white duration-300 cursor-pointer">
                    My Orders
                  </li>
                  </Link>
                </motion.ul>
              )}
            </div>
            <div className="relative">
              <Link to="/cart">
                <FaShoppingCart />
                <span className="absolute -top-2 -right-2 text-xs w-4 h-4 flex items-center justify-center rounded-full bg-primeColor text-white">
                  {products.length > 0 ? products.length : 0}
                </span>
              </Link>
            </div>
          </div>
        </div>
      </nav>
    </div>
  );
};

export default Header;
