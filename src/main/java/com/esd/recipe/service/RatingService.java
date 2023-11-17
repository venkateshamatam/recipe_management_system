package com.esd.recipe.service;

import com.esd.recipe.model.Rating;
import com.esd.recipe.model.Recipe;
import com.esd.recipe.model.User;

import java.util.List;

public interface RatingService {

    Rating save(int userId, int recipeId, Double value, String comments);
    List<Rating> get();
    Rating get(int id);
    Float getAverageRatingByRecipeId(int recipeId);
}
