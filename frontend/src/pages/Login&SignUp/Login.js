import React, { Component } from 'react';
import ArrowButtonToHome from '../../components/designLayouts/buttons/ArrowButtonToHome'; 
import CustomArrowFront from '../../assets/icons/CustomArrowFront';
import google from '../../assets/images/Google Button.png';
import apple from '../../assets/images/Apple Button.png';
import fb from '../../assets/images/Facebook Button.png';

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
      <ArrowButtonToHome />
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
                <p className='addressTitle'>SHIPPING ADDRESS</p>
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
              {/* <hr />
              <div className='altSignContainer'>
            <p className='altSign'>SIGN IN WITH</p>
          </div>
          <div className='options'>
            <img className='social' src={google} alt="Google Button" />
            <img className='social' src={apple} alt="Apple Button" />
            <img className='social' src={fb} alt="Facebook Button" />
          </div> */}
            </div>
          ) : (
            <div className='loginForm'>
              <div className='contact'>
                <div className='welcomeAll'>
                  <h1 className='welcome'>WELCOME</h1>
                  <h1 className='welcome'>BACK!</h1>
                </div>
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
              <br/>
              <hr />
              {/* <div className='altSignContainer'>
            <p className='altSign'>SIGN IN WITH</p>
          </div>
          <div className='options'>
            <img className='social' src={google} alt="Google Button" />
            <img className='social' src={apple} alt="Apple Button" />
            <img className='social' src={fb} alt="Facebook Button" />
          </div> */}
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