package com.perfumestore.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponse {

    private Long id;
    private Long perfumeId;
    private String perfumeName;
    private String perfumeImageUrl;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
}
