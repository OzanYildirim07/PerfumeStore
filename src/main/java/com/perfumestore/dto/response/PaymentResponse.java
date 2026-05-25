package com.perfumestore.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private Long id;
    private BigDecimal amount;
    private String paymentMethod;
    private String status;
    private String transactionId;
    private LocalDateTime createdAt;
}
