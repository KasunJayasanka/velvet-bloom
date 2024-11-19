import React, { useState } from "react";
import { motion } from "framer-motion";
import NavTitle from "./NavTitle";

const Size = () => {
  const [showSizes, setShowSizes] = useState(true);
  const Sizes = [
    {
      _id: 9001,
      title: "XS",

    },
    {
      _id: 9002,
      title: "S",
 
    },
    {
      _id: 9003,
      title: "M",

    },
    {
      _id: 9004,
      title: "L",

    },
    {
      _id: 9005,
      title: "XL",

    },
  ];

  return (
    <div>
      <div
        onClick={() => setShowSizes(!showSizes)}
        className="cursor-pointer"
      >
        <NavTitle title="Shop by Size" icons={true} />
      </div>
      {showSizes && (
        <motion.div
          initial={{ y: -20, opacity: 0 }}
          animate={{ y: 0, opacity: 1 }}
          transition={{ duration: 0.5 }}
        >
          <ul className="flex flex-col gap-4  ">
            {Sizes.map((item) => (
              <li
                key={item._id}
                className="border-b-[1px] border-b-[#F0F0F0] pb-2 flex items-center gap-2"
              >
                {item.title}
              </li>
            ))}
          </ul>
        </motion.div>
      )}
    </div>
  );
};

export default Size;
