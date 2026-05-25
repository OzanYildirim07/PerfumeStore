package com.perfumestore.service.concretes;

import com.perfumestore.dto.request.OrderCreateRequest;
import com.perfumestore.dto.request.OrderItemRequest;
import com.perfumestore.dto.response.OrderResponse;
import com.perfumestore.entity.Order;
import com.perfumestore.entity.OrderItem;
import com.perfumestore.entity.Payment;
import com.perfumestore.entity.Perfume;
import com.perfumestore.entity.User;
import com.perfumestore.exception.BusinessException;
import com.perfumestore.exception.ResourceNotFoundException;
import com.perfumestore.mapper.OrderMapper;
import com.perfumestore.repository.OrderRepository;
import com.perfumestore.repository.PaymentRepository;
import com.perfumestore.repository.PerfumeRepository;
import com.perfumestore.service.abstracts.IOrderService;
import com.perfumestore.service.abstracts.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final PerfumeRepository perfumeRepository;
    private final PaymentRepository paymentRepository;
    private final OrderMapper orderMapper;
    private final IUserService userService;

    public OrderService(OrderRepository orderRepository, PerfumeRepository perfumeRepository,
                        PaymentRepository paymentRepository, OrderMapper orderMapper, IUserService userService) {
        this.orderRepository = orderRepository;
        this.perfumeRepository = perfumeRepository;
        this.paymentRepository = paymentRepository;
        this.orderMapper = orderMapper;
        this.userService = userService;
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return orderMapper.toResponseList(orderRepository.findAll());
    }

    @Override
    public List<OrderResponse> getOrdersByCurrentUser() {
        User currentUser = userService.getCurrentUser();
        return orderMapper.toResponseList(orderRepository.findByUserId(currentUser.getId()));
    }

    @Override
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return orderMapper.toResponse(order);
    }

    @Override
    public List<OrderResponse> getTop3MostExpensiveOrders() {
        List<Order> orders = orderRepository.findTop3ByOrderByTotalAmountDesc();

        return orderMapper.toResponseList(orders);
    }
    @Override
    @Transactional
    public OrderResponse createOrder(OrderCreateRequest request) {
        User currentUser = userService.getCurrentUser();
        Order order = new Order();
        order.setUser(currentUser);
        order.setStatus(Order.OrderStatus.PENDING);
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderItemRequest itemRequest : request.getItems()) {
            Perfume perfume = perfumeRepository.findById(itemRequest.getPerfumeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Perfume not found with id: " + itemRequest.getPerfumeId()));
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setPerfume(perfume);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setUnitPrice(perfume.getPrice());
            orderItem.calculateSubtotal();
            orderItems.add(orderItem);
            totalAmount = totalAmount.add(orderItem.getSubtotal());
        }
        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);
        Order savedOrder = orderRepository.save(order);
        Payment payment = new Payment();
        payment.setOrder(savedOrder);
        payment.setAmount(totalAmount);
        payment.setPaymentMethod(Payment.PaymentMethod.valueOf(request.getPaymentMethod().name()));
        payment.setTransactionId(UUID.randomUUID().toString());
        paymentRepository.save(payment);
        savedOrder.setPayment(payment);
        return orderMapper.toResponse(savedOrder);
    }

    @Override
    public OrderResponse updateOrderStatus(Long id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        try {
            Order.OrderStatus newStatus = Order.OrderStatus.valueOf(status.toUpperCase());
            order.setStatus(newStatus);
            Order updatedOrder = orderRepository.save(order);
            return orderMapper.toResponse(updatedOrder);
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Invalid order status: " + status);
        }
    }
}
