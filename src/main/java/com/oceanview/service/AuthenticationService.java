package com.oceanview.service;

import com.oceanview.dao.UserDAO;
import com.oceanview.modal.User;
import com.oceanview.util.PasswordUtil;

import java.sql.SQLException;

/**
 * Service class for authentication operations
 */
public class AuthenticationService {
    private UserDAO userDAO;

    public AuthenticationService() {
        this.userDAO = new UserDAO();
    }

    /**
     * Authenticate user with username and password
     */
    public User login(String username, String password) {
        try {
            User user = userDAO.findByUsername(username);

            if (user != null && user.isActive()) {
                if (PasswordUtil.verifyPassword(password, user.getPassword())) {
                    userDAO.updateLastLogin(user.getUserId());
                    return user;
                }
            }

            return null;
        } catch (SQLException e) {
            System.err.println("Login error: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Logout user
     */
    public void logout(String userId) {
        // Log the logout action if needed
        System.out.println("User " + userId + " logged out");
    }

    /**
     * Change user password
     */
    public boolean changePassword(String userId, String oldPassword, String newPassword) {
        try {
            User user = userDAO.findById(userId);

            if (user != null && PasswordUtil.verifyPassword(oldPassword, user.getPassword())) {
                String hashedPassword = PasswordUtil.hashPassword(newPassword);
                user.setPassword(hashedPassword);
                return userDAO.update(user);
            }

            return false;
        } catch (SQLException e) {
            System.err.println("Password change error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Validate session token
     */
    public boolean validateSession(String userId) {
        try {
            User user = userDAO.findById(userId);
            return user != null && user.isActive();
        } catch (SQLException e) {
            System.err.println("Session validation error: " + e.getMessage());
            return false;
        }
    }
}
