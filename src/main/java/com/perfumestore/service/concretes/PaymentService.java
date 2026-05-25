package com.perfumestore.service.concretes;

import com.perfumestore.dto.response.PaymentResponse;
import com.perfumestore.entity.Order;
import com.perfumestore.entity.Payment;
import com.perfumestore.exception.BusinessException;
import com.perfumestore.exception.ResourceNotFoundException;
import com.perfumestore.repository.OrderRepository;
import com.perfumestore.repository.PaymentRepository;
import com.perfumestore.service.abstracts.IPaymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentService implements IPaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentService(PaymentRepository paymentRepository, OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public PaymentResponse getPaymentByOrderId(Long orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for order id: " + orderId));
        return toResponse(payment);
    }

    @Override
    @Transactional
    public PaymentResponse processPayment(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        if (order.getPayment() != null && order.getPayment().getStatus() == Payment.PaymentStatus.COMPLETED) {
            throw new BusinessException("Payment already completed for this order");
        }
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElse(new Payment());
        payment.setOrder(order);
        payment.setAmount(order.getTotalAmount());
        if (payment.getPaymentMethod() == null) {
            payment.setPaymentMethod(Payment.PaymentMethod.CREDIT_CARD);
        }
        payment.setStatus(Payment.PaymentStatus.COMPLETED);
        if (payment.getTransactionId() == null) {
            payment.setTransactionId(UUID.randomUUID().toString());
        }
        if (payment.getCreatedAt() == null) {
            payment.setCreatedAt(LocalDateTime.now());
        }
        Payment savedPayment = paymentRepository.save(payment);
        order.setStatus(Order.OrderStatus.CONFIRMED);
        orderRepository.save(order);
        return toResponse(savedPayment);
    }

    private PaymentResponse toResponse(Payment payment) {
        PaymentResponse response = new PaymentResponse();
        response.setId(payment.getId());
        response.setAmount(payment.getAmount());
        response.setPaymentMethod(payment.getPaymentMethod().name());
        response.setStatus(payment.getStatus().name());
        response.setTransactionId(payment.getTransactionId());
        response.setCreatedAt(payment.getCreatedAt());
        return response;
    }
}
