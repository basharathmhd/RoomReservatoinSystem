package com.oceanview.modal;

import java.io.Serializable;
import java.time.LocalDateTime;


public class Payment implements Serializable {
    private static final long serialVersionUID = 1L;

    private String paymentId;
    private String billId;
    private LocalDateTime paymentDate;
    private double amount;
    private String paymentMethod; // CASH, CREDIT_CARD, DEBIT_CARD, ONLINE
    private String transactionId;
    private String status; // SUCCESS, FAILED, PENDING
    private String remarks;

    // Constructors
    public Payment() {
        this.paymentDate = LocalDateTime.now();
        this.status = "PENDING";
    }

    public Payment(String paymentId, String billId, double amount, String paymentMethod) {
        this();
        this.paymentId = paymentId;
        this.billId = billId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }

    // Getters and Setters
    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId='" + paymentId + '\'' +
                ", billId='" + billId + '\'' +
                ", amount=" + amount +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
