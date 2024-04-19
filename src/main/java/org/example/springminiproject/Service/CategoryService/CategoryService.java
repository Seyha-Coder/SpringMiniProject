package org.example.springminiproject.Service.CategoryService;


import org.example.springminiproject.Model.Category.Category;
import org.example.springminiproject.Model.Category.CategoryDTO;
import org.example.springminiproject.Model.Category.CategoryRequest;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    List<Category> getAllCategory(Integer offset, Integer limit);


    Category getCategoryById(UUID id);

    Category createCategory(CategoryRequest categoryRequest,UUID userId);


    Category updateCategory(UUID id, CategoryRequest categoryRequest);

    Category deleteCategory(UUID id);
}
