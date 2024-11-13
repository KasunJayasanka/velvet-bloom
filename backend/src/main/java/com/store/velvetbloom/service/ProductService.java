package com.store.velvetbloom.service;

import com.store.velvetbloom.domain.Category;
import com.store.velvetbloom.domain.OrderItem;
import com.store.velvetbloom.domain.Product;
import com.store.velvetbloom.domain.Review;
import com.store.velvetbloom.domain.SelectedItem;
import com.store.velvetbloom.domain.Variety;
import com.store.velvetbloom.model.ProductDTO;
import com.store.velvetbloom.repository.CategoryRepository;
import com.store.velvetbloom.repository.OrderItemRepository;
import com.store.velvetbloom.repository.ProductRepository;
import com.store.velvetbloom.repository.ReviewRepository;
import com.store.velvetbloom.repository.SelectedItemRepository;
import com.store.velvetbloom.repository.VarietyRepository;
import com.store.velvetbloom.util.NotFoundException;
import com.store.velvetbloom.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final VarietyRepository varietyRepository;
    private final SelectedItemRepository selectedItemRepository;
    private final OrderItemRepository orderItemRepository;
    private final ReviewRepository reviewRepository;

    public ProductService(final ProductRepository productRepository,
            final CategoryRepository categoryRepository, final VarietyRepository varietyRepository,
            final SelectedItemRepository selectedItemRepository,
            final OrderItemRepository orderItemRepository,
            final ReviewRepository reviewRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.varietyRepository = varietyRepository;
        this.selectedItemRepository = selectedItemRepository;
        this.orderItemRepository = orderItemRepository;
        this.reviewRepository = reviewRepository;
    }

    public List<ProductDTO> findAll() {
        final List<Product> products = productRepository.findAll(Sort.by("productId"));
        return products.stream()
                .map(product -> mapToDTO(product, new ProductDTO()))
                .toList();
    }

    public ProductDTO get(final UUID productId) {
        return productRepository.findById(productId)
                .map(product -> mapToDTO(product, new ProductDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final ProductDTO productDTO) {
        final Product product = new Product();
        mapToEntity(productDTO, product);
        return productRepository.save(product).getProductId();
    }

    public void update(final UUID productId, final ProductDTO productDTO) {
        final Product product = productRepository.findById(productId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(productDTO, product);
        productRepository.save(product);
    }

    public void delete(final UUID productId) {
        productRepository.deleteById(productId);
    }

    private ProductDTO mapToDTO(final Product product, final ProductDTO productDTO) {
        productDTO.setProductId(product.getProductId());
        productDTO.setProductName(product.getProductName());
        productDTO.setSizeGuidImg(product.getSizeGuidImg());
        productDTO.setDescription(product.getDescription());
        productDTO.setDiscount(product.getDiscount());
        productDTO.setBrand(product.getBrand());
        productDTO.setUnitPrice(product.getUnitPrice());
        productDTO.setGender(product.getGender());
        productDTO.setCategory(product.getCategory() == null ? null : product.getCategory().getCategoryId());
        return productDTO;
    }

    private Product mapToEntity(final ProductDTO productDTO, final Product product) {
        product.setProductName(productDTO.getProductName());
        product.setSizeGuidImg(productDTO.getSizeGuidImg());
        product.setDescription(productDTO.getDescription());
        product.setDiscount(productDTO.getDiscount());
        product.setBrand(productDTO.getBrand());
        product.setUnitPrice(productDTO.getUnitPrice());
        product.setGender(productDTO.getGender());
        final Category category = productDTO.getCategory() == null ? null : categoryRepository.findById(productDTO.getCategory())
                .orElseThrow(() -> new NotFoundException("category not found"));
        product.setCategory(category);
        return product;
    }

    public ReferencedWarning getReferencedWarning(final UUID productId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Product product = productRepository.findById(productId)
                .orElseThrow(NotFoundException::new);
        final Variety productVariety = varietyRepository.findFirstByProduct(product);
        if (productVariety != null) {
            referencedWarning.setKey("product.variety.product.referenced");
            referencedWarning.addParam(productVariety.getVarietyId());
            return referencedWarning;
        }
        final SelectedItem productSelectedItem = selectedItemRepository.findFirstByProduct(product);
        if (productSelectedItem != null) {
            referencedWarning.setKey("product.selectedItem.product.referenced");
            referencedWarning.addParam(productSelectedItem.getSelectedItemId());
            return referencedWarning;
        }
        final OrderItem productOrderItem = orderItemRepository.findFirstByProduct(product);
        if (productOrderItem != null) {
            referencedWarning.setKey("product.orderItem.product.referenced");
            referencedWarning.addParam(productOrderItem.getOrderItemId());
            return referencedWarning;
        }
        final Review productReview = reviewRepository.findFirstByProduct(product);
        if (productReview != null) {
            referencedWarning.setKey("product.review.product.referenced");
            referencedWarning.addParam(productReview.getReviewId());
            return referencedWarning;
        }
        return null;
    }

}
