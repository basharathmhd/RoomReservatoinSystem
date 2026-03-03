package com.oceanview.dao;

import com.oceanview.modal.Payment;
import java.sql.SQLException;
import java.util.List;

public class PaymentDAO extends BaseDAO<Payment> {

    private final RowMapper<Payment> mapper = rs -> {
        Payment p = new Payment();
        p.setPaymentId(rs.getString("payment_id"));
        p.setBillId(rs.getString("bill_id"));
        p.setPaymentDate(rs.getTimestamp("payment_date").toLocalDateTime());
        p.setAmount(rs.getDouble("amount"));
        p.setPaymentMethod(rs.getString("payment_method"));
        p.setTransactionId(rs.getString("transaction_id"));
        p.setStatus(rs.getString("status"));
        p.setRemarks(rs.getString("remarks"));
        return p;
    };

    @Override
    public Payment findById(String id) throws SQLException {
        String sql = "SELECT * FROM payments WHERE payment_id = ?";
        return executeQuerySingle(sql, mapper, id);
    }

    @Override
    public List<Payment> findAll() throws SQLException {
        String sql = "SELECT * FROM payments";
        return executeQuery(sql, mapper);
    }

    @Override
    public boolean insert(Payment p) throws SQLException {
        String sql = "INSERT INTO payments (payment_id, bill_id, payment_date, amount, payment_method, transaction_id, status, remarks) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        return executeUpdate(sql, p.getPaymentId(), p.getBillId(), p.getPaymentDate(), p.getAmount(),
                p.getPaymentMethod(), p.getTransactionId(), p.getStatus(), p.getRemarks()) > 0;
    }

    @Override
    public boolean update(Payment p) throws SQLException {
        String sql = "UPDATE payments SET bill_id = ?, payment_date = ?, amount = ?, payment_method = ?, transaction_id = ?, status = ?, remarks = ? WHERE payment_id = ?";
        return executeUpdate(sql, p.getBillId(), p.getPaymentDate(), p.getAmount(), p.getPaymentMethod(),
                p.getTransactionId(), p.getStatus(), p.getRemarks(), p.getPaymentId()) > 0;
    }

    @Override
    public boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM payments WHERE payment_id = ?";
        return executeUpdate(sql, id) > 0;
    }

    public List<Payment> findByBill(String billId) throws SQLException {
        String sql = "SELECT * FROM payments WHERE bill_id = ?";
        return executeQuery(sql, mapper, billId);
    }
}
