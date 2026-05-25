package com.perfumestore.mapper;

import com.perfumestore.dto.request.PerfumeCreateRequest;
import com.perfumestore.dto.request.PerfumeUpdateRequest;
import com.perfumestore.dto.response.PerfumeResponse;
import com.perfumestore.entity.Content;
import com.perfumestore.entity.Perfume;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PerfumeMapper {

    public PerfumeResponse toResponse(Perfume perfume) {
        if (perfume == null) {
            return null;
        }
        PerfumeResponse response = new PerfumeResponse();
        response.setId(perfume.getId());
        response.setName(perfume.getName());
        response.setBrand(perfume.getBrand());
        response.setDescription(perfume.getDescription());
        response.setPrice(perfume.getPrice());
        response.setImageUrl(perfume.getImageUrl());
        response.setGender(perfume.getGender().name());
        response.setVolumeMl(perfume.getVolumeMl());
        response.setCategoryName(perfume.getCategory() != null ? perfume.getCategory().getName() : null);
        if (perfume.getContents() != null) {
            response.setContents(perfume.getContents().stream()
                    .map(Content::getName)
                    .collect(Collectors.toList()));
        }
        return response;
    }

    public List<PerfumeResponse> toResponseList(List<Perfume> perfumes) {
        return perfumes.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Perfume toEntity(PerfumeCreateRequest request) {
        if (request == null) {
            return null;
        }
        Perfume perfume = new Perfume();
        perfume.setName(request.getName());
        perfume.setBrand(request.getBrand());
        perfume.setDescription(request.getDescription());
        perfume.setPrice(request.getPrice());
        perfume.setImageUrl(request.getImageUrl());
        perfume.setGender(request.getGender());
        perfume.setVolumeMl(request.getVolumeMl());
        return perfume;
    }

    public void updateEntity(Perfume perfume, PerfumeUpdateRequest request) {
        if (perfume == null || request == null) {
            return;
        }
        perfume.setName(request.getName());
        perfume.setBrand(request.getBrand());
        perfume.setDescription(request.getDescription());
        perfume.setPrice(request.getPrice());
        perfume.setImageUrl(request.getImageUrl());
        perfume.setGender(request.getGender());
        perfume.setVolumeMl(request.getVolumeMl());
    }
}
