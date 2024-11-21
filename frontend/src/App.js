import {
  createBrowserRouter,
  RouterProvider,
  Outlet,
  createRoutesFromElements,
  Route,
  ScrollRestoration,
} from "react-router-dom";
import Footer from "./components/home/Footer/Footer";
import FooterBottom from "./components/home/Footer/FooterBottom";
import Header from "./components/home/Header/Header";
import About from "./pages/About/About";
import Cart from "./pages/Cart/Cart";
import Contact from "./pages/Contact/Contact";
import Home from "./pages/Home/Home";
import ProductDetails from "./pages/ProductDetails/ProductDetails";
import Shop from "./pages/Shop/Shop";
import Login from "./pages/Login&SignUp/Login";
import Profile from "./pages/Profile/Profile";
import CustomerDetails from "./pages/Profile/CustomerDetails";
import ChangePassword from "./pages/Profile/ChangePassword";

const Layout = () => {
  return (
    <div className="bg-softpurple">
      <Header />
      
      <ScrollRestoration />
      <Outlet />
      <Footer />
      <FooterBottom />
    </div>
  );
};
const router = createBrowserRouter(
  createRoutesFromElements(
    <Route>
      <Route path="/" element={<Layout />}>
        {/* ==================== Header Navlink Start here =================== */}
        <Route index element={<Home />}></Route>
        <Route path="/shop" element={<Shop />}></Route>
        <Route path="/about" element={<About />}></Route>
        <Route path="/contact" element={<Contact />}></Route>
        {/* ==================== Header Navlink End here ===================== */}
        {/* <Route path="/offer" element={<Offer />}></Route> */}
        <Route path="/product/:_id" element={<ProductDetails />}></Route>
        <Route path="/cart" element={<Cart />}></Route>
      </Route>
      <Route path="/signin" element={<Login />}></Route>
      <Route path="/profile" element={<Profile />}></Route>
      <Route path="/customerdetails" element={<CustomerDetails />}></Route>
      <Route path="/changepassword" element={<ChangePassword />}></Route>
    </Route>
    
  )
);

function App() {
  return (
    <div className="font-bodyFont">
      <RouterProvider router={router} />
    </div>
  );
}

export default App;
