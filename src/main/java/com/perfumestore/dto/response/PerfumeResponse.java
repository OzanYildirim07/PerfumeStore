package com.perfumestore.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerfumeResponse {

    private Long id;
    private String name;
    private String brand;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private String gender;
    private Integer volumeMl;
    private String categoryName;
    private List<String> contents;
}
