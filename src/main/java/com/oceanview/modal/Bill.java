package com.oceanview.modal;

import java.io.Serializable;
import java.time.LocalDateTime;


public class Bill implements Serializable {
    private static final long serialVersionUID = 1L;

    private String billId;
    private String reservationNumber;
    private LocalDateTime issueDate;
    private double roomCharges;
    private double serviceCharges;
    private double taxAmount;
    private double discountAmount;
    private double totalAmount;
    private double finalAmount;
    private String paymentStatus; // PENDING, PAID, PARTIAL
    private String paymentMethod;
    private String notes;

    // Additional field for joins
    private Reservation reservation;

    // Constructors
    public Bill() {
        this.issueDate = LocalDateTime.now();
        this.paymentStatus = "PENDING";
    }

    public Bill(String billId, String reservationNumber) {
        this();
        this.billId = billId;
        this.reservationNumber = reservationNumber;
    }

    // Business methods
    public void calculateTotalAmount() {
        this.totalAmount = this.roomCharges + this.serviceCharges;
    }

    public void calculateTax(double taxRate) {
        this.taxAmount = this.totalAmount * taxRate;
    }

    public void calculateFinalAmount() {
        this.finalAmount = this.totalAmount + this.taxAmount - this.discountAmount;
    }

    public boolean isPaid() {
        return "PAID".equals(paymentStatus);
    }

    // Getters and Setters
    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getReservationNumber() {
        return reservationNumber;
    }

    public void setReservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public LocalDateTime getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDateTime issueDate) {
        this.issueDate = issueDate;
    }

    public double getRoomCharges() {
        return roomCharges;
    }

    public void setRoomCharges(double roomCharges) {
        this.roomCharges = roomCharges;
    }

    public double getServiceCharges() {
        return serviceCharges;
    }

    public void setServiceCharges(double serviceCharges) {
        this.serviceCharges = serviceCharges;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(double finalAmount) {
        this.finalAmount = finalAmount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "billId='" + billId + '\'' +
                ", reservationNumber='" + reservationNumber + '\'' +
                ", finalAmount=" + finalAmount +
                ", paymentStatus='" + paymentStatus + '\'' +
                '}';
    }
}
