package com.esd.recipe.util;

import com.esd.recipe.model.User;

public class LoginResponse {
    private final String token;
    private final User user;

    public LoginResponse(String token, User user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }
}
