package com.perfumestore.mapper;

import com.perfumestore.dto.response.OrderItemResponse;
import com.perfumestore.dto.response.OrderResponse;
import com.perfumestore.dto.response.PaymentResponse;
import com.perfumestore.entity.Order;
import com.perfumestore.entity.OrderItem;
import com.perfumestore.entity.Payment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderResponse toResponse(Order order) {
        if (order == null) {
            return null;
        }
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setUserId(order.getUser() != null ? order.getUser().getId() : null);
        response.setUsername(order.getUser() != null ? order.getUser().getUsername() : null);
        response.setTotalAmount(order.getTotalAmount());
        response.setStatus(order.getStatus().name());
        response.setCreatedAt(order.getCreatedAt());
        if (order.getOrderItems() != null) {
            response.setItems(order.getOrderItems().stream()
                    .map(this::toItemResponse)
                    .collect(Collectors.toList()));
        }
        if (order.getPayment() != null) {
            response.setPayment(toPaymentResponse(order.getPayment()));
        }
        return response;
    }

    public List<OrderResponse> toResponseList(List<Order> orders) {
        return orders.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private OrderItemResponse toItemResponse(OrderItem item) {
        if (item == null) {
            return null;
        }
        OrderItemResponse response = new OrderItemResponse();
        response.setId(item.getId());
        response.setPerfumeId(item.getPerfume() != null ? item.getPerfume().getId() : null);
        response.setPerfumeName(item.getPerfume() != null ? item.getPerfume().getName() : null);
        response.setPerfumeImageUrl(item.getPerfume() != null ? item.getPerfume().getImageUrl() : null);
        response.setQuantity(item.getQuantity());
        response.setUnitPrice(item.getUnitPrice());
        response.setSubtotal(item.getSubtotal());
        return response;
    }

    private PaymentResponse toPaymentResponse(Payment payment) {
        if (payment == null) {
            return null;
        }
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
