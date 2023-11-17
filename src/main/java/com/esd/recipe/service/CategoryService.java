package com.esd.recipe.service;

import com.esd.recipe.model.Category;

import java.util.List;

public interface CategoryService {
    public Category save(Category category);
    List<Category> getAllCategories();
}
