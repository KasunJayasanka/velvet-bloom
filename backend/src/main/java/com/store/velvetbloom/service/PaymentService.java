package com.store.velvetbloom.service;

import com.store.velvetbloom.domain.Orders;
import com.store.velvetbloom.domain.Payment;
import com.store.velvetbloom.model.PaymentDTO;
import com.store.velvetbloom.repository.OrdersRepository;
import com.store.velvetbloom.repository.PaymentRepository;
import com.store.velvetbloom.util.NotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrdersRepository ordersRepository;

    public PaymentService(final PaymentRepository paymentRepository,
            final OrdersRepository ordersRepository) {
        this.paymentRepository = paymentRepository;
        this.ordersRepository = ordersRepository;
    }

    public List<PaymentDTO> findAll() {
        final List<Payment> payments = paymentRepository.findAll(Sort.by("paymentId"));
        return payments.stream()
                .map(payment -> mapToDTO(payment, new PaymentDTO()))
                .toList();
    }

    public PaymentDTO get(final UUID paymentId) {
        return paymentRepository.findById(paymentId)
                .map(payment -> mapToDTO(payment, new PaymentDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final PaymentDTO paymentDTO) {
        final Payment payment = new Payment();
        mapToEntity(paymentDTO, payment);
        return paymentRepository.save(payment).getPaymentId();
    }

    public void update(final UUID paymentId, final PaymentDTO paymentDTO) {
        final Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(paymentDTO, payment);
        paymentRepository.save(payment);
    }

    public void delete(final UUID paymentId) {
        paymentRepository.deleteById(paymentId);
    }

    private PaymentDTO mapToDTO(final Payment payment, final PaymentDTO paymentDTO) {
        paymentDTO.setPaymentId(payment.getPaymentId());
        paymentDTO.setPaymentMethod(payment.getPaymentMethod());
        paymentDTO.setOrder(payment.getOrder() == null ? null : payment.getOrder().getOrderId());
        return paymentDTO;
    }

    private Payment mapToEntity(final PaymentDTO paymentDTO, final Payment payment) {
        payment.setPaymentMethod(paymentDTO.getPaymentMethod());
        final Orders order = paymentDTO.getOrder() == null ? null : ordersRepository.findById(paymentDTO.getOrder())
                .orElseThrow(() -> new NotFoundException("order not found"));
        payment.setOrder(order);
        return payment;
    }

}
