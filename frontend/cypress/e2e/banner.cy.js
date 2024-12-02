describe("Banner Component", () => {
    beforeEach(() => {
      cy.visit("/"); // Adjust the path to your homepage
    });
  
    it("should render the banner component with all images", () => {
      cy.get(".w-full.bg-white").within(() => {
        cy.get("a").should("have.length", 3); // Ensure there are 3 banner links
        cy.get("img").each(($img) => {
          cy.wrap($img).should("be.visible"); // Check each image is visible
        });
      });
    });
  
    it("should navigate to shop page when a banner is clicked", () => {
      cy.get("a").first().click();
      cy.url().should("include", "/shop");
    });
  });
  