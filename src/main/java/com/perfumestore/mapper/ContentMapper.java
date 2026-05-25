package com.perfumestore.mapper;

import com.perfumestore.dto.response.ContentResponse;
import com.perfumestore.entity.Content;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ContentMapper {

    public ContentResponse toResponse(Content content) {
        if (content == null) {
            return null;
        }
        ContentResponse response = new ContentResponse();
        response.setId(content.getId());
        response.setName(content.getName());
        response.setType(content.getType().name());
        return response;
    }

    public List<ContentResponse> toResponseList(List<Content> contents) {
        return contents.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
