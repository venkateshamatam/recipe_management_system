package com.esd.recipe.dao;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import com.esd.recipe.model.Recipe;
import com.esd.recipe.model.User;
import com.esd.recipe.util.PasswordUtils;
import jakarta.persistence.PersistenceException;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import static com.esd.recipe.model.User.Role.ADMIN;
import static com.esd.recipe.model.User.Role.USER;


@Repository
public class UserDaoImpl extends DAO {

	public List<User> get() {
		List<User> users = getSession().createQuery("from User", User.class).list();
		for (User user : users) {
			getSession().refresh(user);
		}
		return users;
	}

	public void addFavorite(int userId, int recipeId) {
		Session session = getSession();
		begin();
		User user = session.get(User.class, userId);
		Recipe recipe = session.get(Recipe.class, recipeId);

		if (user == null || recipe == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User or Recipe not found");
		}

		user.getFavoriteRecipes().add(recipe);
		session.merge(user);
		commit();
		close();
	}

	public void removeFavorite(int userId, int recipeId) {
		Session session = getSession();
		begin();
		User user = session.get(User.class, userId);
		Recipe recipe = session.get(Recipe.class, recipeId);

		if (user == null || recipe == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User or Recipe not found");
		}

		user.getFavoriteRecipes().remove(recipe);
		session.merge(user);
		commit();
		close();
	}

	public User get(int id) {
		User userObj = getSession().get(User.class, id);
		if (userObj != null) {
			getSession().refresh(userObj);
		}
		return userObj;
	}

	public void save(User user) throws ResponseStatusException, NoSuchAlgorithmException {
		// Hash the password
		String hashedPassword = PasswordUtils.hashPassword(user.getPassword());
		user.setPassword(hashedPassword);
		user.setRole(USER);

		try {
			begin();
			getSession().merge(user);
			commit();
			close();
		} catch (Exception e) {
			rollback();
			if (e.getCause() instanceof ConstraintViolationException) {
				// Handle duplicate username error
				throw new ResponseStatusException(HttpStatus.CONFLICT, "Username or email already exists");
			} else {
				// Handle other persistence errors
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not create account");
			}
		}
	}

	public void promoteUserToAdmin(int userId, User loggedInUser) throws ResponseStatusException {
		if (!loggedInUser.getRole().equals(ADMIN)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admins can promote users to admin");
		}

		User userToPromote = getSession().get(User.class, userId);

		// Check if the user to promote exists
		if (userToPromote == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
		}

		// Set the role as "ADMIN" for the user to promote
		userToPromote.setRole(ADMIN);

		try {
			begin();
			getSession().merge(userToPromote);
			commit();
		} catch (PersistenceException e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Could not promote user to admin");
		}
	}

	public void update(String username, String email, int id) throws ResponseStatusException, NoSuchAlgorithmException {
		User userToEdit = getSession().get(User.class, id);

		if (userToEdit == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
		}

		userToEdit.setEmail(email);
		userToEdit.setUsername(username);

		try {
			begin();
			getSession().merge(userToEdit);
			commit();
		} catch (PersistenceException e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Could not update");
		}
	}


	public void delete(int id) {
		try {
			begin();
			getSession().remove(getSession().get(User.class, id));
			commit();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Could not delete user");
		}
	}

	public User findByUsernameAndPassword(String username, String password) {
		try {
			Session session = DAO.getSession();
			Query<User> query = session.createQuery("from User where username=:username and password=:password", User.class);
			query.setParameter("username", username);
			query.setParameter("password", password);
			User user = null;
			try {
				user = query.getSingleResult();
			} catch (Exception e) {
				// user not found
			}
			return user;
		} catch (Exception e) {
			throw e;
		} finally {
			DAO.close();
		}
	}

	public User findByUsername(String username) {
		try {
			Session session = DAO.getSession();
			Query<User> query = session.createQuery("from User where username=:username", User.class);
			query.setParameter("username", username);
			User user = null;
			try {
				user = query.getSingleResult();
			} catch (Exception e) {
				// user not found
			}
			return user;
		} catch (Exception e) {
			throw e;
		} finally {
			DAO.close();
		}
	}

	public User findById(int id) {
		return (User) getSession().get(User.class, id);
	}
}