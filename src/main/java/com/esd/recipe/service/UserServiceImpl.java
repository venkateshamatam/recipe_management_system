package com.esd.recipe.service;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import com.esd.recipe.dao.UserDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esd.recipe.model.User;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserServiceImpl implements UserService {


	private UserDaoImpl userDaoImpl;
	@Autowired
	public UserServiceImpl(UserDaoImpl userDaoImpl){
		this.userDaoImpl = userDaoImpl;
	}
	@Transactional
	@Override
	public List<User> get() {
		return userDaoImpl.get();
	}

	@Transactional
	@Override
	public User get(int id) {
		return userDaoImpl.get(id);
	}

	@Override
	public void save(User user) throws ResponseStatusException, NoSuchAlgorithmException {
		userDaoImpl.save(user);
	}

	@Override
	public void update(String username, String email, int id) throws NoSuchAlgorithmException {
		userDaoImpl.update(username,email,id);
	}


	@Override
	public void delete(int id) {
		userDaoImpl.delete(id);
	}

	@Override
	public User findByUsernameAndPassword(String username, String password) {
		return userDaoImpl.findByUsernameAndPassword(username, password);
	}
	@Override
	public boolean isAdmin(int userId) {
		User user = userDaoImpl.findById(userId);
		if (user != null) {
			return user.getRole().equals("ADMIN"); // Assuming there's a method isAdmin() in the User model
		}
		return false;
	}

	@Override
	public void promoteUserToAdmin(int userId, User loggedInUser) throws ResponseStatusException {
		userDaoImpl.promoteUserToAdmin(userId, loggedInUser);
	}

	@Override
	public User findByUsername(String username) {
		return userDaoImpl.findByUsername(username);
	}
	@Override
	public void addFavorite(int userId, int recipeId) {
		userDaoImpl.addFavorite(userId, recipeId);
	}

	@Override
	public void removeFavorite(int userId, int recipeId) {
		userDaoImpl.removeFavorite(userId, recipeId);
	}

//	@Transactional
//	@Override
//	public void save(User user) {
//		userDAO.save(user);
//	}
//
//	@Transactional
//	@Override
//	public void delete(int id) throws IllegalArgumentException{
//		userDao.delete(id);
//	}
//
//	@Transactional
//	@Override
//	public User findByUsernameAndPassword(String username, String password) {
//	    return userDao.findByUsernameAndPassword(username, password);
//	}


}
