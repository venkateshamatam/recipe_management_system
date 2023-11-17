package com.esd.recipe.service;

import com.esd.recipe.dao.CategoryDAO;
import com.esd.recipe.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryDAO categoryDAO;

    @Override
    public Category save(Category category) {
        return categoryDAO.save(category);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryDAO.findAll();
    }

}
