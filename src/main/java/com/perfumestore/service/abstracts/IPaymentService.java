package com.perfumestore.service.abstracts;

import com.perfumestore.dto.response.PaymentResponse;

public interface IPaymentService {

    PaymentResponse getPaymentByOrderId(Long orderId);

    PaymentResponse processPayment(Long orderId);
}
