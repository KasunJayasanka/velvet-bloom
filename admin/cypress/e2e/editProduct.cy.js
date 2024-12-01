describe('Edit Product Page UI', () => {
    beforeEach(() => {
      // Assuming '/product/edit/:id' is the edit page URL
      cy.visit('/project-management');
    });
     
    it('Should navigate to the Edit Product page when the edit icon is clicked', () => {
      // Click the edit icon for the first product
      cy.get('[data-testid="EditIcon"]').first().click();
  
      // Verify navigation to the Edit Product page
      cy.url().should('include', '/edit-product/');
    });

    it('Should load the Edit Product page with correct title', () => {
      cy.contains('Edit Product').should('be.visible');
    });
  
    // it('Should display the correct product name in the input field', () => {
    //   cy.get('input[name="productName"]')
    //     .should('have.value', 'Product Name') // Replace 'Product Name' with the actual default value if needed
    //     .should('be.visible');
    // });
  
    
});
  