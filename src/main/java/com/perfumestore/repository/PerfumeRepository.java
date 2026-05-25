package com.perfumestore.repository;

import com.perfumestore.entity.Perfume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PerfumeRepository extends JpaRepository<Perfume, Long> {

    List<Perfume> findByNameContainingIgnoreCase(String name);

    List<Perfume> findByGender(Perfume.Gender gender);

    List<Perfume> findByPriceLessThan(BigDecimal price);

    List<Perfume> findByCategoryId(Long categoryId);

    List<Perfume> findTop3ByOrderByPriceDesc();

    List<Perfume> findByGenderAndContentsNameContainingIgnoreCase(Perfume.Gender gender, String note);

    List<Perfume> findByNameContainingIgnoreCaseOrBrandContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrContentsNameContainingIgnoreCase(
            String name, String brand, String description, String contentName);
}