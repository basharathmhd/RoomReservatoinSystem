package com.oceanview.servlet;

import com.oceanview.dao.UserDAO;
import com.oceanview.modal.User;
import com.oceanview.util.JSONUtil;
import com.oceanview.util.PasswordUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@WebServlet(name = "UserServlet", urlPatterns = { "/api/users/*" })
public class UserServlet extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String pathInfo = request.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {
                List<User> users = userDAO.findAll();
                // Don't expose password hashes
                for (User u : users) {
                    u.setPassword(null);
                }
                sendResponse(response, JSONUtil.createSuccessResponse("Users retrieved", users));
            } else {
                String id = pathInfo.substring(1);
                User user = userDAO.findById(id);
                if (user != null) {
                    user.setPassword(null);
                    sendResponse(response, JSONUtil.createSuccessResponse("User found", user));
                } else {
                    sendError(response, "User not found");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, "Error retrieving users: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String body = getRequestBody(request);

            String username = extractJsonValue(body, "username");
            String password = extractJsonValue(body, "password");
            String fullName = extractJsonValue(body, "fullName");
            String role = extractJsonValue(body, "role");

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
                sendResponse(response, JSONUtil.createSuccessResponse("User created", user));
            } else {
                sendError(response, "Failed to create user");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, "Error creating user: " + e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                sendError(response, "User ID required");
                return;
            }

            String id = pathInfo.substring(1);
            User existing = userDAO.findById(id);
            if (existing == null) {
                sendError(response, "User not found");
                return;
            }

            String body = getRequestBody(request);

            String username = extractJsonValue(body, "username");
            if (username != null)
                existing.setUsername(username);

            String fullName = extractJsonValue(body, "fullName");
            if (fullName != null)
                existing.setFullName(fullName);

            String role = extractJsonValue(body, "role");
            if (role != null)
                existing.setRole(role);

            String activeStr = extractJsonValue(body, "isActive");
            if (activeStr != null)
                existing.setActive(Boolean.parseBoolean(activeStr));

            // If password is being updated, hash it
            String password = extractJsonValue(body, "password");
            if (password != null && !password.isEmpty()) {
                existing.setPassword(PasswordUtil.hashPassword(password));
            }

            existing.setModifiedDate(LocalDateTime.now());
            existing.setModifiedBy("system");

            boolean success = userDAO.update(existing);
            if (success) {
                existing.setPassword(null);
                sendResponse(response, JSONUtil.createSuccessResponse("User updated", existing));
            } else {
                sendError(response, "Failed to update user");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, "Error updating user: " + e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                sendError(response, "User ID required");
                return;
            }

            String id = pathInfo.substring(1);
            boolean success = userDAO.delete(id);
            if (success) {
                sendResponse(response, JSONUtil.createSuccessResponse("User deleted", null));
            } else {
                sendError(response, "User not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, "Error deleting user: " + e.getMessage());
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

    private String extractJsonValue(String json, String key) {
        String searchKey = "\"" + key + "\"";
        int keyIndex = json.indexOf(searchKey);
        if (keyIndex == -1)
            return null;

        int colonIndex = json.indexOf(":", keyIndex);
        if (colonIndex == -1)
            return null;

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

    private void sendResponse(HttpServletResponse response, String json) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }

    private void sendError(HttpServletResponse response, String message) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        PrintWriter out = response.getWriter();
        out.print(JSONUtil.createErrorResponse(message));
        out.flush();
    }
}
