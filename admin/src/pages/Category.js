import React, { useState, useEffect } from 'react';
import {
  Paper, Typography, Table, TableBody, TableCell, TableContainer,
  TableHead, TableRow, TablePagination, Grid, Button, TextField, IconButton, Dialog, DialogActions, DialogContent, DialogTitle
} from '@mui/material';
import axios from 'axios';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import { useNavigate } from 'react-router-dom'; 

function Category() {
  const [categories, setCategories] = useState([]);
  const [filteredCategories, setFilteredCategories] = useState([]);
  const [searchCategory, setSearchCategory] = useState('');
  const [openDeleteDialog, setOpenDeleteDialog] = useState(false);
  const [openEditDialog, setOpenEditDialog] = useState(false);
  const [categoryToDelete, setCategoryToDelete] = useState(null);
  const [categoryToEdit, setCategoryToEdit] = useState(null);
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(5);

  const navigate = useNavigate();  

  useEffect(() => {
    async function fetchCategories() {
      try {
        const response = await axios.get('http://localhost:8080/categories/with-product-count');        
        setCategories(response.data);
        setFilteredCategories(response.data); 
      } catch (error) {
        console.error('Error fetching categories:', error);
      }
    }
    fetchCategories();
  }, []);

  // Filter categories based on the search category name
  useEffect(() => {
    const filtered = categories.filter((category) =>
      category.category_name.toLowerCase().includes(searchCategory.toLowerCase())
    );
    setFilteredCategories(filtered);
  }, [searchCategory, categories]);

  // Pagination controls
  const handleChangePage = (event, newPage) => setPage(newPage);
  const handleChangeRowsPerPage = (event) => {
    setRowsPerPage(parseInt(event.target.value, 10));
    setPage(0);
  };

  // Open the delete confirmation dialog
  const handleOpenDeleteDialog = (category) => {
    setCategoryToDelete(category);
    setOpenDeleteDialog(true);
  };

  // Close the delete confirmation dialog
  const handleCloseDeleteDialog = () => {
    setCategoryToDelete(null);
    setOpenDeleteDialog(false);
  };

  // Handle category deletion
  const handleDeleteCategory = async () => {
    try {
      const token = localStorage.getItem("token");
      await axios.delete(`http://localhost:8080/categories/${categoryToDelete.categoryID}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      alert('Category deleted successfully!');
      setCategories(categories.filter((category) => category.categoryID !== categoryToDelete.categoryID));
      setFilteredCategories(filteredCategories.filter((category) => category.categoryID !== categoryToDelete.categoryID));
      handleCloseDeleteDialog();
    } catch (error) {
      console.error('Error deleting category:', error);
    }
  };

  // Open the edit dialog
  const handleOpenEditDialog = (category) => {
    setCategoryToEdit(category);
    setOpenEditDialog(true);
  };

  // Close the edit dialog
  const handleCloseEditDialog = () => {
    setCategoryToEdit(null);
    setOpenEditDialog(false);
  };
 
  // Handle category update
  const handleUpdateCategory = async () => {
    try {
      const formData = new FormData();
      const sendCategory ={
        name:categoryToEdit.category_name,
        description:categoryToEdit.description
      }
      formData.append('category', JSON.stringify(sendCategory));
      const token = localStorage.getItem("token");
      await axios.patch(`http://localhost:8080/categories/${categoryToEdit.categoryID}`, formData, {
          headers: { 
            'Content-Type': 'multipart/form-data',
            'Authorization': `Bearer ${token}`
          },
      });
      setCategories(categories.map((category) =>
        category.categoryID === categoryToEdit.categoryID ? categoryToEdit : category
      ));
      setFilteredCategories(filteredCategories.map((category) =>
        category.categoryID === categoryToEdit.categoryID ? categoryToEdit : category
      ));
      handleCloseEditDialog();
      alert('Product added successfully!');
    } catch (error) {
      console.error('Error updating category:', error);
    }
  };

  // Navigate to Add Category page
  const handleAddCategory = () => {
    navigate('/add-category'); 
  };

  return (
    <Paper
      sx={{
        padding: 3,
        backgroundColor: 'white',
        minHeight: '81vh',
        marginTop: 2,
        marginLeft: 2,
        marginRight: 2,
        borderRadius: 2,
        boxShadow: 3,
      }}
    >
      <Typography variant="h4" sx={{ color: '#9E4BDC', fontWeight: 'bold',marginBottom:2 }}>
        Category
      </Typography>

      <Grid container spacing={2} sx={{ marginBottom: 2, alignItems: 'center' }}>
        <Grid item xs={6}>
          <TextField
            fullWidth
            label="Search by Category Name"
            value={searchCategory}
            onChange={(e) => setSearchCategory(e.target.value)}
          />
        </Grid>
        <Grid item xs={6} sx={{ textAlign: 'right' }}>
          <Button
            variant="contained"
            color="primary"
            onClick={handleAddCategory}
          >
            Add Category 
          </Button>
        </Grid>
      </Grid>

      {/* Categories Table */}
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Category Name</TableCell>
              <TableCell>Description</TableCell>
              <TableCell>Number of Products</TableCell>
              <TableCell>Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {filteredCategories.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage).map((category) => (
              <TableRow key={category.categoryID} data-testid="category-row">
                <TableCell>{category.category_name}</TableCell>
                <TableCell>{category.description}</TableCell>
                <TableCell>{category.numberOfProducts}</TableCell>
                <TableCell>
                  <IconButton onClick={() => handleOpenEditDialog(category)} data-testid={`EditIcon`} >
                    <EditIcon />
                  </IconButton>
                  <IconButton onClick={() => handleOpenDeleteDialog(category)} data-testid={`DeleteIcon`}>
                    <DeleteIcon color="error" />
                  </IconButton>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      {/* Pagination */}
      <TablePagination
        rowsPerPageOptions={[5, 10, 20]}
        component="div"
        count={filteredCategories.length}
        rowsPerPage={rowsPerPage}
        page={page}
        onPageChange={handleChangePage}
        onRowsPerPageChange={handleChangeRowsPerPage}
      />

      {/* Edit Category Dialog */}
      <Dialog open={openEditDialog} onClose={handleCloseEditDialog}>
        <DialogTitle>Edit Category</DialogTitle>
        <DialogContent>
          <TextField
            fullWidth
            label="Category Name"
            value={categoryToEdit?.category_name || ''}
            onChange={(e) => setCategoryToEdit({ ...categoryToEdit, category_name: e.target.value })}
            sx={{ marginTop: 2 }}
          />
          <TextField
            fullWidth
            label="Description"
            value={categoryToEdit?.description || ''}
            onChange={(e) => setCategoryToEdit({ ...categoryToEdit, description: e.target.value })}
            sx={{ marginTop: 2 }}
          />
          <TextField
            fullWidth
            label="Category ID"
            value={categoryToEdit?.categoryID || ''}
            disabled
            sx={{ marginTop: 2 }}
          />
          <TextField
            fullWidth
            label="Number of Products"
            value={categoryToEdit?.numberOfProducts || ''}
            disabled
            sx={{ marginTop: 2 }}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseEditDialog} color="primary" data-testid="CancelButton">
            Cancel
          </Button>
          <Button onClick={handleUpdateCategory} color="secondary" data-testid="SaveButton">
            Save
          </Button>
        </DialogActions>
      </Dialog>

      {/* Delete Category Dialog */}
      <Dialog open={openDeleteDialog} onClose={handleCloseDeleteDialog}>
        <DialogTitle>Delete Category</DialogTitle>
        <DialogContent>
          <Typography>
            Are you sure you want to delete this category? <br />
            <strong>Category Name:</strong> {categoryToDelete?.category_name} <br />
            <strong>Description:</strong> {categoryToDelete?.description}
          </Typography>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDeleteDialog} color="primary">
            Cancel
          </Button>
          <Button onClick={handleDeleteCategory} color="secondary">
            Delete
          </Button>
        </DialogActions>
      </Dialog>
    </Paper>
  );
}

export default Category;
