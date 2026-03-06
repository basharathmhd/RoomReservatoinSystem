package com.oceanview.modal;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserConstructors() {
        User user1 = new User();
        assertNotNull(user1.getCreatedDate());
        assertTrue(user1.isActive());

        User user2 = new User("U001", "johndoe", "hashed_pwd", "John Doe", "ADMIN");
        assertEquals("U001", user2.getUserId());
        assertEquals("johndoe", user2.getUsername());
        assertEquals("hashed_pwd", user2.getPassword());
        assertEquals("John Doe", user2.getFullName());
        assertEquals("ADMIN", user2.getRole());
        assertTrue(user2.isActive());
        assertNotNull(user2.getCreatedDate());
    }

    @Test
    void testUserSettersAndGetters() {
        User user = new User();
        user.setUserId("U002");
        user.setUsername("janedoe");
        user.setPassword("secret");
        user.setFullName("Jane Doe");
        user.setRole("STAFF");
        user.setActive(false);

        assertEquals("U002", user.getUserId());
        assertEquals("janedoe", user.getUsername());
        assertEquals("secret", user.getPassword());
        assertEquals("Jane Doe", user.getFullName());
        assertEquals("STAFF", user.getRole());
        assertFalse(user.isActive());
    }

    @Test
    void testUserToString() {
        User user = new User("U001", "johndoe", "pwd", "John Doe", "ADMIN");
        String str = user.toString();
        assertTrue(str.contains("U001"));
        assertTrue(str.contains("johndoe"));
        assertTrue(str.contains("John Doe"));
        assertTrue(str.contains("ADMIN"));
    }
}
