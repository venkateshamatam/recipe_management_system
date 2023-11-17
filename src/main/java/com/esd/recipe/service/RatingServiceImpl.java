package com.esd.recipe.service;

import com.esd.recipe.model.Rating;
import com.esd.recipe.dao.FeedbackDAO;
import com.esd.recipe.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RatingServiceImpl implements RatingService {

    @Autowired
    private FeedbackDAO feedbackDao;

    public List<Rating> get() {
        return feedbackDao.get();
    }

    @Transactional
    @Override
    public Rating get(int id) {
        return feedbackDao.get(id);
    }

    @Override
    public Float getAverageRatingByRecipeId(int recipeId) {
        return feedbackDao.getAverageRatingByRecipeId(recipeId);
    }

    @Override
    public Rating save(int userId, int recipeId, Double value, String comments) {
        return feedbackDao.addRating(userId, recipeId, value , comments);
    }

    // Other service methods
}
