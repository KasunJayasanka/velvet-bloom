describe("Contact Page and Breadcrumbs Component", () => {
    beforeEach(() => {
      // Navigate to the Contact page with a `state` object simulating navigation from another page
      cy.visit("/contact", {
        state: { data: "About" }, // Simulating `prevLocation` as "About"
      });
    });
  
    it("should render the Contact page and breadcrumbs correctly", () => {
      // Verify the title in Breadcrumbs
      cy.get(".text-5xl").should("contain.text", "Contact");
  
      // Verify the breadcrumb path
      cy.get(".text-sm")
        .should("contain.text", "About")
        .and("contain.text", "contact");
  
      // Verify the form title
      cy.get("form h1").should("contain.text", "Fill up a Form");
    });
  
    it("should show validation errors for empty inputs", () => {
      // Click the Post button without filling any input fields
      cy.get("button").contains("Post").click();
  
      // Check validation error messages
      cy.get(".text-red-500").should("contain.text", "Enter your Name");
      cy.get(".text-red-500").should("contain.text", "Enter your Email");
      cy.get(".text-red-500").should("contain.text", "Enter your Messages");
    });
  
    it("should show a validation error for invalid email", () => {
      // Enter a valid name and message but an invalid email
      cy.get("input[placeholder='Enter your name here']").eq(0).type("John Doe");
      cy.get("input[type='email']").type("invalid-email");
      cy.get("textarea").type("This is a test message.");
      cy.get("button").contains("Post").click();
  
      // Check validation error for email
      cy.get(".text-red-500").should("contain.text", "Enter a Valid Email");
    });
  
    it("should successfully submit the form when inputs are valid", () => {
      // Fill in the form with valid data
      cy.get("input[placeholder='Enter your name here']").eq(0).type("John Doe");
      cy.get("input[type='email']").type("john.doe@example.com");
      cy.get("textarea").type("This is a test message.");
      cy.get("button").contains("Post").click();
  
      // Check for success message
      cy.get("p.text-green-500").should(
        "contain.text",
        "Thank you dear John Doe, Your messages has been received successfully. Futher details will sent to you by your email at john.doe@example.com."
      );
    });
  
    it("should show 'Home' in breadcrumbs if no previous location is provided", () => {
      // Simulate visiting the Contact page without a `state` object
      cy.visit("/contact");
  
      // Verify that the breadcrumb starts with "Home"
      cy.get(".text-sm").should("contain.text", "Home");
    });
});
  