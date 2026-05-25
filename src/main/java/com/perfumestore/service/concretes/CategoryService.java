package com.perfumestore.service.concretes;

import com.perfumestore.dto.response.CategoryResponse;
import com.perfumestore.entity.Category;
import com.perfumestore.exception.ResourceNotFoundException;
import com.perfumestore.mapper.CategoryMapper;
import com.perfumestore.repository.CategoryRepository;
import com.perfumestore.service.abstracts.ICategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryMapper.toResponseList(categoryRepository.findAll());
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        return categoryMapper.toResponse(category);
    }

    @Override
    public CategoryResponse getCategoryByName(String name) {
        Category category = categoryRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with name: " + name));
        return categoryMapper.toResponse(category);
    }
}
