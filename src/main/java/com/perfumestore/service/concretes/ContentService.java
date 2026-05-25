package com.perfumestore.service.concretes;

import com.perfumestore.dto.response.ContentResponse;
import com.perfumestore.entity.Content;
import com.perfumestore.exception.ResourceNotFoundException;
import com.perfumestore.mapper.ContentMapper;
import com.perfumestore.repository.ContentRepository;
import com.perfumestore.service.abstracts.IContentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContentService implements IContentService {

    private final ContentRepository contentRepository;
    private final ContentMapper contentMapper;

    public ContentService(ContentRepository contentRepository, ContentMapper contentMapper) {
        this.contentRepository = contentRepository;
        this.contentMapper = contentMapper;
    }

    @Override
    public List<ContentResponse> getAllContents() {
        return contentMapper.toResponseList(contentRepository.findAll());
    }

    @Override
    public ContentResponse getContentById(Long id) {
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Content not found with id: " + id));
        return contentMapper.toResponse(content);
    }

    @Override
    public List<ContentResponse> getContentsByType(String type) {
        try {
            Content.ContentType contentType = Content.ContentType.valueOf(type.toUpperCase());
            return contentMapper.toResponseList(contentRepository.findByType(contentType));
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("Invalid content type: " + type);
        }
    }
}
