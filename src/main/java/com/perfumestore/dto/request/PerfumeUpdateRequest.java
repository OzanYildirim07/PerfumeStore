package com.perfumestore.dto.request;

import com.perfumestore.entity.Perfume;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerfumeUpdateRequest {

    @NotBlank(message = "Name is required")
    @Size(max = 200, message = "Name must not exceed 200 characters")
    private String name;

    @NotBlank(message = "Brand is required")
    @Size(max = 100, message = "Brand must not exceed 100 characters")
    private String brand;

    @NotBlank(message = "Description is required")
    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;

    @NotBlank(message = "Image URL is required")
    @Size(max = 500, message = "Image URL must not exceed 500 characters")
    private String imageUrl;

    @NotNull(message = "Gender is required")
    private Perfume.Gender gender;

    @NotNull(message = "Volume is required")
    @Positive(message = "Volume must be positive")
    private Integer volumeMl;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    @NotNull(message = "Content IDs are required")
    @Size(min = 1, max = 5, message = "Content IDs must contain between 1 and 5 items")
    private List<Long> contentIds;
}
