package com.perfumestore.service.abstracts;

import com.perfumestore.dto.request.PerfumeCreateRequest;
import com.perfumestore.dto.request.PerfumeUpdateRequest;
import com.perfumestore.dto.response.PerfumeResponse;
import com.perfumestore.entity.Perfume;

import java.math.BigDecimal;
import java.util.List;

public interface IPerfumeService {

    List<PerfumeResponse> getAllPerfumes();

    PerfumeResponse getPerfumeById(Long id);

    List<PerfumeResponse> searchPerfumes(String query);

    List<PerfumeResponse> getPerfumesByGender(Perfume.Gender gender);

    List<PerfumeResponse> getPerfumesByPriceLessThan(BigDecimal price);

    //List<PerfumeResponse> getPerfumesByNotesContaining(String note);
    List<PerfumeResponse> getTop3Bestsellers();

    List<PerfumeResponse> getPerfumesByGenderAndNotes(Perfume.Gender gender, String note);

    List<PerfumeResponse> getPerfumesByCategory(Long categoryId);

    PerfumeResponse createPerfume(PerfumeCreateRequest request);

    PerfumeResponse updatePerfume(Long id, PerfumeUpdateRequest request);

    void deletePerfume(Long id);
}
