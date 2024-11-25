import React, { Component } from 'react';
import ArrowButtonToHome from '../../components/designLayouts/buttons/ArrowButtonToHome'; 
import CustomArrowFront from '../../assets/icons/CustomArrowFront';

import './Login.css';

export class Login extends Component {
  state = {
    isSignUp: true,
    email: '',
    phone: '',
    password: '',
    confirmPassword: '',
    errors: {
      email: '',
      phone: '',
      password: '',
      confirmPassword: '',
    },
  };

  // Email validation regex
  validateEmail = (email) => {
    const regex = /^[a-zA-Z0-9._%+-]+@[a-zAZ0-9.-]+\.[a-zA-Z]{2,}$/;
    return regex.test(email);
  };

  // Phone validation (10 digits)
  validatePhone = (phone) => {
    const regex = /^[0-9]{10}$/;
    return regex.test(phone);
  };

  // Password validation
  validatePassword = (password) => {
    const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,12}$/;
    return regex.test(password);
  };

  // Handle form inputs and validation
  handleChange = (e) => {
    const { name, value } = e.target;
    this.setState({ [name]: value });

    let errors = this.state.errors;

    if (this.state.isSignUp) {
      switch (name) {
        case 'email':
          errors.email = this.validateEmail(value) ? '' : 'Invalid email address';
          break;
        case 'phone':
          errors.phone = this.validatePhone(value) ? '' : 'Phone number must have 10 digits';
          break;
        case 'password':
          errors.password = this.validatePassword(value)
            ? ''
            : 'Password must be 8-12 characters, include upper/lowercase, a number, and a special character';
          break;
        case 'confirmPassword':
          errors.confirmPassword = value === this.state.password ? '' : 'Passwords do not match';
          break;
        default:
          break;
      }
    }

    this.setState({ errors });
  };

  // Toggle between Sign Up and Sign In
  toggleForm = () => {
    this.setState({ isSignUp: !this.state.isSignUp });
  };

  // Handle form submission
  handleSubmit = (e) => {
    e.preventDefault();

    const { isSignUp, email, phone, password, confirmPassword, errors } = this.state;

    // Ensure all fields are valid before submission (only check validation if it's Sign Up form)
    if (
      (isSignUp &&
        !errors.email &&
        !errors.phone &&
        !errors.password &&
        !errors.confirmPassword &&
        email &&
        phone &&
        password &&
        confirmPassword) ||
      (!isSignUp && email && password) // For Sign In, only email and password are required
    ) {
      // Proceed with form submission logic (e.g., API call)
      console.log('Form Submitted!');
    } else {
      console.log('Form contains errors');
    }
  };

  render() {
    const { isSignUp, email, phone, password, confirmPassword, errors } = this.state;

    return (
      <div className='fullBody'>
        <ArrowButtonToHome />
        <div className='imageContainer'>
          <img className='logoo' src={require('../../assets/images/logo.png')} alt="Logo" />
        </div>
        <div className='slideMenu'>
          <h2
            className={`slideMenuItem ${isSignUp ? 'active' : ''}`}
            onClick={() => this.setState({ isSignUp: true })}
          >
            SIGN UP
          </h2>
          <h2
            className={`slideMenuItem ${!isSignUp ? 'active' : ''}`}
            onClick={() => this.setState({ isSignUp: false })}
          >
            SIGN IN
          </h2>
        </div>
        <div className={`formContainer ${isSignUp ? 'slideRight' : 'slideLeft'}`}>
          {isSignUp ? (
            
            <form onSubmit={this.handleSubmit} className='loginForm'>
              <div className='welcomeAll'>
                  <h1 className='welcome'>FASHION </h1>
                  <h1 className='welcome'>AWAITS YOU!</h1>
                </div>
              <div className='name'>
                <input
                  type='text'
                  className='fName'
                  placeholder='First Name'
                  name='firstName'
                  required
                />
                <input
                  type='text'
                  className='lName'
                  placeholder='Last Name'
                  name='lastName'
                  required
                />
              </div>
              <div className='contact'>
                <input
                  type='email'
                  className='email'
                  placeholder='Email'
                  name='email'
                  value={email}
                  onChange={this.handleChange}
                  required
                />
                {errors.email && <p className='error'>{errors.email}</p>}
                <input
                  type='text'
                  className='mobile'
                  placeholder='Mobile No'
                  name='phone'
                  value={phone}
                  onChange={this.handleChange}
                  required
                />
                {errors.phone && <p className='error'>{errors.phone}</p>}
                <input
                  type='password'
                  className='pw'
                  placeholder='Password'
                  name='password'
                  value={password}
                  onChange={this.handleChange}
                  required
                />
                {errors.password && <p className='error'>{errors.password}</p>}
                <input
                  type='password'
                  className='pw'
                  placeholder='Confirm Password'
                  name='confirmPassword'
                  value={confirmPassword}
                  onChange={this.handleChange}
                  required
                />
                {errors.confirmPassword && <p className='error'>{errors.confirmPassword}</p>}
              </div>
              <div className='button-container'>
                <button type='submit' className='signUpButton'>
                  Sign Up
                  <CustomArrowFront />
                </button>
              </div>
            </form>
          ) : (
            <form onSubmit={this.handleSubmit} className='loginForm'>
              <div className='contact'>
                <div className='welcomeAll'>
                  <h1 className='welcome'>WELCOME</h1>
                  <h1 className='welcome'>BACK!</h1>
                </div>
                <input
                  type='email'
                  className='email'
                  placeholder='Email'
                  name='email'
                  value={email}
                  onChange={this.handleChange}
                  required
                />
                {errors.email && <p className='error'>{errors.email}</p>}
                <input
                  type='password'
                  className='pw'
                  placeholder='Password'
                  name='password'
                  value={password}
                  onChange={this.handleChange}
                  required
                />
                {errors.password && <p className='error'>{errors.password}</p>}
              </div>
              <div className='button-container'>
                <button type='submit' className='signUpButton'>
                  Sign In
                  <span className='arrowIcon'>
                    <CustomArrowFront />
                  </span>
                </button>
              </div>
              <br />
            </form>
          )}
        </div>
        <div className='formErrors'>
          {(errors.email || errors.phone || errors.password || errors.confirmPassword) && (
            <p className='error'>
              Please fix the errors above to proceed.
            </p>
          )}
        </div>
      </div>
    );
  }
}

export default Login;
