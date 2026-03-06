package com.oceanview.dao;

import com.oceanview.modal.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


class UserDAOTest {

    private UserDAO userDAO;
    private User user;

    @BeforeEach
    void setUp() {
        userDAO = new UserDAO();
        user = new User("U001", "admin", "hashedpass123", "Admin User", "ADMIN");
    }

    // --- User Entity Construction Tests ---

    @Test
    void testUserDefaultConstructorSetsActiveTrue() {
        User defaultUser = new User();
        assertTrue(defaultUser.isActive(),
                "Default constructor should set isActive to true");
    }

    @Test
    void testUserDefaultConstructorSetsCreatedDate() {
        User defaultUser = new User();
        assertNotNull(defaultUser.getCreatedDate(),
                "Default constructor should set createdDate");
    }

    @Test
    void testUserParameterizedConstructor() {
        assertEquals("U001", user.getUserId());
        assertEquals("admin", user.getUsername());
        assertEquals("hashedpass123", user.getPassword());
        assertEquals("Admin User", user.getFullName());
        assertEquals("ADMIN", user.getRole());
    }

    @Test
    void testUserParameterizedConstructorInheritsDefaults() {
        assertTrue(user.isActive(),
                "Parameterized constructor should inherit isActive = true");
        assertNotNull(user.getCreatedDate(),
                "Parameterized constructor should inherit createdDate");
    }

    // --- Getter and Setter Tests ---

    @Test
    void testSetAndGetUserId() {
        user.setUserId("U999");
        assertEquals("U999", user.getUserId());
    }

    @Test
    void testSetAndGetUsername() {
        user.setUsername("newuser");
        assertEquals("newuser", user.getUsername());
    }

    @Test
    void testSetAndGetPassword() {
        user.setPassword("newHashedPass");
        assertEquals("newHashedPass", user.getPassword());
    }

    @Test
    void testSetAndGetFullName() {
        user.setFullName("John Doe");
        assertEquals("John Doe", user.getFullName());
    }

    @Test
    void testSetAndGetRole() {
        user.setRole("STAFF");
        assertEquals("STAFF", user.getRole());
    }

    @Test
    void testSetAndGetActive() {
        user.setActive(false);
        assertFalse(user.isActive());

        user.setActive(true);
        assertTrue(user.isActive());
    }

    @Test
    void testSetAndGetLastLogin() {
        LocalDateTime loginTime = LocalDateTime.of(2026, 3, 5, 10, 30, 0);
        user.setLastLogin(loginTime);
        assertEquals(loginTime, user.getLastLogin());
    }

    @Test
    void testSetAndGetCreatedDate() {
        LocalDateTime created = LocalDateTime.of(2026, 1, 1, 0, 0, 0);
        user.setCreatedDate(created);
        assertEquals(created, user.getCreatedDate());
    }

    @Test
    void testSetAndGetCreatedBy() {
        user.setCreatedBy("system");
        assertEquals("system", user.getCreatedBy());
    }

    @Test
    void testSetAndGetModifiedDate() {
        LocalDateTime modified = LocalDateTime.of(2026, 2, 15, 14, 0, 0);
        user.setModifiedDate(modified);
        assertEquals(modified, user.getModifiedDate());
    }

    @Test
    void testSetAndGetModifiedBy() {
        user.setModifiedBy("admin");
        assertEquals("admin", user.getModifiedBy());
    }

    // --- Role Validation Tests ---

    @Test
    void testAdminRole() {
        user.setRole("ADMIN");
        assertEquals("ADMIN", user.getRole());
    }

    @Test
    void testStaffRole() {
        user.setRole("STAFF");
        assertEquals("STAFF", user.getRole());
    }

    @Test
    void testManagerRole() {
        user.setRole("MANAGER");
        assertEquals("MANAGER", user.getRole());
    }

    // --- toString Tests ---

    @Test
    void testToStringContainsUserId() {
        assertTrue(user.toString().contains("U001"),
                "toString should contain the user ID");
    }

    @Test
    void testToStringContainsUsername() {
        assertTrue(user.toString().contains("admin"),
                "toString should contain the username");
    }

    @Test
    void testToStringContainsRole() {
        assertTrue(user.toString().contains("ADMIN"),
                "toString should contain the role");
    }

    @Test
    void testToStringContainsFullName() {
        assertTrue(user.toString().contains("Admin User"),
                "toString should contain the full name");
    }

    @Test
    void testToStringContainsActiveStatus() {
        assertTrue(user.toString().contains("isActive=true"),
                "toString should contain the active status");
    }

    // --- Edge Case Tests ---

    @Test
    void testLastLoginDefaultsToNull() {
        User newUser = new User();
        assertNull(newUser.getLastLogin(),
                "Last login should be null by default");
    }

    @Test
    void testModifiedDateDefaultsToNull() {
        User newUser = new User();
        assertNull(newUser.getModifiedDate(),
                "Modified date should be null by default");
    }

    @Test
    void testCreatedByDefaultsToNull() {
        User newUser = new User();
        assertNull(newUser.getCreatedBy(),
                "Created by should be null by default");
    }

    @Test
    void testUserDAOInstantiation() {
        assertNotNull(userDAO, "UserDAO should be instantiable");
    }

    @Test
    void testDeactivateAndReactivateUser() {
        assertTrue(user.isActive());

        user.setActive(false);
        assertFalse(user.isActive());

        user.setActive(true);
        assertTrue(user.isActive());
    }

    @Test
    void testCreatedDateIsRecentForNewUser() {
        User newUser = new User();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime created = newUser.getCreatedDate();

        assertNotNull(created);
        assertTrue(created.isBefore(now.plusSeconds(1)),
                "Created date should be before or equal to now");
        assertTrue(created.isAfter(now.minusSeconds(5)),
                "Created date should be within the last 5 seconds");
    }
}
