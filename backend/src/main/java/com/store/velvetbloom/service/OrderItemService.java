package com.store.velvetbloom.service;

import com.store.velvetbloom.domain.OrderItem;
import com.store.velvetbloom.domain.Orders;
import com.store.velvetbloom.domain.Product;
import com.store.velvetbloom.model.OrderItemDTO;
import com.store.velvetbloom.repository.OrderItemRepository;
import com.store.velvetbloom.repository.OrdersRepository;
import com.store.velvetbloom.repository.ProductRepository;
import com.store.velvetbloom.util.NotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrdersRepository ordersRepository;
    private final ProductRepository productRepository;

    public OrderItemService(final OrderItemRepository orderItemRepository,
            final OrdersRepository ordersRepository, final ProductRepository productRepository) {
        this.orderItemRepository = orderItemRepository;
        this.ordersRepository = ordersRepository;
        this.productRepository = productRepository;
    }

    public List<OrderItemDTO> findAll() {
        final List<OrderItem> orderItems = orderItemRepository.findAll(Sort.by("orderItemId"));
        return orderItems.stream()
                .map(orderItem -> mapToDTO(orderItem, new OrderItemDTO()))
                .toList();
    }

    public OrderItemDTO get(final UUID orderItemId) {
        return orderItemRepository.findById(orderItemId)
                .map(orderItem -> mapToDTO(orderItem, new OrderItemDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final OrderItemDTO orderItemDTO) {
        final OrderItem orderItem = new OrderItem();
        mapToEntity(orderItemDTO, orderItem);
        return orderItemRepository.save(orderItem).getOrderItemId();
    }

    public void update(final UUID orderItemId, final OrderItemDTO orderItemDTO) {
        final OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(orderItemDTO, orderItem);
        orderItemRepository.save(orderItem);
    }

    public void delete(final UUID orderItemId) {
        orderItemRepository.deleteById(orderItemId);
    }

    private OrderItemDTO mapToDTO(final OrderItem orderItem, final OrderItemDTO orderItemDTO) {
        orderItemDTO.setOrderItemId(orderItem.getOrderItemId());
        orderItemDTO.setQuantity(orderItem.getQuantity());
        orderItemDTO.setUnitPrice(orderItem.getUnitPrice());
        orderItemDTO.setOrder(orderItem.getOrder() == null ? null : orderItem.getOrder().getOrderId());
        orderItemDTO.setProduct(orderItem.getProduct() == null ? null : orderItem.getProduct().getProductId());
        return orderItemDTO;
    }

    private OrderItem mapToEntity(final OrderItemDTO orderItemDTO, final OrderItem orderItem) {
        orderItem.setQuantity(orderItemDTO.getQuantity());
        orderItem.setUnitPrice(orderItemDTO.getUnitPrice());
        final Orders order = orderItemDTO.getOrder() == null ? null : ordersRepository.findById(orderItemDTO.getOrder())
                .orElseThrow(() -> new NotFoundException("order not found"));
        orderItem.setOrder(order);
        final Product product = orderItemDTO.getProduct() == null ? null : productRepository.findById(orderItemDTO.getProduct())
                .orElseThrow(() -> new NotFoundException("product not found"));
        orderItem.setProduct(product);
        return orderItem;
    }

}
