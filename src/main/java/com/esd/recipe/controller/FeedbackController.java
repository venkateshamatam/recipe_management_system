package com.esd.recipe.controller;

import com.esd.recipe.model.Rating;
import com.esd.recipe.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {
        "http://localhost:3000",
        "https://localhost:3000",
        "http://127.0.0.1:3000",
        "https://127.0.0.1:3000",
        "http://[::1]:3000",
        "https://[::1]:3000"
}, allowCredentials = "true")
public class FeedbackController {

    @Autowired
    private RatingService ratingService;
    @PostMapping("/ratings")
    public ResponseEntity<Rating> addRating(@RequestParam("userId") int userId, @RequestParam("recipeId") int recipeId, @RequestParam("value") Double value, @RequestBody String comments) {
        Rating rating = ratingService.save(userId, recipeId, value, comments);
        return new ResponseEntity<>(rating, HttpStatus.CREATED);
    }

    @GetMapping("/recipes/{recipeId}/averageRating")
    public ResponseEntity<Float> getAverageRatingByRecipeId(@PathVariable("recipeId") int recipeId) {
        Float averageRating = ratingService.getAverageRatingByRecipeId(recipeId);
        if (averageRating != null) {
            return new ResponseEntity<>(averageRating, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
