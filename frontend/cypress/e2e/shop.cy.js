describe("Shop Component", () => {
    beforeEach(() => {
      cy.intercept("GET", "http://localhost:8080/products", {
        fixture: "products.json", // Mock data, create this file in `cypress/fixtures`
      }).as("getProducts");
      cy.visit("/shop");
    });
  
    it("should fetch and display products", () => {
      cy.wait("@getProducts");
      cy.get(".product-item").should("have.length.greaterThan", 0); // Adjust the class name as per your implementation
    });
  
    it("should filter products by category", () => {
      cy.get(".category-filter").first().click(); // Simulate category selection
      cy.get(".product-item").each(($item) => {
        cy.wrap($item).should("contain", "Category Name"); // Adjust filter check
      });
    });
  
    it("should filter products by price range", () => {
      cy.get(".price-filter").type("100-500"); // Simulate price filter input
      cy.get(".apply-price-filter").click(); // Simulate filter button click
      cy.get(".product-item").each(($item) => {
        cy.wrap($item)
          .find(".price")
          .invoke("text")
          .then((price) => {
            expect(Number(price)).to.be.within(100, 500);
          });
      });
    });
  
    it("should paginate products correctly", () => {
      cy.get(".pagination-button").contains("2").click(); // Navigate to page 2
      cy.get(".product-item").should("have.length", 12); // Verify items per page
    });
  });
  