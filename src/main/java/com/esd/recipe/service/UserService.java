package com.esd.recipe.service;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import com.esd.recipe.model.User;
import org.springframework.web.server.ResponseStatusException;

public interface UserService {
	List<User> get();
	
	User get(int id);
	
	void save(User user) throws ResponseStatusException, NoSuchAlgorithmException;


	void delete(int id);

	User findByUsernameAndPassword(String username, String password);

    void promoteUserToAdmin(int userId, User loggedInUser) throws ResponseStatusException;
	void update(String username, String email, int id) throws NoSuchAlgorithmException;
	User findByUsername(String username);

	void addFavorite(int userId, int recipeId);
	void removeFavorite(int userId, int recipeId);
	boolean isAdmin(int userId);
}
