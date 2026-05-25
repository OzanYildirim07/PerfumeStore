package com.perfumestore.controller;

import com.perfumestore.dto.request.OrderCreateRequest;
import com.perfumestore.dto.response.OrderResponse;
import com.perfumestore.service.abstracts.IOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Orders", description = "Order management APIs")
@SecurityRequirement(name = "bearerAuth")
public class OrderController {

    private final IOrderService orderService;

    public OrderController(IOrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    @Operation(summary = "Get all orders", description = "Retrieve all orders (Admin only)")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/my-orders")
    @Operation(summary = "Get my orders", description = "Retrieve current user's orders")
    public ResponseEntity<List<OrderResponse>> getMyOrders() {
        return ResponseEntity.ok(orderService.getOrdersByCurrentUser());
    }

    @GetMapping("/best-3")
    @Operation(summary = "Get top 3 most expensive orders", description = "Retrieve the 3 highest priced orders in the system for dashboard metrics")
    public ResponseEntity<List<OrderResponse>> getTop3ExpensiveOrders() {
        // Az önce servis katmanında düzenlediğimiz o canavar metodu çağırıyoruz:
        return ResponseEntity.ok(orderService.getTop3MostExpensiveOrders());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID", description = "Retrieve a specific order by ID")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PostMapping
    @Operation(summary = "Create order", description = "Create a new order with payment")
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(request));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update order status", description = "Update the status of an order")
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
    }
}
