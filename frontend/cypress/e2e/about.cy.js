describe("About Page and Breadcrumbs Component", () => {
    beforeEach(() => {
      // Navigate to the About page with a `state` object to simulate navigation from another page
      cy.visit("/about", {
        state: { data: "Shop" }, // Simulating `prevLocation` as "Shop"
      });
    });
  
    it("should render the About page and display breadcrumbs", () => {
      // Verify the title in Breadcrumbs
      cy.get(".text-5xl")
        .should("contain.text", "About");
  
      // Verify the breadcrumb path
      cy.get(".text-sm")
        .should("contain.text", "Shop")
        .and("contain.text", "about"); // Ensure 'About' appears in breadcrumbs
  
      // Check the description on the About page
      cy.get(".text-base")
        .should("contain.text", "VelvetBloom is one of the world's leading ecommerce brands");
  
      // Verify the "Continue Shopping" button exists and works
      cy.get("button").should("contain.text", "Continue Shopping").click();
  
      // Verify the redirection to the shop page
      cy.url().should("include", "/shop");
    });
  
    it("should show 'Home' in breadcrumbs if no previous location is set", () => {
      // Simulate visiting without a `state` object
      cy.visit("/about");
  
      // Verify that the breadcrumb starts with "Home"
      cy.get(".text-sm").should("contain.text", "Home");
    });
  });
  