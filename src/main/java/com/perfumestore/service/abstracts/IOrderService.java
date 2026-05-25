package com.perfumestore.service.abstracts;

import com.perfumestore.dto.request.OrderCreateRequest;
import com.perfumestore.dto.response.OrderResponse;

import java.util.List;

public interface IOrderService {

    List<OrderResponse> getAllOrders();

    List<OrderResponse> getOrdersByCurrentUser();

    List<OrderResponse> getTop3MostExpensiveOrders();

    OrderResponse getOrderById(Long id);

    OrderResponse createOrder(OrderCreateRequest request);

    OrderResponse updateOrderStatus(Long id, String status);
}
