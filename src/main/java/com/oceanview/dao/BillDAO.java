package com.oceanview.dao;

import com.oceanview.modal.Bill;
import java.sql.SQLException;
import java.util.List;

public class BillDAO extends BaseDAO<Bill> {

    private final RowMapper<Bill> mapper = rs -> {
        Bill b = new Bill();
        b.setBillId(rs.getString("bill_id"));
        b.setReservationNumber(rs.getString("reservation_number"));
        b.setIssueDate(rs.getTimestamp("issue_date").toLocalDateTime());
        b.setRoomCharges(rs.getDouble("room_charges"));
        b.setServiceCharges(rs.getDouble("service_charges"));
        b.setTaxAmount(rs.getDouble("tax_amount"));
        b.setDiscountAmount(rs.getDouble("discount_amount"));
        b.setTotalAmount(rs.getDouble("total_amount"));
        b.setFinalAmount(rs.getDouble("final_amount"));
        b.setPaymentStatus(rs.getString("payment_status"));
        b.setPaymentMethod(rs.getString("payment_method"));
        b.setNotes(rs.getString("notes"));
        return b;
    };

    @Override
    public Bill findById(String id) throws SQLException {
        String sql = "SELECT * FROM bills WHERE bill_id = ?";
        return executeQuerySingle(sql, mapper, id);
    }

    @Override
    public List<Bill> findAll() throws SQLException {
        String sql = "SELECT * FROM bills";
        return executeQuery(sql, mapper);
    }

    @Override
    public boolean insert(Bill b) throws SQLException {
        String sql = "INSERT INTO bills (bill_id, reservation_number, issue_date, room_charges, service_charges, tax_amount, discount_amount, total_amount, final_amount, payment_status, payment_method, notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return executeUpdate(sql, b.getBillId(), b.getReservationNumber(), b.getIssueDate(), b.getRoomCharges(),
                b.getServiceCharges(), b.getTaxAmount(), b.getDiscountAmount(), b.getTotalAmount(), b.getFinalAmount(),
                b.getPaymentStatus(), b.getPaymentMethod(), b.getNotes()) > 0;
    }

    @Override
    public boolean update(Bill b) throws SQLException {
        String sql = "UPDATE bills SET reservation_number = ?, issue_date = ?, room_charges = ?, service_charges = ?, tax_amount = ?, discount_amount = ?, total_amount = ?, final_amount = ?, payment_status = ?, payment_method = ?, notes = ? WHERE bill_id = ?";
        return executeUpdate(sql, b.getReservationNumber(), b.getIssueDate(), b.getRoomCharges(), b.getServiceCharges(),
                b.getTaxAmount(), b.getDiscountAmount(), b.getTotalAmount(), b.getFinalAmount(), b.getPaymentStatus(),
                b.getPaymentMethod(), b.getNotes(), b.getBillId()) > 0;
    }

    @Override
    public boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM bills WHERE bill_id = ?";
        return executeUpdate(sql, id) > 0;
    }

    public List<Bill> findByReservation(String reservationNumber) throws SQLException {
        String sql = "SELECT * FROM bills WHERE reservation_number = ?";
        return executeQuery(sql, mapper, reservationNumber);
    }
}
