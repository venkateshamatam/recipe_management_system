package com.esd.recipe.controller;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;

import com.esd.recipe.util.LoginResponse;
import com.esd.recipe.util.PasswordUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.esd.recipe.service.UserService;
import com.esd.recipe.model.User;
import org.springframework.web.server.ResponseStatusException;

import static com.esd.recipe.dao.DAO.getSession;

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
public class UserController {

	@Autowired
	private UserService userService;

//	@PostMapping("/user")
//	public User save(@RequestBody User userObj) {
//		userService.save(userObj);
//		return userObj;
//	}

	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody User userObj) {
		try {
			userService.save(userObj);
			return ResponseEntity.ok(userObj);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		}
	}
	private HashMap<String, HttpSession> sessionMap = new HashMap<>();

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody User userObj, HttpServletRequest request, HttpServletResponse response) {
		String username = userObj.getUsername();
		String password = userObj.getPassword();

		try {
			// Hash the password
			String hashedPassword = PasswordUtils.hashPassword(password);
			User user = userService.findByUsernameAndPassword(username, hashedPassword);

			if (user == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username and password combination");
			}

			// Store the logged-in user's information in the session
			HttpSession session = request.getSession(true);

			session.setAttribute("loggedInUser", user);
			sessionMap.put(session.getId(), session);
			getSession().close();

			// Return response with session ID
			String sessionId = session.getId();
			response.setHeader("Set-Cookie", "JSESSIONID=" + sessionId + "; Path=/");

			// Return the session ID in the response body
			return ResponseEntity.ok().body(sessionId);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(@RequestHeader("Session-ID") String sessionId, HttpServletResponse response) {
		// Retrieve the HttpSession object for the specified session ID from the sessionMap
		HttpSession session = sessionMap.get(sessionId);

		// If the session is found, invalidate it and remove it from the sessionMap
		if (session != null) {
			session.invalidate();
			sessionMap.remove(sessionId);

			// Remove session ID cookie from client-side
			Cookie sessionCookie = new Cookie("JSESSIONID", "");
			sessionCookie.setPath("/");
			sessionCookie.setMaxAge(0);
			response.addCookie(sessionCookie);

			// Return a success response
			return ResponseEntity.ok().body("Logged out successfully");
		} else {
			// If the session is not found, return a failure response
			return ResponseEntity.badRequest().body("Session not found");
		}
	}

	@GetMapping("/current-user")
	public ResponseEntity<User> getCurrentUser(HttpServletRequest request) {
		User user = getCurrentUserFromSession(request);
		if (user == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		return ResponseEntity.ok(user);
	}

	private User getCurrentUserFromSession(HttpServletRequest request) {
		String sessionId = request.getHeader("Session-ID");
		HttpSession session = sessionMap.get(sessionId);
		if (session == null) {
			return null;
		}
		return (User) session.getAttribute("loggedInUser");
	}


	@GetMapping("/user")
	public List<User> get() {
		return userService.get();
	}

	@GetMapping("/user/{id}")
	public User get(@PathVariable int id) {
		User userObj = userService.get(id);
		if (userObj == null) {
			throw new RuntimeException("User not found");
		}
		return userObj;
	}

	@PutMapping("/user/{id}")
	public ResponseEntity<?> updateUser(@PathVariable int id, @RequestBody User user, HttpServletRequest request) throws NoSuchAlgorithmException {
		String sessionId = request.getHeader("Session-ID");
		HttpSession s = sessionMap.get(sessionId);
		if (s == null) {
			return null;
		}
		User linuser = (User) s.getAttribute("loggedInUser");

		if (linuser.getRole() == User.Role.ADMIN || linuser.id == id) {
			User existingUser = userService.get(id);
			if (existingUser == null) {
				return ResponseEntity.notFound().build();
			}
			userService.update(user.getUsername(),user.getEmail(), id);
			return ResponseEntity.ok(existingUser);
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not authorized to update the user");
		}
	}

	@DeleteMapping("/user/{id}")
	public ResponseEntity<String> delete(@PathVariable int id, HttpSession session, HttpServletRequest request	) {
//		User linuser = (User) session.getAttribute("loggedInUser");
		String sessionId = request.getHeader("Session-ID");
		HttpSession s = sessionMap.get(sessionId);
		if (s == null) {
			return null;
		}
		User linuser =  (User) s.getAttribute("loggedInUser");

		// Check if the logged-in user is an admin
		if (!linuser.getRole().equals(User.Role.ADMIN)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admins can remove users");
		}
		try {
			userService.delete(id);
			return ResponseEntity.ok("User with id " + id + " deleted successfully");
		} catch (ResponseStatusException ex) {
			return ResponseEntity.status(ex.getStatusCode().value()).body(ex.getMessage());
		}
	}

	@PostMapping("/user/{userId}/promote")
	public ResponseEntity<String> promoteUserToAdmin(@PathVariable int userId, HttpServletRequest request) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String sessionId = request.getHeader("Session-ID");
		HttpSession s = sessionMap.get(sessionId);
		if (s == null) {
			return null;
		}
		User linuser =  (User) s.getAttribute("loggedInUser");

		try {
			userService.promoteUserToAdmin(userId, linuser);
			return ResponseEntity.ok("User with id " + userId + " promoted to admin successfully");
		} catch (ResponseStatusException ex) {
			return ResponseEntity.status(ex.getStatusCode().value()).body(ex.getMessage());
		}
	}

 }
