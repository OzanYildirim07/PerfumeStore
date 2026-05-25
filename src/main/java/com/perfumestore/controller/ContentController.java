package com.perfumestore.controller;

import com.perfumestore.dto.response.ContentResponse;
import com.perfumestore.service.abstracts.IContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/contents")
@Tag(name = "Contents", description = "Content/Note management APIs")
public class ContentController {

    private final IContentService contentService;

    public ContentController(IContentService contentService) {
        this.contentService = contentService;
    }

    @GetMapping
    @Operation(summary = "Get all contents", description = "Retrieve all perfume contents/notes")
    public ResponseEntity<List<ContentResponse>> getAllContents() {
        return ResponseEntity.ok(contentService.getAllContents());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get content by ID", description = "Retrieve a specific content by ID")
    public ResponseEntity<ContentResponse> getContentById(@PathVariable Long id) {
        return ResponseEntity.ok(contentService.getContentById(id));
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Get contents by type", description = "Retrieve contents by type (MEYVE, BAHARAT, ODUNSU, FRESH, AMBER)")
    public ResponseEntity<List<ContentResponse>> getContentsByType(@PathVariable String type) {
        return ResponseEntity.ok(contentService.getContentsByType(type));
    }
}
