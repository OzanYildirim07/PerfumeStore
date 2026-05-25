package com.perfumestore.controller;

import com.perfumestore.dto.request.PerfumeCreateRequest;
import com.perfumestore.dto.request.PerfumeUpdateRequest;
import com.perfumestore.dto.response.PerfumeResponse;
import com.perfumestore.entity.Perfume;
import com.perfumestore.service.abstracts.IPerfumeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/perfumes")
@Tag(name = "Perfumes", description = "Perfume management APIs")
public class PerfumeController {

    private final IPerfumeService perfumeService;

    public PerfumeController(IPerfumeService perfumeService) {
        this.perfumeService = perfumeService;
    }

    @GetMapping
    @Operation(summary = "Get all perfumes", description = "Retrieve all perfumes")
    public ResponseEntity<List<PerfumeResponse>> getAllPerfumes() {
        return ResponseEntity.ok(perfumeService.getAllPerfumes());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get perfume by ID", description = "Retrieve a specific perfume by ID")
    public ResponseEntity<PerfumeResponse> getPerfumeById(@PathVariable Long id) {
        return ResponseEntity.ok(perfumeService.getPerfumeById(id));
    }
    @GetMapping("/bestsellers")
    public ResponseEntity<List<PerfumeResponse>> getBestsellers() {
        return ResponseEntity.ok(perfumeService.getTop3Bestsellers());
    }
    @GetMapping("/filter-advanced")
    public ResponseEntity<List<PerfumeResponse>> getAdvancedFilter(
            @RequestParam String gender,
            @RequestParam String note) {

        Perfume.Gender genderEnum = Perfume.Gender.valueOf(gender.toUpperCase());

        return ResponseEntity.ok(perfumeService.getPerfumesByGenderAndNotes(genderEnum, note));
    }
    @GetMapping("/search")
    @Operation(summary = "Search perfumes", description = "Search perfumes by name, brand, or notes")
    public ResponseEntity<List<PerfumeResponse>> searchPerfumes(@RequestParam String query) {
        return ResponseEntity.ok(perfumeService.searchPerfumes(query));
    }

    @GetMapping("/gender/{gender}")
    @Operation(summary = "Get perfumes by gender", description = "Filter perfumes by gender (ERKEK, KADIN, UNISEX)")
    public ResponseEntity<List<PerfumeResponse>> getPerfumesByGender(@PathVariable String gender) {
        return ResponseEntity.ok(perfumeService.getPerfumesByGender(Perfume.Gender.valueOf(gender.toUpperCase())));
    }

    @GetMapping("/price-less-than")
    @Operation(summary = "Get perfumes by price", description = "Get perfumes with price less than specified value")
    public ResponseEntity<List<PerfumeResponse>> getPerfumesByPriceLessThan(@RequestParam BigDecimal price) {
        return ResponseEntity.ok(perfumeService.getPerfumesByPriceLessThan(price));
    }


    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Get perfumes by category", description = "Get perfumes by category ID")
    public ResponseEntity<List<PerfumeResponse>> getPerfumesByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(perfumeService.getPerfumesByCategory(categoryId));
    }

    @PostMapping
    @Operation(summary = "Create perfume", description = "Create a new perfume (Admin only)")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<PerfumeResponse> createPerfume(@Valid @RequestBody PerfumeCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(perfumeService.createPerfume(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update perfume", description = "Update an existing perfume (Admin only)")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<PerfumeResponse> updatePerfume(@PathVariable Long id, @Valid @RequestBody PerfumeUpdateRequest request) {
        return ResponseEntity.ok(perfumeService.updatePerfume(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete perfume", description = "Delete a perfume by ID (Admin only)")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> deletePerfume(@PathVariable Long id) {
        perfumeService.deletePerfume(id);
        return ResponseEntity.noContent().build();
    }
}
