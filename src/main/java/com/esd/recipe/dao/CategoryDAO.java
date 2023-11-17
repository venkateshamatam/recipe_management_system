package com.esd.recipe.dao;

import com.esd.recipe.model.Category;

import java.util.List;

public interface CategoryDAO {
    Category save(Category category);
    List<Category> findAll();
}
