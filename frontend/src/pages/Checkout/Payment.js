import React from "react";
import { Link } from "react-router-dom";

const PaymentForm = ({ cartItems = [] }) => { 
  const subtotal = Array.isArray(cartItems) 
    ? cartItems.reduce((acc, item) => acc + (item.price * item.quantity), 0) 
    : 0;
  const shippingPrice = 10;
  const total = subtotal + shippingPrice;

  return (
    <div className="space-y-4">
      <input type="text" placeholder="Card Number" className="w-full p-3 border border-gray-300 rounded-md" />
      <input type="text" placeholder="Expiry Date" className="w-full p-3 border border-gray-300 rounded-md" />
      <input type="text" placeholder="CVV" className="w-full p-3 border border-gray-300 rounded-md" />
      <div>
        <Link
          to={{
            pathname: "/OrderConfirmation",
            state: {
              cartItems: cartItems, 
              subtotal: subtotal,
              shippingPrice: shippingPrice,
              total: total,
            },
          }}
        >
        <button className="px-8 py-3 bg-purple-600 text-white rounded-lg hover:bg-purple-700">
                      Checkout
        </button>
        </Link>

      </div>
    </div>
  );
};

export default PaymentForm;





// import React from "react";
// import { Link } from "react-router-dom";

// const PaymentForm = ({ cartItems }) => (
//   <div className="space-y-4">
//     <input type="text" placeholder="Card Number" className="w-full p-3 border border-gray-300 rounded-md" />
//     <input type="text" placeholder="Expiry Date" className="w-full p-3 border border-gray-300 rounded-md" />
//     <input type="text" placeholder="CVV" className="w-full p-3 border border-gray-300 rounded-md" />
//     <div>
//       {/* Send the cartItems to OrderConfirmation page */}
//       <Link to={{ pathname: "/OrderConfirmation", state: { cartItems } }}>
//         <button className="w-52 h-10 bg-primeColor text-white hover:bg-black duration-300">
//           Checkout
//         </button>
//       </Link>
//     </div>
//   </div>
// );

// export default PaymentForm;