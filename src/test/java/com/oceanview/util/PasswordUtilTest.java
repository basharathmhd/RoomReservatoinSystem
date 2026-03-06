package com.oceanview.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PasswordUtilTest {

    @Test
    void testGenerateSalt() {
        String salt1 = PasswordUtil.generateSalt();
        String salt2 = PasswordUtil.generateSalt();
        assertNotNull(salt1);
        assertNotNull(salt2);
        assertNotEquals(salt1, salt2); // Salts should be unique
        assertTrue(salt1.length() > 0);
    }

    @Test
    void testHashPasswordWithSalt() {
        String salt = PasswordUtil.generateSalt();
        String hash1 = PasswordUtil.hashPassword("myPassword123", salt);
        String hash2 = PasswordUtil.hashPassword("myPassword123", salt);
        assertNotNull(hash1);
        assertEquals(hash1, hash2); // Same password and salt should yield same hash
    }

    @Test
    void testHashPassword() {
        String hash1 = PasswordUtil.hashPassword("myPassword123");
        String hash2 = PasswordUtil.hashPassword("myPassword123");
        assertNotNull(hash1);
        assertTrue(hash1.contains(":"));
        assertNotEquals(hash1, hash2); // Different auto-generated salts should yield different hashes
    }

    @Test
    void testVerifyPassword() {
        String password = "SecurePassword!123";
        String hash = PasswordUtil.hashPassword(password);

        assertTrue(PasswordUtil.verifyPassword(password, hash));
        assertFalse(PasswordUtil.verifyPassword("wrongpassword", hash));
        assertFalse(PasswordUtil.verifyPassword(password, "invalidhashformat"));
        assertFalse(PasswordUtil.verifyPassword(password, null));
    }

    @Test
    void testIsStrongPassword() {
        assertTrue(PasswordUtil.isStrongPassword("StrongPass1!"));
        assertFalse(PasswordUtil.isStrongPassword("weak")); // Too short
        assertFalse(PasswordUtil.isStrongPassword("nouppercase1!")); // No upper
        assertFalse(PasswordUtil.isStrongPassword("NOLOWERCASE1!")); // No lower
        assertFalse(PasswordUtil.isStrongPassword("NoDigitSpecial!")); // No digit
        assertFalse(PasswordUtil.isStrongPassword("NoSpecialChar123")); // No special
        assertFalse(PasswordUtil.isStrongPassword(null));
    }
}
