package com.oceanview.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ValidationUtilTest {

    @Test
    void testIsEmpty() {
        assertTrue(ValidationUtil.isEmpty(null));
        assertTrue(ValidationUtil.isEmpty(""));
        assertTrue(ValidationUtil.isEmpty("   "));
        assertFalse(ValidationUtil.isEmpty("text"));
    }

    @Test
    void testIsValidEmail() {
        assertTrue(ValidationUtil.isValidEmail("test@example.com"));
        assertTrue(ValidationUtil.isValidEmail("first.last@domain.co.uk"));
        assertFalse(ValidationUtil.isValidEmail("invalid-email"));
        assertFalse(ValidationUtil.isValidEmail("test@.com"));
        assertFalse(ValidationUtil.isValidEmail(""));
        assertFalse(ValidationUtil.isValidEmail(null));
    }

    @Test
    void testIsValidPhone() {
        assertTrue(ValidationUtil.isValidPhone("1234567890"));
        assertTrue(ValidationUtil.isValidPhone("123-456-7890"));
        assertTrue(ValidationUtil.isValidPhone("(123) 456-7890"));
        assertTrue(ValidationUtil.isValidPhone("+1 123 456 7890"));
        assertFalse(ValidationUtil.isValidPhone("123")); // too short
        assertFalse(ValidationUtil.isValidPhone("abcdefghij"));
        assertFalse(ValidationUtil.isValidPhone(""));
        assertFalse(ValidationUtil.isValidPhone(null));
    }

    @Test
    void testIsAlphanumeric() {
        assertTrue(ValidationUtil.isAlphanumeric("Test123"));
        assertTrue(ValidationUtil.isAlphanumeric("123"));
        assertTrue(ValidationUtil.isAlphanumeric("test"));
        assertFalse(ValidationUtil.isAlphanumeric("Test 123"));
        assertFalse(ValidationUtil.isAlphanumeric("Test@123"));
        assertFalse(ValidationUtil.isAlphanumeric(""));
        assertFalse(ValidationUtil.isAlphanumeric(null));
    }

    @Test
    void testIsValidLength() {
        assertTrue(ValidationUtil.isValidLength("test", 2, 5));
        assertTrue(ValidationUtil.isValidLength("test", 4, 4));
        assertFalse(ValidationUtil.isValidLength("test12", 2, 5)); // too long
        assertFalse(ValidationUtil.isValidLength("t", 2, 5)); // too short
        assertFalse(ValidationUtil.isValidLength("", 2, 5));
        assertFalse(ValidationUtil.isValidLength(null, 2, 5));
    }

    @Test
    void testIsPositiveNumber() {
        assertTrue(ValidationUtil.isPositiveNumber(1.0));
        assertTrue(ValidationUtil.isPositiveNumber(100.5));
        assertFalse(ValidationUtil.isPositiveNumber(0.0));
        assertFalse(ValidationUtil.isPositiveNumber(-5.5));
    }

    @Test
    void testIsInRange() {
        assertTrue(ValidationUtil.isInRange(5, 1, 10));
        assertTrue(ValidationUtil.isInRange(1, 1, 10));
        assertTrue(ValidationUtil.isInRange(10, 1, 10));
        assertFalse(ValidationUtil.isInRange(0, 1, 10));
        assertFalse(ValidationUtil.isInRange(11, 1, 10));
    }

    @Test
    void testSanitizeInput() {
        assertEquals("test string", ValidationUtil.sanitizeInput("test string"));
        assertEquals("select  from users", ValidationUtil.sanitizeInput("select * from users;"));
        assertEquals("", ValidationUtil.sanitizeInput(null));
    }

    @Test
    void testIsValidReservationNumber() {
        assertTrue(ValidationUtil.isValidReservationNumber("RES-2023-12345"));
        assertFalse(ValidationUtil.isValidReservationNumber("RES-202-12345"));
        assertFalse(ValidationUtil.isValidReservationNumber("RES-2023-1234"));
        assertFalse(ValidationUtil.isValidReservationNumber("ABC-2023-12345"));
        assertFalse(ValidationUtil.isValidReservationNumber(""));
        assertFalse(ValidationUtil.isValidReservationNumber(null));
    }
}
