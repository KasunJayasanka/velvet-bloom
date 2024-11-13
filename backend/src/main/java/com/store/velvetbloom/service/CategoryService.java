package com.store.velvetbloom.service;

import com.store.velvetbloom.domain.Category;
import com.store.velvetbloom.domain.Product;
import com.store.velvetbloom.model.CategoryDTO;
import com.store.velvetbloom.repository.CategoryRepository;
import com.store.velvetbloom.repository.ProductRepository;
import com.store.velvetbloom.util.NotFoundException;
import com.store.velvetbloom.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryService(final CategoryRepository categoryRepository,
            final ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    public List<CategoryDTO> findAll() {
        final List<Category> categories = categoryRepository.findAll(Sort.by("categoryId"));
        return categories.stream()
                .map(category -> mapToDTO(category, new CategoryDTO()))
                .toList();
    }

    public CategoryDTO get(final UUID categoryId) {
        return categoryRepository.findById(categoryId)
                .map(category -> mapToDTO(category, new CategoryDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final CategoryDTO categoryDTO) {
        final Category category = new Category();
        mapToEntity(categoryDTO, category);
        return categoryRepository.save(category).getCategoryId();
    }

    public void update(final UUID categoryId, final CategoryDTO categoryDTO) {
        final Category category = categoryRepository.findById(categoryId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(categoryDTO, category);
        categoryRepository.save(category);
    }

    public void delete(final UUID categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    private CategoryDTO mapToDTO(final Category category, final CategoryDTO categoryDTO) {
        categoryDTO.setCategoryId(category.getCategoryId());
        categoryDTO.setCategoryName(category.getCategoryName());
        return categoryDTO;
    }

    private Category mapToEntity(final CategoryDTO categoryDTO, final Category category) {
        category.setCategoryName(categoryDTO.getCategoryName());
        return category;
    }

    public ReferencedWarning getReferencedWarning(final UUID categoryId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Category category = categoryRepository.findById(categoryId)
                .orElseThrow(NotFoundException::new);
        final Product categoryProduct = productRepository.findFirstByCategory(category);
        if (categoryProduct != null) {
            referencedWarning.setKey("category.product.category.referenced");
            referencedWarning.addParam(categoryProduct.getProductId());
            return referencedWarning;
        }
        return null;
    }

}
