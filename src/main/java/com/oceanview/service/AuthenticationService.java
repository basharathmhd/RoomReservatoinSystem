package com.oceanview.service;

import com.oceanview.dao.UserDAO;
import com.oceanview.modal.User;
import com.oceanview.util.PasswordUtil;

import java.sql.SQLException;


public class AuthenticationService {
    private UserDAO userDAO;

    public AuthenticationService() {
        this.userDAO = new UserDAO();
    }

   
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

  
    public void logout(String userId) {
        
        System.out.println("User " + userId + " logged out");
    }

    
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
