package com.store.velvetbloom.service;

import com.store.velvetbloom.domain.Customer;
import com.store.velvetbloom.domain.Product;
import com.store.velvetbloom.domain.Review;
import com.store.velvetbloom.model.ReviewDTO;
import com.store.velvetbloom.repository.CustomerRepository;
import com.store.velvetbloom.repository.ProductRepository;
import com.store.velvetbloom.repository.ReviewRepository;
import com.store.velvetbloom.util.NotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public ReviewService(final ReviewRepository reviewRepository,
            final CustomerRepository customerRepository,
            final ProductRepository productRepository) {
        this.reviewRepository = reviewRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    public List<ReviewDTO> findAll() {
        final List<Review> reviews = reviewRepository.findAll(Sort.by("reviewId"));
        return reviews.stream()
                .map(review -> mapToDTO(review, new ReviewDTO()))
                .toList();
    }

    public ReviewDTO get(final UUID reviewId) {
        return reviewRepository.findById(reviewId)
                .map(review -> mapToDTO(review, new ReviewDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final ReviewDTO reviewDTO) {
        final Review review = new Review();
        mapToEntity(reviewDTO, review);
        return reviewRepository.save(review).getReviewId();
    }

    public void update(final UUID reviewId, final ReviewDTO reviewDTO) {
        final Review review = reviewRepository.findById(reviewId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(reviewDTO, review);
        reviewRepository.save(review);
    }

    public void delete(final UUID reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    private ReviewDTO mapToDTO(final Review review, final ReviewDTO reviewDTO) {
        reviewDTO.setReviewId(review.getReviewId());
        reviewDTO.setDescription(review.getDescription());
        reviewDTO.setCustomer(review.getCustomer() == null ? null : review.getCustomer().getCustomerId());
        reviewDTO.setProduct(review.getProduct() == null ? null : review.getProduct().getProductId());
        return reviewDTO;
    }

    private Review mapToEntity(final ReviewDTO reviewDTO, final Review review) {
        review.setDescription(reviewDTO.getDescription());
        final Customer customer = reviewDTO.getCustomer() == null ? null : customerRepository.findById(reviewDTO.getCustomer())
                .orElseThrow(() -> new NotFoundException("customer not found"));
        review.setCustomer(customer);
        final Product product = reviewDTO.getProduct() == null ? null : productRepository.findById(reviewDTO.getProduct())
                .orElseThrow(() -> new NotFoundException("product not found"));
        review.setProduct(product);
        return review;
    }

}
