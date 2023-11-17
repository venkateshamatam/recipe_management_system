package com.esd.recipe.service;

import com.esd.recipe.dao.RecipeDAOImpl;
import com.esd.recipe.dao.UserDaoImpl;
import com.esd.recipe.model.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class RecipeServiceImpl implements RecipeService {

    private RecipeDAOImpl recipeDAO;

    private UserDaoImpl userDAO;
//    @Autowired
//    public RecipeServiceImpl(RecipeDAOImpl recipeDAO) {
//        this.recipeDAO = recipeDAO;
//    }

    @Autowired
    public RecipeServiceImpl(RecipeDAOImpl recipeDAO, UserDaoImpl userDAO) {
        this.recipeDAO = recipeDAO;
        this.userDAO = userDAO;
    }
    @Override
    public void delete(int id) {
        recipeDAO.delete(id);
    }

//    @Override
//    public void save(Recipe recipe) {
//        recipeDAO.save(recipe);
//    }

    @Override
    public void save(int userId, Recipe recipe) {
        recipeDAO.save(userId, recipe);
    }

//    @Override
//    public void save(int userId,Recipe recipe) {
//        String username = "user1";
//        User createdBy = userDAO.findByUsername(username);
//        recipe.setCreatedBy(createdBy);
//        recipeDAO.save(recipe);
//    }


    @Override
    public Recipe get(int id) {
        return recipeDAO.get(id);
    }

    @Override
    public List<Recipe> get() {
        // TODO Auto-generated method stub
        return recipeDAO.get();
    }

    @Override
    public List<Recipe> search(String query) {
        return recipeDAO.search(query);
    }

    @Override
    public List<Recipe> searchByIngredient(String query) {
        return recipeDAO.searchByIngredient(query);
    }

    @Override
    public Recipe update(int id, Recipe recipe) {
        Recipe existingRecipe = recipeDAO.get(id);
        if (existingRecipe == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found");
        }
        existingRecipe.setTitle(recipe.getTitle());
        existingRecipe.setDescription(recipe.getDescription());
        existingRecipe.setIngredients(recipe.getIngredients());
        existingRecipe.setCategories(recipe.getCategories());
        return recipeDAO.update(existingRecipe);
    }



    @Override
    public Recipe addCategory(int recipeId, int categoryId) {
        return recipeDAO.addCategory(recipeId, categoryId);
    }

    @Override
    public List<Recipe> getByUser(int userID) {
        return recipeDAO.getByUser(userID);
    }


}
