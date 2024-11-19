import React, { useState } from "react";
import Banner from "../../components/Banner/Banner";
import Pagination from "../../components/pageProps/shopPage/Pagination";
import ProductBanner from "../../components/pageProps/shopPage/ProductBanner";
import Breadcrumbs from "../../components/pageProps/Breadcrumbs";

const Home = () => {
  const [itemsPerPage, setItemsPerPage] = useState(12);
  const itemsPerPageFromBanner = (itemsPerPage) => {
    setItemsPerPage(itemsPerPage);
  };
  return (
    <div className="w-full mx-auto">
      <Banner />
      <div className="max-w-container mx-auto px-4">
      <Breadcrumbs title="Products" />
      </div>
      <div className="max-w-container mx-auto px-4">
      <Pagination itemsPerPage={itemsPerPage} />
      </div>
    </div>
  );
};

export default Home;
