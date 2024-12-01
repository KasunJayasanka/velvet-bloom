import React, { useState } from "react";
import Banner from "../../components/Banner/Banner";
import Shop from "../Shop/Shop";

const Home = () => {
  const [itemsPerPage, setItemsPerPage] = useState(12);
  const itemsPerPageFromBanner = (itemsPerPage) => {
    setItemsPerPage(itemsPerPage);
  };
  return (
    <div className="w-full mx-auto">
      <Banner />
   
      <div className="max-w-container mx-auto px-4">
        <Shop/>
      
      </div>
    </div>
  );
};

export default Home;
