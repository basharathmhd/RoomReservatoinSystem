package com.oceanview.modal;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

    @Test
    void testPaymentConstructors() {
        Payment pay1 = new Payment();
        assertNotNull(pay1.getPaymentDate());
        assertEquals("PENDING", pay1.getStatus());

        Payment pay2 = new Payment("P001", "B001", 150.0, "CREDIT_CARD");
        assertEquals("P001", pay2.getPaymentId());
        assertEquals("B001", pay2.getBillId());
        assertEquals(150.0, pay2.getAmount());
        assertEquals("CREDIT_CARD", pay2.getPaymentMethod());
        assertEquals("PENDING", pay2.getStatus());
        assertNotNull(pay2.getPaymentDate());
    }

    @Test
    void testSettersAndGetters() {
        Payment pay = new Payment();
        pay.setPaymentId("P002");
        pay.setBillId("B002");
        pay.setAmount(200.50);
        pay.setPaymentMethod("CASH");
        pay.setStatus("SUCCESS");
        pay.setTransactionId("TXN123456");
        pay.setRemarks("Paid in full");

        assertEquals("P002", pay.getPaymentId());
        assertEquals("B002", pay.getBillId());
        assertEquals(200.50, pay.getAmount());
        assertEquals("CASH", pay.getPaymentMethod());
        assertEquals("SUCCESS", pay.getStatus());
        assertEquals("TXN123456", pay.getTransactionId());
        assertEquals("Paid in full", pay.getRemarks());
    }

    @Test
    void testToString() {
        Payment pay = new Payment("P003", "B003", 300.75, "ONLINE");
        String str = pay.toString();

        assertTrue(str.contains("P003"));
        assertTrue(str.contains("B003"));
        assertTrue(str.contains("300.75"));
        assertTrue(str.contains("ONLINE"));
        assertTrue(str.contains("PENDING"));
    }
}
