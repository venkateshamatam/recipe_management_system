package com.esd.recipe.dao;

import com.esd.recipe.model.*;
import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Repository
public class RecipeDAOImpl extends DAO implements RecipeDAO {
    @Override
    public void delete(int id) {
        try {
            begin();
            getSession().remove(getSession().get(Recipe.class, id));
            commit();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Could not delete Recipe");
        }
    }

    @Override
    public void save(int userId, Recipe recipe) {
        Session session = getSession();
        begin();
        User user = session.get(User.class, userId);

        if (user == null || recipe == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User or Recipe not found");
        }
        recipe.setCreatedBy(user.getId());
        getSession().merge(recipe);
        commit();
        close();
    }


    @Override
    public Recipe get(int id) {
        Recipe getUser=getSession().get(Recipe.class,id);
        return getUser;
    }

    @Override
    public List<Recipe> get() {
        List<Recipe> allUsers = getSession().createQuery("from Recipe", Recipe.class).list();
        for(Recipe recipe: allUsers) {
            getSession().refresh(recipe);
        }
        return allUsers;
    }

    @Override
    public List<Recipe> getByUser(int userId) {
        String hql = "FROM Recipe WHERE createdBy = :userId";
        TypedQuery<Recipe> query = getSession().createQuery(hql, Recipe.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    @Override
    public List<Recipe> search(String query) {
        Session session = getSession();
        Query<Recipe> q = session.createQuery("FROM Recipe r WHERE LOWER(r.title) LIKE LOWER(:query)", Recipe.class);
        q.setParameter("query", "%" + query + "%");
        List<Recipe> resultList = q.getResultList();
        session.close();
        return resultList;
    }

    @Override
    public List<Recipe> searchByIngredient(String query) {
        Session session = getSession();
        Query<Recipe> q = session.createQuery("SELECT DISTINCT r FROM Recipe r JOIN r.ingredients i WHERE LOWER(i.name) LIKE LOWER(:query)", Recipe.class);
        q.setParameter("query", "%" + query + "%");
        List<Recipe> resultList = q.getResultList();
        session.close();
        return resultList;
    }

    @Override
    public Recipe addCategory(int recipeId, int categoryId) {
        Session session = getSession();
        begin();
        Recipe recipe = session.get(Recipe.class, recipeId);
        Category category = session.get(Category.class, categoryId);

        if (recipe == null || category == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe or Category not found");
        }

        recipe.getCategories().add(category);
        session.merge(recipe);
        commit();
        close();
        return recipe;
    }

    @Override
    public Recipe update(Recipe recipe) {
        Session session = getSession();
        begin();
        session.merge(recipe);
        commit();
        close();
        return recipe;
    }



}
