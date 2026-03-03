package com.oceanview.dao;

import com.oceanview.modal.User;

import java.sql.*;
import java.util.List;


public class UserDAO extends BaseDAO<User> {

    private static final String TABLE_NAME = "users";

    private static final String INSERT_SQL =
            "INSERT INTO " + TABLE_NAME + " (user_id, username, password, full_name, role, " +
                    "is_active, created_date, created_by) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_SQL =
            "UPDATE " + TABLE_NAME + " SET username = ?, password = ?, full_name = ?, " +
                    "role = ?, is_active = ?, modified_date = ?, modified_by = ? WHERE user_id = ?";

    private static final String DELETE_SQL =
            "DELETE FROM " + TABLE_NAME + " WHERE user_id = ?";

    private static final String SELECT_BY_ID_SQL =
            "SELECT * FROM " + TABLE_NAME + " WHERE user_id = ?";

    private static final String SELECT_ALL_SQL =
            "SELECT * FROM " + TABLE_NAME;

    private static final String SELECT_BY_USERNAME_SQL =
            "SELECT * FROM " + TABLE_NAME + " WHERE username = ?";

    private static final String UPDATE_LAST_LOGIN_SQL =
            "UPDATE " + TABLE_NAME + " SET last_login = ? WHERE user_id = ?";


    private final RowMapper<User> userMapper = rs -> {
        User user = new User();
        user.setUserId(rs.getString("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setFullName(rs.getString("full_name"));
        user.setRole(rs.getString("role"));
        user.setActive(rs.getBoolean("is_active"));

        Timestamp lastLogin = rs.getTimestamp("last_login");
        if (lastLogin != null) {
            user.setLastLogin(lastLogin.toLocalDateTime());
        }

        Timestamp createdDate = rs.getTimestamp("created_date");
        if (createdDate != null) {
            user.setCreatedDate(createdDate.toLocalDateTime());
        }

        user.setCreatedBy(rs.getString("created_by"));

        Timestamp modifiedDate = rs.getTimestamp("modified_date");
        if (modifiedDate != null) {
            user.setModifiedDate(modifiedDate.toLocalDateTime());
        }

        user.setModifiedBy(rs.getString("modified_by"));

        return user;
    };

    @Override
    public User findById(String id) throws SQLException {
        return executeQuerySingle(SELECT_BY_ID_SQL, userMapper, id);
    }

    @Override
    public List<User> findAll() throws SQLException {
        return executeQuery(SELECT_ALL_SQL, userMapper);
    }

    @Override
    public boolean insert(User user) throws SQLException {
        int result = executeUpdate(INSERT_SQL,
                user.getUserId(),
                user.getUsername(),
                user.getPassword(),
                user.getFullName(),
                user.getRole(),
                user.isActive(),
                Timestamp.valueOf(user.getCreatedDate()),
                user.getCreatedBy()
        );
        return result > 0;
    }

    @Override
    public boolean update(User user) throws SQLException {
        int result = executeUpdate(UPDATE_SQL,
                user.getUsername(),
                user.getPassword(),
                user.getFullName(),
                user.getRole(),
                user.isActive(),
                user.getModifiedDate() != null ?
                        Timestamp.valueOf(user.getModifiedDate()) : null,
                user.getModifiedBy(),
                user.getUserId()
        );
        return result > 0;
    }

    @Override
    public boolean delete(String id) throws SQLException {
        int result = executeUpdate(DELETE_SQL, id);
        return result > 0;
    }


    public User findByUsername(String username) throws SQLException {
        return executeQuerySingle(SELECT_BY_USERNAME_SQL, userMapper, username);
    }


    public boolean updateLastLogin(String userId) throws SQLException {
        int result = executeUpdate(UPDATE_LAST_LOGIN_SQL,
                Timestamp.valueOf(java.time.LocalDateTime.now()),
                userId
        );
        return result > 0;
    }


    public boolean usernameExists(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE username = ?";
        return count(sql, username) > 0;
    }


    public List<User> findActiveUsers() throws SQLException {
        String sql = SELECT_ALL_SQL + " WHERE is_active = true";
        return executeQuery(sql, userMapper);
    }
}
