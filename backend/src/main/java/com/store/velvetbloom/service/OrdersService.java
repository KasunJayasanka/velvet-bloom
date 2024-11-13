package com.store.velvetbloom.service;

import com.store.velvetbloom.domain.Customer;
import com.store.velvetbloom.domain.OrderItem;
import com.store.velvetbloom.domain.Orders;
import com.store.velvetbloom.domain.Payment;
import com.store.velvetbloom.model.OrdersDTO;
import com.store.velvetbloom.repository.CustomerRepository;
import com.store.velvetbloom.repository.OrderItemRepository;
import com.store.velvetbloom.repository.OrdersRepository;
import com.store.velvetbloom.repository.PaymentRepository;
import com.store.velvetbloom.util.NotFoundException;
import com.store.velvetbloom.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class OrdersService {

    private final OrdersRepository ordersRepository;
    private final CustomerRepository customerRepository;
    private final OrderItemRepository orderItemRepository;
    private final PaymentRepository paymentRepository;

    public OrdersService(final OrdersRepository ordersRepository,
            final CustomerRepository customerRepository,
            final OrderItemRepository orderItemRepository,
            final PaymentRepository paymentRepository) {
        this.ordersRepository = ordersRepository;
        this.customerRepository = customerRepository;
        this.orderItemRepository = orderItemRepository;
        this.paymentRepository = paymentRepository;
    }

    public List<OrdersDTO> findAll() {
        final List<Orders> orderses = ordersRepository.findAll(Sort.by("orderId"));
        return orderses.stream()
                .map(orders -> mapToDTO(orders, new OrdersDTO()))
                .toList();
    }

    public OrdersDTO get(final UUID orderId) {
        return ordersRepository.findById(orderId)
                .map(orders -> mapToDTO(orders, new OrdersDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final OrdersDTO ordersDTO) {
        final Orders orders = new Orders();
        mapToEntity(ordersDTO, orders);
        return ordersRepository.save(orders).getOrderId();
    }

    public void update(final UUID orderId, final OrdersDTO ordersDTO) {
        final Orders orders = ordersRepository.findById(orderId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(ordersDTO, orders);
        ordersRepository.save(orders);
    }

    public void delete(final UUID orderId) {
        ordersRepository.deleteById(orderId);
    }

    private OrdersDTO mapToDTO(final Orders orders, final OrdersDTO ordersDTO) {
        ordersDTO.setOrderId(orders.getOrderId());
        ordersDTO.setOrderDate(orders.getOrderDate());
        ordersDTO.setOrderStatus(orders.getOrderStatus());
        ordersDTO.setDeliveredDate(orders.getDeliveredDate());
        ordersDTO.setTotalAmount(orders.getTotalAmount());
        ordersDTO.setCustomer(orders.getCustomer() == null ? null : orders.getCustomer().getCustomerId());
        return ordersDTO;
    }

    private Orders mapToEntity(final OrdersDTO ordersDTO, final Orders orders) {
        orders.setOrderDate(ordersDTO.getOrderDate());
        orders.setOrderStatus(ordersDTO.getOrderStatus());
        orders.setDeliveredDate(ordersDTO.getDeliveredDate());
        orders.setTotalAmount(ordersDTO.getTotalAmount());
        final Customer customer = ordersDTO.getCustomer() == null ? null : customerRepository.findById(ordersDTO.getCustomer())
                .orElseThrow(() -> new NotFoundException("customer not found"));
        orders.setCustomer(customer);
        return orders;
    }

    public ReferencedWarning getReferencedWarning(final UUID orderId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Orders orders = ordersRepository.findById(orderId)
                .orElseThrow(NotFoundException::new);
        final OrderItem orderOrderItem = orderItemRepository.findFirstByOrder(orders);
        if (orderOrderItem != null) {
            referencedWarning.setKey("orders.orderItem.order.referenced");
            referencedWarning.addParam(orderOrderItem.getOrderItemId());
            return referencedWarning;
        }
        final Payment orderPayment = paymentRepository.findFirstByOrder(orders);
        if (orderPayment != null) {
            referencedWarning.setKey("orders.payment.order.referenced");
            referencedWarning.addParam(orderPayment.getPaymentId());
            return referencedWarning;
        }
        return null;
    }

}
