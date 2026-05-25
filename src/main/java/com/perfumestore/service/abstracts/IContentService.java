package com.perfumestore.service.abstracts;

import com.perfumestore.dto.response.ContentResponse;

import java.util.List;

public interface IContentService {

    List<ContentResponse> getAllContents();

    ContentResponse getContentById(Long id);

    List<ContentResponse> getContentsByType(String type);
}
