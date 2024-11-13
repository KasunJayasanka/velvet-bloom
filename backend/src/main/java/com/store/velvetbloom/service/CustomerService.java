package com.store.velvetbloom.service;

import com.store.velvetbloom.domain.Cart;
import com.store.velvetbloom.domain.Customer;
import com.store.velvetbloom.domain.Orders;
import com.store.velvetbloom.domain.Review;
import com.store.velvetbloom.model.CustomerDTO;
import com.store.velvetbloom.repository.CartRepository;
import com.store.velvetbloom.repository.CustomerRepository;
import com.store.velvetbloom.repository.OrdersRepository;
import com.store.velvetbloom.repository.ReviewRepository;
import com.store.velvetbloom.util.NotFoundException;
import com.store.velvetbloom.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CartRepository cartRepository;
    private final OrdersRepository ordersRepository;
    private final ReviewRepository reviewRepository;

    public CustomerService(final CustomerRepository customerRepository,
            final CartRepository cartRepository, final OrdersRepository ordersRepository,
            final ReviewRepository reviewRepository) {
        this.customerRepository = customerRepository;
        this.cartRepository = cartRepository;
        this.ordersRepository = ordersRepository;
        this.reviewRepository = reviewRepository;
    }

    public List<CustomerDTO> findAll() {
        final List<Customer> customers = customerRepository.findAll(Sort.by("customerId"));
        return customers.stream()
                .map(customer -> mapToDTO(customer, new CustomerDTO()))
                .toList();
    }

    public CustomerDTO get(final UUID customerId) {
        return customerRepository.findById(customerId)
                .map(customer -> mapToDTO(customer, new CustomerDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final CustomerDTO customerDTO) {
        final Customer customer = new Customer();
        mapToEntity(customerDTO, customer);
        return customerRepository.save(customer).getCustomerId();
    }

    public void update(final UUID customerId, final CustomerDTO customerDTO) {
        final Customer customer = customerRepository.findById(customerId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(customerDTO, customer);
        customerRepository.save(customer);
    }

    public void delete(final UUID customerId) {
        customerRepository.deleteById(customerId);
    }

    private CustomerDTO mapToDTO(final Customer customer, final CustomerDTO customerDTO) {
        customerDTO.setCustomerId(customer.getCustomerId());
        customerDTO.setFirstname(customer.getFirstname());
        customerDTO.setLastname(customer.getLastname());
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setMobileNo(customer.getMobileNo());
        customerDTO.setFirstAddress(customer.getFirstAddress());
        customerDTO.setSecondAddress(customer.getSecondAddress());
        customerDTO.setDistrict(customer.getDistrict());
        customerDTO.setPassword(customer.getPassword());
        return customerDTO;
    }

    private Customer mapToEntity(final CustomerDTO customerDTO, final Customer customer) {
        customer.setFirstname(customerDTO.getFirstname());
        customer.setLastname(customerDTO.getLastname());
        customer.setEmail(customerDTO.getEmail());
        customer.setMobileNo(customerDTO.getMobileNo());
        customer.setFirstAddress(customerDTO.getFirstAddress());
        customer.setSecondAddress(customerDTO.getSecondAddress());
        customer.setDistrict(customerDTO.getDistrict());
        customer.setPassword(customerDTO.getPassword());
        return customer;
    }

    public ReferencedWarning getReferencedWarning(final UUID customerId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Customer customer = customerRepository.findById(customerId)
                .orElseThrow(NotFoundException::new);
        final Cart customerCart = cartRepository.findFirstByCustomer(customer);
        if (customerCart != null) {
            referencedWarning.setKey("customer.cart.customer.referenced");
            referencedWarning.addParam(customerCart.getCartId());
            return referencedWarning;
        }
        final Orders customerOrders = ordersRepository.findFirstByCustomer(customer);
        if (customerOrders != null) {
            referencedWarning.setKey("customer.orders.customer.referenced");
            referencedWarning.addParam(customerOrders.getOrderId());
            return referencedWarning;
        }
        final Review customerReview = reviewRepository.findFirstByCustomer(customer);
        if (customerReview != null) {
            referencedWarning.setKey("customer.review.customer.referenced");
            referencedWarning.addParam(customerReview.getReviewId());
            return referencedWarning;
        }
        return null;
    }

}
