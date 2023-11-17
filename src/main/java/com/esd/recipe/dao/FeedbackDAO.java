package com.esd.recipe.dao;

import com.esd.recipe.model.Rating;
import com.esd.recipe.model.Recipe;
import com.esd.recipe.model.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FeedbackDAO extends DAO {

    public Rating addRating(int userId, int recipeId, Double value, String comments) {
        Session session = getSession();
        begin();
        User user = session.get(User.class, userId);
        Recipe recipe = session.get(Recipe.class, recipeId);
        Rating rating = new Rating();
        rating.setUser(user);
        rating.setRecipe(recipe);
        rating.setValue(value);
        rating.setComments(comments);

        session.merge(rating);
        commit();
        close();
        return rating;
    }


    public List<Rating> get() {
        List<Rating> ratings = getSession().createQuery("from Rating", Rating.class).list();
        for(Rating r: ratings){
            getSession().refresh(ratings);
        }
        return ratings;
    }

    public Rating get(int id) {
        Rating ratingObj = getSession().get(Rating.class, id);
        if(ratingObj !=null){
            getSession().refresh(ratingObj);
        }
        return ratingObj;
    }

    public Float getAverageRatingByRecipeId(int recipeId) {
        Session session = getSession();
        begin();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Double> query = cb.createQuery(Double.class);
        Root<Rating> root = query.from(Rating.class);

        query.select(cb.avg(root.get("value").as(Double.class))); // Change the type to Double
        query.where(cb.equal(root.get("recipe").get("id"), recipeId));

        Double averageRating = session.createQuery(query).getSingleResult();

        commit();
        close();
        return averageRating != null ? averageRating.floatValue() : null;
    }

}
