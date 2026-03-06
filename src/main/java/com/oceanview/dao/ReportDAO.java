package com.oceanview.dao;

import com.oceanview.modal.ReportSummary;
import com.oceanview.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportDAO {

    private DatabaseConnection dbConnection;

    public ReportDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    protected Connection getConnection() throws SQLException {
        return dbConnection.getConnection();
    }

    public ReportSummary getSummary() throws SQLException {
        ReportSummary summary = new ReportSummary();
        Connection conn = null;

        try {
            conn = getConnection();

            summary.setTotalReservations(getCount(conn, "SELECT COUNT(*) FROM reservations"));
            summary.setTotalRevenue(getSum(conn,
                    "SELECT SUM(final_amount) FROM bills WHERE payment_status='PAID' OR payment_status='COMPLETED'"));
            summary.setAvailableRooms(getCount(conn, "SELECT COUNT(*) FROM rooms WHERE status='AVAILABLE'"));
            summary.setBookedRooms(
                    getCount(conn, "SELECT COUNT(*) FROM rooms WHERE status='BOOKED' OR status='OCCUPIED'"));
            summary.setTotalGuests(getCount(conn, "SELECT COUNT(*) FROM guests"));

        } finally {
            if (conn != null) {
                conn.close();
            }
        }

        return summary;
    }

    private long getCount(Connection conn, String sql) {
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private double getSum(Connection conn, String sql) {
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}
