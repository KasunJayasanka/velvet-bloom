import React, { Component } from 'react';
import axios from 'axios';
import { connect } from "react-redux";
import { setLoggedIn } from "../../redux/velvetSlice"; // Import setLoggedIn
import ArrowButtonToHome from '../../components/designLayouts/buttons/ArrowButtonToHome';
import CustomArrowFront from '../../assets/icons/CustomArrowFront';
import './Login.css';

const BACKEND_URL = process.env.REACT_APP_BACKEND_URL || 'http://localhost:8080';

export class Login extends Component {
  state = {
    isSignUp: true,
    firstName: '',
    lastName: '',
    email: '',
    phone: '',
    password: '',
    confirmPassword: '',
    country: '',
    city: '',
    address: '',
    secondaryAddress: '',
    errors: {},
    loading: false,
    successMessage: '',
    errorMessage: '',
  };

  handleChange = (e) => {
    const { name, value } = e.target;
    this.setState({ [name]: value });
  };

  handleSubmit = async (e) => {
    e.preventDefault();
    const {
      isSignUp,
      firstName,
      lastName,
      email,
      phone,
      password,
      confirmPassword,
      country,
      city,
      address,
      secondaryAddress,
    } = this.state;

    this.setState({ loading: true, errorMessage: '', successMessage: '' });

    try {
      let response;
      if (isSignUp) {
        // Sign Up API Call
        response = await axios.post(`${BACKEND_URL}/auth/signup`, {
          firstName,
          lastName,
          email,
          mobileNo: phone,
          password,
          country,
          city,
          address,
          secondaryAddress,
        });
        this.setState({ successMessage: 'Sign Up Successful! Please Sign In.' });
        this.toggleForm();
      } else {
        // Sign In API Call
        response = await axios.post(`${BACKEND_URL}/auth/login`, { email, password });
        localStorage.setItem('token', response.data.token); // Save token
        this.props.setLoggedIn({
          email,
          token: response.data.token,
        }); // Update Redux state
        window.location.href = response.data.redirectUrl; // Redirect user
      }
    } catch (error) {
      const errorMessage =
        error.response && error.response.data ? error.response.data.message : 'Something went wrong!';
      this.setState({ errorMessage });
    } finally {
      this.setState({ loading: false });
    }
  };

  toggleForm = () => {
    this.setState((prevState) => ({ isSignUp: !prevState.isSignUp }));
  };

  render() {
    const {
      isSignUp,
      firstName,
      lastName,
      email,
      phone,
      password,
      confirmPassword,
      country,
      city,
      address,
      secondaryAddress,
      loading,
      successMessage,
      errorMessage,
    } = this.state;

    return (
      <div className="fullBody">
        <ArrowButtonToHome />
        <div className="imageContainer">
          <img className="logoo" src={require('../../assets/images/logo.png')} alt="Logo" />
        </div>
        <div className="slideMenu">
          <h2
            className={`slideMenuItem ${isSignUp ? 'active' : ''}`}
            onClick={this.toggleForm}
          >
            SIGN UP
          </h2>
          <h2
            className={`slideMenuItem ${!isSignUp ? 'active' : ''}`}
            onClick={this.toggleForm}
          >
            SIGN IN
          </h2>
        </div>
        <div className={`formContainer ${isSignUp ? 'slideRight' : 'slideLeft'}`}>
          <form onSubmit={this.handleSubmit} className="loginForm">
            {isSignUp && (
              <>
                <input
                  type="text"
                  name="firstName"
                  placeholder="First Name"
                  value={firstName}
                  onChange={this.handleChange}
                  required
                />
                <input
                  type="text"
                  name="lastName"
                  placeholder="Last Name"
                  value={lastName}
                  onChange={this.handleChange}
                  required
                />
                <input
                  type="email"
                  name="email"
                  placeholder="Email"
                  value={email}
                  onChange={this.handleChange}
                  required
                />
                <input
                  type="text"
                  name="phone"
                  placeholder="Mobile No"
                  value={phone}
                  onChange={this.handleChange}
                  required
                />
                <input
                  type="password"
                  name="password"
                  placeholder="Password"
                  value={password}
                  onChange={this.handleChange}
                  required
                />
                <input
                  type="password"
                  name="confirmPassword"
                  placeholder="Confirm Password"
                  value={confirmPassword}
                  onChange={this.handleChange}
                  required
                />
                <input
                  type="text"
                  name="country"
                  placeholder="Country"
                  value={country}
                  onChange={this.handleChange}
                  required
                />
                <input
                  type="text"
                  name="city"
                  placeholder="City"
                  value={city}
                  onChange={this.handleChange}
                  required
                />
                <input
                  type="text"
                  name="address"
                  placeholder="Address"
                  value={address}
                  onChange={this.handleChange}
                  required
                />
                <input
                  type="text"
                  name="secondaryAddress"
                  placeholder="Secondary Address (Optional)"
                  value={secondaryAddress}
                  onChange={this.handleChange}
                />
              </>
            )}
            {!isSignUp && (
              <>
                <input
                  type="email"
                  name="email"
                  placeholder="Email"
                  value={email}
                  onChange={this.handleChange}
                  required
                />
                <input
                  type="password"
                  name="password"
                  placeholder="Password"
                  value={password}
                  onChange={this.handleChange}
                  required
                />
              </>
            )}
            {loading && <p>Loading...</p>}
            {successMessage && <p className="success">{successMessage}</p>}
            {errorMessage && <p className="error">{errorMessage}</p>}
            <button type="submit" className="signUpButton">
              {isSignUp ? 'Sign Up' : 'Sign In'}
              <CustomArrowFront />
            </button>
          </form>
        </div>
      </div>
    );
  }
}

const mapDispatchToProps = {
  setLoggedIn,
};

export default connect(null, mapDispatchToProps)(Login);
