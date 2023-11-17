package com.esd.recipe.dao;

import com.esd.recipe.model.Recipe;
import com.esd.recipe.model.User;

import java.util.List;

public interface RecipeDAO {

    void delete(int id);
    void save(int userId, Recipe recipe);
    Recipe get(int id);
    List<Recipe> get();


    List<Recipe> getByUser(int userId);

    List<Recipe> search(String query);
    public List<Recipe> searchByIngredient(String query);
    Recipe addCategory(int recipeId, int categoryId);
    Recipe update(Recipe recipe);



//    public void addRating(int recipeId, int value, User user);
//
//    public void addReview(int recipeId, String text, User user);
    }
