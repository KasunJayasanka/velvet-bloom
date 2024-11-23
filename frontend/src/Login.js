import React, { Component } from 'react';
import CustomArrow from './assets/Icons/CustomArrow';
import CustomArrowFront from './assets/Icons/CustomArrowFront';
import google from './assets/images/Google.png';
import apple from './assets/images/Apple Button.png';
import fb from './assets/images/Facebook Button.png';

import './Login.css';

export class Login extends Component {
  // Initialize state with isSignUp
  state = {
    isSignUp: true,
  };

  // Toggle function for switching between Sign Up and Sign In
  toggleForm = () => {
    this.setState((prevState) => ({ isSignUp: !prevState.isSignUp }));
  };

  render() {
    const { isSignUp } = this.state;

    return (
      <div className='fullBody'>
        <CustomArrow />
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
            <div className='loginForm'>
              <div className='name'>
                <input type='text' className='fName' placeholder='First Name' />
                <input type='text' className='lName' placeholder='Last Name' />
              </div>
              <div className='contact'>
                <input type='text' className='email' placeholder='Email' />
                <input type='text' className='mobile' placeholder='Mobile No' />
                <input type='password' className='pw' placeholder='Password' />
                <input type='password' className='pw' placeholder='Confirm Password' />
              </div>
              <div className='address'>
                <h5>SHIPPING ADDRESS</h5>
                <div className='name'>
                  <input type='text' className='country' placeholder='Country' />
                  <input type='text' className='city' placeholder='City' />
                </div>
                <div className='contact'>
                  <input type='text' className='address' placeholder='Address' />
                  <input type='text' className='address' placeholder='Secondary Address (Optional)' />
                </div>
              </div>
              <div className='button-container'>
                <button type='submit' className='signUpButton'>
                  Sign Up
                  <CustomArrowFront />
                </button>
              </div>          
              <hr />
              <div className='altSignContainer'>
            <p className='altSign'>SIGN IN WITH</p>
          </div>
          <div className='options'>
            <img src={google} alt="Google Button" />
            <img src={apple} alt="Apple Button" />
            <img src={fb} alt="Facebook Button" />
          </div>
            </div>
          ) : (
            <div className='loginForm'>
              <div className='contact'>
                <h1 className='welcome'>WELCOME<br/>BACK!</h1>
                
                <input type='text' className='email' placeholder='Email' />
                <input type='password' className='pw' placeholder='Password' />
              </div>
              <div className='button-container'><br/>
                <button type='submit' className='signUpButton'>
                  Sign In
                    <span className='arrowIcon'>
                        <CustomArrowFront />
                    </span>
                </button>
              </div>
              <br/><hr />
              <div className='altSignContainer'>
            <p className='altSign'>SIGN IN WITH</p>
          </div>
          <div className='options'>
            <img src={google} alt="Google Button" />
            <img src={apple} alt="Apple Button" />
            <img src={fb} alt="Facebook Button" />
          </div>
          <div className='forgotPassword'>
            <p>FORGOT PASSWORD?</p>
          </div>
            </div>
          )}
        </div>
      </div>
    );
  }
}

export default Login;
