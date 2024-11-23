import React from 'react';

const InformationForm = () => (
    <div className="space-y-4">
      <input type="text" placeholder="Full Name" className="w-full" />
      <input type="email" placeholder="Email" className="w-full " />
      <input type="tel" placeholder="Phone" className="w-full" />
    </div>
  );

  export default InformationForm;