package com.esd.recipe.controller;

import com.esd.recipe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FavoriteController {

    @Autowired
    private UserService userService;


    @PostMapping("/user/{userId}/favorites/add")
    public ResponseEntity<Void> addFavorite(@PathVariable("userId") int userId, @RequestParam("recipeId") int recipeId) {
        userService.addFavorite(userId, recipeId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/user/{userId}/favorites/remove")
    public ResponseEntity<Void> removeFavorite(@PathVariable("userId") int userId, @RequestParam("recipeId") int recipeId) {
        userService.removeFavorite(userId, recipeId);
        return ResponseEntity.ok().build();
    }
}
