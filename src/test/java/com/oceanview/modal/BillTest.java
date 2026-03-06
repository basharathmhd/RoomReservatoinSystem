package com.oceanview.modal;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BillTest {

    @Test
    void testBillConstructors() {
        Bill bill1 = new Bill();
        assertNotNull(bill1.getIssueDate());
        assertEquals("PENDING", bill1.getPaymentStatus());

        Bill bill2 = new Bill("B001", "RES-001");
        assertEquals("B001", bill2.getBillId());
        assertEquals("RES-001", bill2.getReservationNumber());
        assertEquals("PENDING", bill2.getPaymentStatus());
    }

    @Test
    void testCalculations() {
        Bill bill = new Bill();
        bill.setRoomCharges(100.0);
        bill.setServiceCharges(50.0);
        bill.calculateTotalAmount();
        assertEquals(150.0, bill.getTotalAmount());

        bill.calculateTax(0.1); // 10% tax
        assertEquals(15.0, bill.getTaxAmount());

        bill.setDiscountAmount(5.0);
        bill.calculateFinalAmount();

        // Final Amount = Total + Tax - Discount = 150 + 15 - 5 = 160
        assertEquals(160.0, bill.getFinalAmount());
    }

    @Test
    void testIsPaid() {
        Bill bill = new Bill();
        assertFalse(bill.isPaid());

        bill.setPaymentStatus("PAID");
        assertTrue(bill.isPaid());

        bill.setPaymentStatus("PARTIAL");
        assertFalse(bill.isPaid());
    }

    @Test
    void testSettersAndGetters() {
        Bill bill = new Bill();
        bill.setPaymentMethod("CREDIT_CARD");
        bill.setNotes("Late checkout included");

        assertEquals("CREDIT_CARD", bill.getPaymentMethod());
        assertEquals("Late checkout included", bill.getNotes());
    }

    @Test
    void testToString() {
        Bill bill = new Bill("B002", "RES-002");
        bill.setFinalAmount(250.0);
        String str = bill.toString();

        assertTrue(str.contains("B002"));
        assertTrue(str.contains("RES-002"));
        assertTrue(str.contains("250.0"));
        assertTrue(str.contains("PENDING"));
    }
}
