import React from "react";
import Category from "./shopBy/Category";
import Size from "./shopBy/Size";
import Price from "./shopBy/Price";

const ShopSideNav = () => {
  return (
    <div className="w-full flex flex-col gap-6">
      <Category icons={false} />
      <Size />
      <Price />
    </div>
  );
};

export default ShopSideNav;
