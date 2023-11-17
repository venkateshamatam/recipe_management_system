package com.esd.recipe.service;

import com.esd.recipe.model.Recipe;
import com.esd.recipe.model.User;

import java.util.List;

public interface RecipeService {


    void delete(int id);
    void save(int userId, Recipe recipe);
    Recipe get(int id);
    Recipe update(int id, Recipe recipe);

    List<Recipe> get();

    List<Recipe> search(String query);
    public List<Recipe> searchByIngredient(String query);
    Recipe addCategory(int recipeId, int categoryId);

    List<Recipe> getByUser(int userID);

//    public void addRating(int recipeId, int value, User user);
//    public void addReview(int recipeId, String text, User user);
//
    }



