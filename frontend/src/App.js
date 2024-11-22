import './App.css';
import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Login from "./Login"; 
import MyOrdersOngoing from './MyOrdersOngoing'
import MyOrdersAll from './MyOrdersAll';
import OrderTracking from './OrderTracking';

function App() {
  // return (
  //   <Router>
  //     {/* <Navbar /> */}
  //     {/* <Hero /> */}
  //     <div>
  //       {/* Define routes for different pages */}
  //       <Routes>
  //         <Route path="/Login" element={<Login />} />
  //       </Routes>
  //     </div>
  //   </Router>
  // );
  return <OrderTracking />;
}

export default App;
