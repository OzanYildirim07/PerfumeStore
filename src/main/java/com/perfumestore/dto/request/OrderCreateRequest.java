package com.perfumestore.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateRequest {

    @NotEmpty(message = "Order items are required")
    @Valid
    private List<OrderItemRequest> items;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    public enum PaymentMethod {
        CREDIT_CARD, DEBIT_CARD, PAYPAL, BANK_TRANSFER
    }
}
