package com.oceanview.servlet;

import com.oceanview.dao.UserDAO;
import com.oceanview.modal.User;
import com.oceanview.service.AuthenticationService;
import com.oceanview.util.JSONUtil;
import com.oceanview.util.PasswordUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

/**
 * Servlet handling authentication operations
 * Endpoints: POST /api/auth/login, POST /api/auth/logout, POST
 * /api/auth/register, GET /api/auth/validate
 */
@WebServlet(name = "AuthServlet", urlPatterns = { "/api/auth/*" })
public class AuthServlet extends HttpServlet {

    private AuthenticationService authService;
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        authService = new AuthenticationService();
        userDAO = new UserDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            sendError(response, "Invalid endpoint");
            return;
        }

        switch (pathInfo) {
            case "/login":
                handleLogin(request, response);
                break;
            case "/register":
                handleRegister(request, response);
                break;
            case "/logout":
                handleLogout(request, response);
                break;
            case "/change-password":
                handleChangePassword(request, response);
                break;
            default:
                sendError(response, "Endpoint not found");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();

        if ("/validate".equals(pathInfo)) {
            handleValidate(request, response);
        } else {
            sendError(response, "Endpoint not found");
        }
    }

    /**
     * Handle login request
     */
    private void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            // Parse JSON request body
            String requestBody = getRequestBody(request);

            // Manual JSON parsing (simple approach)
            String username = extractJsonValue(requestBody, "username");
            String password = extractJsonValue(requestBody, "password");

            if (username == null || password == null) {
                sendError(response, "Username and password required");
                return;
            }

            // Authenticate user
            User user = authService.login(username, password);

            if (user != null) {
                // Create session
                HttpSession session = request.getSession(true);
                session.setAttribute("userId", user.getUserId());
                session.setAttribute("username", user.getUsername());
                session.setAttribute("role", user.getRole());
                session.setMaxInactiveInterval(30 * 60); // 30 minutes

                // Return success response
                String responseJson = String.format(
                        "{\"success\":true,\"message\":\"Login successful\"," +
                                "\"data\":{\"userId\":\"%s\",\"username\":\"%s\"," +
                                "\"fullName\":\"%s\",\"role\":\"%s\",\"sessionId\":\"%s\"}}",
                        user.getUserId(),
                        user.getUsername(),
                        user.getFullName(),
                        user.getRole(),
                        session.getId());

                sendResponse(response, responseJson);
            } else {
                sendError(response, "Invalid username or password");
            }

        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, "Login failed: " + e.getMessage());
        }
    }

    /**
     * Handle logout request
     */
    private void handleLogout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            HttpSession session = request.getSession(false);
            if (session != null) {
                String userId = (String) session.getAttribute("userId");
                authService.logout(userId);
                session.invalidate();
            }

            sendResponse(response, JSONUtil.createSuccessResponse("Logout successful", null));
        } catch (Exception e) {
            sendError(response, "Logout failed: " + e.getMessage());
        }
    }

    /**
     * Handle validate session request
     */
    private void handleValidate(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("userId") != null) {
            String responseJson = String.format(
                    "{\"success\":true,\"valid\":true," +
                            "\"data\":{\"userId\":\"%s\",\"username\":\"%s\",\"role\":\"%s\"}}",
                    session.getAttribute("userId"),
                    session.getAttribute("username"),
                    session.getAttribute("role"));
            sendResponse(response, responseJson);
        } else {
            String responseJson = "{\"success\":false,\"valid\":false," +
                    "\"message\":\"No valid session\"}";
            sendResponse(response, responseJson);
        }
    }

    /**
     * Handle change password request
     */
    private void handleChangePassword(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("userId") == null) {
                sendError(response, "Unauthorized");
                return;
            }

            String requestBody = getRequestBody(request);
            String oldPassword = extractJsonValue(requestBody, "oldPassword");
            String newPassword = extractJsonValue(requestBody, "newPassword");
            String userId = (String) session.getAttribute("userId");

            if (oldPassword == null || newPassword == null) {
                sendError(response, "Old and new passwords required");
                return;
            }

            boolean success = authService.changePassword(userId, oldPassword, newPassword);

            if (success) {
                sendResponse(response, JSONUtil.createSuccessResponse(
                        "Password changed successfully", null));
            } else {
                sendError(response, "Password change failed");
            }

        } catch (Exception e) {
            sendError(response, "Error changing password: " + e.getMessage());
        }
    }

    /**
     * Handle user registration with hashed passwords
     */
    private void handleRegister(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            String requestBody = getRequestBody(request);

            String username = extractJsonValue(requestBody, "username");
            String password = extractJsonValue(requestBody, "password");
            String fullName = extractJsonValue(requestBody, "fullName");
            String role = extractJsonValue(requestBody, "role");

            if (username == null || password == null || fullName == null) {
                sendError(response, "username, password, and fullName are required");
                return;
            }

            // Check if username already exists
            if (userDAO.usernameExists(username)) {
                sendError(response, "Username already exists");
                return;
            }

            // Hash the password before storing
            String hashedPassword = PasswordUtil.hashPassword(password);

            User user = new User();
            user.setUserId("USR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            user.setUsername(username);
            user.setPassword(hashedPassword);
            user.setFullName(fullName);
            user.setRole(role != null ? role : "STAFF");
            user.setActive(true);
            user.setCreatedBy("system");

            boolean success = userDAO.insert(user);
            if (success) {
                user.setPassword(null); // Don't return hash
                sendResponse(response, JSONUtil.createSuccessResponse("Registration successful", user));
            } else {
                sendError(response, "Registration failed");
            }

        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, "Registration failed: " + e.getMessage());
        }
    }

    private String getRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;

        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        return sb.toString();
    }

    /**
     * Simple JSON value extractor
     */
    private String extractJsonValue(String json, String key) {
        String searchKey = "\"" + key + "\"";
        int keyIndex = json.indexOf(searchKey);

        if (keyIndex == -1) {
            return null;
        }

        int colonIndex = json.indexOf(":", keyIndex);
        if (colonIndex == -1) {
            return null;
        }

        int valueStart = colonIndex + 1;
        while (valueStart < json.length() &&
                (json.charAt(valueStart) == ' ' || json.charAt(valueStart) == '"')) {
            valueStart++;
        }

        int valueEnd = valueStart;
        while (valueEnd < json.length() &&
                json.charAt(valueEnd) != '"' &&
                json.charAt(valueEnd) != ',' &&
                json.charAt(valueEnd) != '}') {
            valueEnd++;
        }

        return json.substring(valueStart, valueEnd).trim();
    }

    /**
     * Send JSON response
     */
    private void sendResponse(HttpServletResponse response, String json) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }

    /**
     * Send error response
     */
    private void sendError(HttpServletResponse response, String message) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        String json = JSONUtil.createErrorResponse(message);
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }
}
