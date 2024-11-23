import React from 'react';

const ShippingForm = () => (
    <div className="space-y-4">
            <div className="flex space-x-4">
              <input type="text" placeholder="First Name" className="w-1/2 p-3 " />
              <input type="text" placeholder="Last Name" className="w-1/2 p-3 " />
            </div>
            <div>
              <input type="text" placeholder="Country" className="w-full p-3 " />
            </div>
            <div>
              <input type="text" placeholder="State / Region" className="w-full p-3 " />
            </div>
            <div>
              <input type="text" placeholder="Address" className="w-full p-3 " />
            </div>
            <div className="flex space-x-4">
              <input type="text" placeholder="City" className="w-1/2 p-3" />
              <input type="text" placeholder="Postal Code" className="w-1/2 p-3 " />
            </div>
          </div>
  );

  export default ShippingForm;