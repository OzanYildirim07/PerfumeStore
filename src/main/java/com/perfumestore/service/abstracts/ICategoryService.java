package com.perfumestore.service.abstracts;

import com.perfumestore.dto.response.CategoryResponse;

import java.util.List;

public interface ICategoryService {

    List<CategoryResponse> getAllCategories();

    CategoryResponse getCategoryById(Long id);

    CategoryResponse getCategoryByName(String name);
}
