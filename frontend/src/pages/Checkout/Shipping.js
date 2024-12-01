import React from 'react';

const ShippingForm = ({ formData, setFormData }) => {
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      shipping: {
        ...prevData.shipping,
        [name]: value,
      },
    }));
  };

  return (
    <div className="space-y-4">
      <div className="flex space-x-4">
        <input
          type="text"
          name="firstName"
          placeholder="First Name"
          className="w-1/2 p-3"
          value={formData.shipping.firstName || ''}
          onChange={handleInputChange}
        />
        <input
          type="text"
          name="lastName"
          placeholder="Last Name"
          className="w-1/2 p-3"
          value={formData.shipping.lastName || ''}
          onChange={handleInputChange}
        />
      </div>
      <div>
        <input
          type="text"
          name="country"
          placeholder="Country"
          className="w-full p-3"
          value={formData.shipping.country || ''}
          onChange={handleInputChange}
        />
      </div>
      <div>
        <input
          type="text"
          name="state"
          placeholder="State / Region"
          className="w-full p-3"
          value={formData.shipping.state || ''}
          onChange={handleInputChange}
        />
      </div>
      <div>
        <input
          type="text"
          name="address"
          placeholder="Address"
          className="w-full p-3"
          value={formData.shipping.address || ''}
          onChange={handleInputChange}
        />
      </div>
      <div className="flex space-x-4">
        <input
          type="text"
          name="city"
          placeholder="City"
          className="w-1/2 p-3"
          value={formData.shipping.city || ''}
          onChange={handleInputChange}
        />
        <input
          type="text"
          name="postalCode"
          placeholder="Postal Code"
          className="w-1/2 p-3"
          value={formData.shipping.postalCode || ''}
          onChange={handleInputChange}
        />
      </div>
    </div>
  );
};

export default ShippingForm;
