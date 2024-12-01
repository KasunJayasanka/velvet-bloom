import React from 'react';

const InformationForm = ({ formData, setFormData }) => {
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  return (
    <div className="space-y-4">
      <input
        type="text"
        name="fullName"
        placeholder="Full Name"
        className="w-full p-3"
        value={formData.fullName || ''}
        onChange={handleInputChange}
      />
      <input
        type="email"
        name="email"
        placeholder="Email"
        className="w-full p-3"
        value={formData.email || ''}
        onChange={handleInputChange}
      />
      <input
        type="tel"
        name="phone"
        placeholder="Phone"
        className="w-full p-3"
        value={formData.phone || ''}
        onChange={handleInputChange}
      />
    </div>
  );
};

export default InformationForm;
