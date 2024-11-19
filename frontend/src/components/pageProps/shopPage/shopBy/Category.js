import React, { useState } from "react";
import { motion } from "framer-motion";
// import { FaPlus } from "react-icons/fa";
import { ImPlus } from "react-icons/im";
import NavTitle from "./NavTitle";

const Category = () => {
  const [showSubCatOne, setShowSubCatOne] = useState(true);
  const items = [
    {
      _id: 990,
      title: "Men",
      icons: true,
    },
    {
      _id: 991,
      title: "Women",
    },
    {
      _id: 992,
      title: "Kids",
      icons: true,
    },
    {
      _id: 994,
      title: "Others",
    },
  ];
  return (
    <div  onClick={() => setShowSubCatOne(!showSubCatOne)}
    className="cursor-pointer">
       
      <NavTitle title="Shop by Category" icons={true} />
      {showSubCatOne && (
        <motion.div
          initial={{ y: -20, opacity: 0 }}
          animate={{ y: 0, opacity: 1 }}
          transition={{ duration: 0.5 }}
        >
      <div>
        <ul className="flex flex-col gap-4 text-sm lg:text-base text-[#767676]">
          {items.map(({ _id, title, icons }) => (
            <li
              key={_id}
              className="border-b-[1px] border-b-[#F0F0F0] pb-2 flex items-center justify-between"
            >
              {title}
              
            </li>
          ))}
        </ul>
      </div>
      </motion.div>
      )}
    </div>
  );
};

export default Category;
