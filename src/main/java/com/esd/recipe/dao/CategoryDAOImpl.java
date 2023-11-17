package com.esd.recipe.dao;

import com.esd.recipe.model.Category;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoryDAOImpl extends DAO implements CategoryDAO {

    @Override
    public Category save(Category category) {
        Session session = getSession();
        session.beginTransaction();
        session.saveOrUpdate(category);
        session.getTransaction().commit();
        session.close();
        return category;
    }

    @Override
    public List<Category> findAll() {
            List<Category> allCategories = getSession().createQuery("from Category", Category.class).list();
            for(Category cat: allCategories) {
                getSession().refresh(cat);
            }
            return allCategories;
    }
}
