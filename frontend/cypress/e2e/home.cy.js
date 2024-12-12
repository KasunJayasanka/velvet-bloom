describe("Home Page", () => {
    beforeEach(() => {
      cy.visit("/");
    });
  
    it("should display the banner and shop components", () => {
      cy.get(".w-full.mx-auto").within(() => {
        cy.get(".w-full.bg-white").should("exist"); // Updated to match actual class
        cy.get(".max-w-container").should("exist");
      });
    });
  
});
  