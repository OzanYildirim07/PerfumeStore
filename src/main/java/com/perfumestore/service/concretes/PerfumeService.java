package com.perfumestore.service.concretes;

import com.perfumestore.dto.request.PerfumeCreateRequest;
import com.perfumestore.dto.request.PerfumeUpdateRequest;
import com.perfumestore.dto.response.PerfumeResponse;
import com.perfumestore.entity.Category;
import com.perfumestore.entity.Content;
import com.perfumestore.entity.Perfume;
import com.perfumestore.exception.ResourceNotFoundException;
import com.perfumestore.mapper.PerfumeMapper;
import com.perfumestore.repository.CategoryRepository;
import com.perfumestore.repository.ContentRepository;
import com.perfumestore.repository.PerfumeRepository;
import com.perfumestore.service.abstracts.IPerfumeService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PerfumeService implements IPerfumeService {

    private final PerfumeRepository perfumeRepository;
    private final CategoryRepository categoryRepository;
    private final ContentRepository contentRepository;
    private final PerfumeMapper perfumeMapper;

    public PerfumeService(PerfumeRepository perfumeRepository, CategoryRepository categoryRepository,
                          ContentRepository contentRepository, PerfumeMapper perfumeMapper) {
        this.perfumeRepository = perfumeRepository;
        this.categoryRepository = categoryRepository;
        this.contentRepository = contentRepository;
        this.perfumeMapper = perfumeMapper;
    }

    @Override
    public List<PerfumeResponse> getAllPerfumes() {
        return perfumeMapper.toResponseList(perfumeRepository.findAll());
    }

    @Override
    public PerfumeResponse getPerfumeById(Long id) {
        Perfume perfume = perfumeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Perfume not found with id: " + id));
        return perfumeMapper.toResponse(perfume);
    }

    @Override
    public List<PerfumeResponse> searchPerfumes(String query) {
        List<Perfume> perfumes = perfumeRepository.findByNameContainingIgnoreCaseOrBrandContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrContentsNameContainingIgnoreCase(query, query, query, query);
        return perfumeMapper.toResponseList(perfumes);
    }

    @Override
    public List<PerfumeResponse> getPerfumesByGender(Perfume.Gender gender) {
        return perfumeMapper.toResponseList(perfumeRepository.findByGender(gender));
    }
    @Override
    public List<PerfumeResponse> getTop3Bestsellers() {
        return perfumeMapper.toResponseList(perfumeRepository.findTop3ByOrderByPriceDesc());
    }
    @Override
    public List<PerfumeResponse> getPerfumesByPriceLessThan(BigDecimal price) {
        return perfumeMapper.toResponseList(perfumeRepository.findByPriceLessThan(price));
    }

   /* @Override
    public List<PerfumeResponse> getPerfumesByNotesContaining(String note) {
        return perfumeMapper.toResponseList(perfumeRepository.findByNotesContaining(note));
    }*/

    @Override
    public List<PerfumeResponse> getPerfumesByGenderAndNotes(Perfume.Gender gender, String note) {
        return perfumeMapper.toResponseList(perfumeRepository.findByGenderAndContentsNameContainingIgnoreCase(gender, note));
    }

    @Override
    public List<PerfumeResponse> getPerfumesByCategory(Long categoryId) {
        return perfumeMapper.toResponseList(perfumeRepository.findByCategoryId(categoryId));
    }

    @Override
    public PerfumeResponse createPerfume(PerfumeCreateRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));
        List<Content> contents = contentRepository.findAllById(request.getContentIds());
        if (contents.isEmpty()) {
            throw new ResourceNotFoundException("No contents found for the given IDs");
        }
        Perfume perfume = perfumeMapper.toEntity(request);
        perfume.setCategory(category);
        perfume.setContents(contents);
        Perfume savedPerfume = perfumeRepository.save(perfume);
        return perfumeMapper.toResponse(savedPerfume);
    }

    @Override
    public PerfumeResponse updatePerfume(Long id, PerfumeUpdateRequest request) {
        Perfume perfume = perfumeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Perfume not found with id: " + id));
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));
        List<Content> contents = contentRepository.findAllById(request.getContentIds());
        if (contents.isEmpty()) {
            throw new ResourceNotFoundException("No contents found for the given IDs");
        }
        perfumeMapper.updateEntity(perfume, request);
        perfume.setCategory(category);
        perfume.setContents(contents);
        Perfume updatedPerfume = perfumeRepository.save(perfume);
        return perfumeMapper.toResponse(updatedPerfume);
    }

    @Override
    public void deletePerfume(Long id) {
        if (!perfumeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Perfume not found with id: " + id);
        }
        perfumeRepository.deleteById(id);
    }
}
