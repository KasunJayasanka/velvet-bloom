/// <reference types="cypress" />

const BACKEND_URL = 'http://localhost:8080'; // Update this URL if it's different in your setup.

describe('Login Component Tests', () => {
  beforeEach(() => {
    cy.visit('/login'); // Ensure your app is running, and replace '/login' with the actual route.
  });

  it('should toggle between Sign Up and Sign In forms', () => {
    // Check initial state (Sign Up form)
    cy.contains('SIGN UP').should('have.class', 'active');
    cy.contains('SIGN IN').should('not.have.class', 'active');

    // Switch to Sign In form
    cy.contains('SIGN IN').click();
    cy.contains('SIGN IN').should('have.class', 'active');
    cy.contains('SIGN UP').should('not.have.class', 'active');

    // Switch back to Sign Up form
    cy.contains('SIGN UP').click();
    cy.contains('SIGN UP').should('have.class', 'active');
    cy.contains('SIGN IN').should('not.have.class', 'active');
  });

  it('should submit the Sign Up form successfully', () => {
    cy.contains('SIGN UP').click();

    cy.get('input[name="firstName"]').type('John');
    cy.get('input[name="lastName"]').type('Doe');
    cy.get('input[name="email"]').type('john.doe@example.com');
    cy.get('input[name="phone"]').type('1234567890');
    cy.get('input[name="password"]').type('Password123!');
    cy.get('input[name="confirmPassword"]').type('Password123!');
    cy.get('input[name="country"]').type('USA');
    cy.get('input[name="city"]').type('New York');
    cy.get('input[name="address"]').type('123 Main St');
    cy.get('input[name="secondaryAddress"]').type('Apt 4B');

    // Mock the API response
    cy.intercept('POST', `${BACKEND_URL}/auth/signup`, {
      statusCode: 200,
      body: { message: 'Sign Up Successful!' },
    }).as('signUpRequest');

    // Submit the form
    cy.get('button[type="submit"]').click();

    // Wait for the API call and verify the success message
    cy.wait('@signUpRequest');
    cy.contains('Sign Up Successful! Please Sign In.');
  });

  it('should submit the Sign In form successfully', () => {
    cy.contains('SIGN IN').click();

    cy.get('input[name="email"]').type('john.doe@example.com');
    cy.get('input[name="password"]').type('Password123!');

    // Mock the API response
    cy.intercept('POST', `${BACKEND_URL}/auth/login`, {
      statusCode: 200,
      body: {
        token: 'fake-jwt-token',
        customerId: '12345',
        redirectUrl: '/dashboard',
      },
    }).as('loginRequest');

    // Submit the form
    cy.get('button[type="submit"]').click();

    // Wait for the API call and verify redirection
    cy.wait('@loginRequest').then(() => {
      expect(localStorage.getItem('token')).to.eq('fake-jwt-token');
      expect(localStorage.getItem('customerID')).to.eq('12345');
    });
  });

  it('should show an error message on failed Sign In', () => {
    cy.contains('SIGN IN').click();

    cy.get('input[name="email"]').type('invalid@example.com');
    cy.get('input[name="password"]').type('WrongPassword');

    // Mock the API response with an error
    cy.intercept('POST', `${BACKEND_URL}/auth/login`, {
      statusCode: 401,
      body: { message: 'Invalid credentials' },
    }).as('loginRequest');

    // Submit the form
    cy.get('button[type="submit"]').click();

    // Wait for the API call and verify the error message
    cy.wait('@loginRequest');
    cy.contains('Invalid credentials');
  });
});
