package com.oceanview.dao;

import com.oceanview.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public abstract class BaseDAO<T> {

    protected DatabaseConnection dbConnection;

    public BaseDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    // db pool connection
    protected Connection getConnection() throws SQLException {
        return dbConnection.getConnection();
    }


    protected void closeResources(Connection conn, Statement stmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("Error closing ResultSet: " + e.getMessage());
            }
        }

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing Statement: " + e.getMessage());
            }
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing Connection: " + e.getMessage());
            }
        }
    }


    protected int executeUpdate(String sql, Object... params) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            // Set parameters
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }

            return pstmt.executeUpdate();
        } finally {
            closeResources(conn, pstmt, null);
        }
    }

    /**
     * Execute query and return single result
     */
    protected T executeQuerySingle(String sql, RowMapper<T> mapper, Object... params)
            throws SQLException {
        List<T> results = executeQuery(sql, mapper, params);
        return results.isEmpty() ? null : results.get(0);
    }

    /**
     * Execute query and return list of results
     */
    protected List<T> executeQuery(String sql, RowMapper<T> mapper, Object... params)
            throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<T> results = new ArrayList<>();

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            // Set parameters
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }

            rs = pstmt.executeQuery();

            while (rs.next()) {
                results.add(mapper.mapRow(rs));
            }

            return results;
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    /**
     * Check if record exists
     */
    protected boolean exists(String sql, Object... params) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }

            rs = pstmt.executeQuery();
            return rs.next();
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }


    protected int count(String sql, Object... params) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }

            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    /**
     * Abstract methods to be implemented by subclasses
     */
    public abstract T findById(String id) throws SQLException;
    public abstract List<T> findAll() throws SQLException;
    public abstract boolean insert(T entity) throws SQLException;
    public abstract boolean update(T entity) throws SQLException;
    public abstract boolean delete(String id) throws SQLException;

    /**
     * Functional interface for row mapping
     */
    @FunctionalInterface
    protected interface RowMapper<T> {
        T mapRow(ResultSet rs) throws SQLException;
    }
}
