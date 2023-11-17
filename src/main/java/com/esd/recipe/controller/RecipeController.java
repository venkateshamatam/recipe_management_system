package com.esd.recipe.controller;
import com.esd.recipe.dao.UserDaoImpl;
import com.esd.recipe.model.Category;
import com.esd.recipe.model.Recipe;
import com.esd.recipe.model.User;
import com.esd.recipe.service.CategoryService;
import com.esd.recipe.service.RecipeService;
import com.esd.recipe.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {
        "http://localhost:3000",
        "https://localhost:3000",
        "http://127.0.0.1:3000",
        "https://127.0.0.1:3000",
        "http://[::1]:3000",
        "https://[::1]:3000"
}, allowCredentials = "true")
public class RecipeController {
    @Autowired
    private RecipeService recipeService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService UserService;

    @GetMapping("/getAllRecipes")
    public List<Recipe> get() {
        return recipeService.get();
    }

    @PostMapping("/user/{userId}/favorites/add")
    public ResponseEntity<Void> addFavorite(@PathVariable("userId") int userId, @RequestParam("recipeId") int recipeId) {
        UserService.addFavorite(userId, recipeId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/recipe")
    public ResponseEntity<Void> save(@RequestBody Recipe recipe, @RequestParam("createdByUserId") int createdByUserId) {
        recipeService.save(createdByUserId, recipe);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/recipe/{id}")
    public ResponseEntity<?> update(@PathVariable("id") int id, @RequestBody Recipe recipe, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }

        User currentUser =  (User) session.getAttribute("loggedInUser");
        int currentUserId = currentUser.id;

        Recipe existingRecipe = recipeService.get(id);

        if (existingRecipe == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recipe not found");
        }
        boolean isAdmin = currentUser.getRole() == User.Role.ADMIN;
        if (isAdmin || existingRecipe.getCreatedBy().equals(currentUserId)) {
            recipeService.update(id, recipe);
            return ResponseEntity.ok(recipe);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not authorized to update the recipe");
        }
    }

    @DeleteMapping("/recipe/{id}")
    public void delete(@PathVariable("id") int id) {
        recipeService.delete(id);
    }

    @GetMapping("/recipes/search")
    public List<Recipe> search(@RequestParam("q") String query) {
        return recipeService.search(query);
    }

    @GetMapping("/recipes/searchbyingredient")
    public List<Recipe> searchByIngredient(@RequestParam("q") String query) {
        return recipeService.searchByIngredient(query);
    }

    @PostMapping("/recipe/addCategory")
    public Recipe addCategory(@RequestParam("recipeId") int recipeId, @RequestParam("categoryId") int categoryId) {
        return recipeService.addCategory(recipeId, categoryId);
    }


    @PostMapping("/category")
    public Category createCategory(@RequestBody Category category) {
        categoryService.save(category);
        return category;
    }

    @GetMapping("/categories")
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/user/{userId}/recipes")
    public ResponseEntity<List<Recipe>> getRecipesByUserId(@PathVariable("userId") int userId) {
        List<Recipe> recipes = recipeService.getByUser(userId);
        return ResponseEntity.ok(recipes);
    }
}
