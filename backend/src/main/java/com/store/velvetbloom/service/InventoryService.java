package com.store.velvetbloom.service;

import com.store.velvetbloom.dto.InventoryStatsResponseDTO;
import com.store.velvetbloom.repository.CategoryRepository;
import com.store.velvetbloom.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    public InventoryStatsResponseDTO getInventoryStats() {
        int categories = (int) categoryRepository.count();
        int totalProducts = (int) productRepository.count();
        int inStock = (int) productRepository.countByProductCountGreaterThan(0);
        int lowStock = (int) productRepository.countByProductCountBetween(1, 5);
        int outOfStock = (int) productRepository.countByProductCount(0);

        return new InventoryStatsResponseDTO(categories, totalProducts, inStock, lowStock, outOfStock);
    }
}
