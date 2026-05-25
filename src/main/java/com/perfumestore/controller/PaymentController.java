package com.perfumestore.controller;

import com.perfumestore.dto.response.PaymentResponse;
import com.perfumestore.service.abstracts.IPaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@Tag(name = "Payments", description = "Payment management APIs")
@SecurityRequirement(name = "bearerAuth")
public class PaymentController {

    private final IPaymentService paymentService;

    public PaymentController(IPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/order/{orderId}")
    @Operation(summary = "Get payment by order ID", description = "Retrieve payment details for a specific order")
    public ResponseEntity<PaymentResponse> getPaymentByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(paymentService.getPaymentByOrderId(orderId));
    }

    @PostMapping("/process/{orderId}")
    @Operation(summary = "Process payment", description = "Process payment for a specific order")
    public ResponseEntity<PaymentResponse> processPayment(@PathVariable Long orderId) {
        return ResponseEntity.ok(paymentService.processPayment(orderId));
    }
}
