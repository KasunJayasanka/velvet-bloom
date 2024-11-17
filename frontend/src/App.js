import './App.css';
import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Login from "./Login"; 

function App() {
  return (
    <Router>
      {/* <Navbar /> */}
      {/* <Hero /> */}
      <div>
        {/* Define routes for different pages */}
        <Routes>
          <Route path="/Login" element={<Login />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
