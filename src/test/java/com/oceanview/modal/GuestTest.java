package com.oceanview.modal;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class GuestTest {

    @Test
    void testGuestConstructors() {
        Guest guest1 = new Guest();
        assertNotNull(guest1.getCreatedDate());

        Guest guest2 = new Guest("G001", "Alice", "Smith", "1234567890");
        assertEquals("G001", guest2.getGuestId());
        assertEquals("Alice", guest2.getFirstName());
        assertEquals("Smith", guest2.getLastName());
        assertEquals("1234567890", guest2.getContactNumber());
        assertEquals("Alice Smith", guest2.getFullName());
    }

    @Test
    void testSettersAndGetters() {
        Guest guest = new Guest();
        guest.setFirstName("Bob");
        guest.setLastName("Johnson");
        guest.setEmail("bob@example.com");
        guest.setAddress("123 Main St");
        guest.setIdentificationNumber("ID987654");
        guest.setNationality("US");

        LocalDate dob = LocalDate.of(1990, 5, 20);
        guest.setDateOfBirth(dob);

        assertEquals("Bob", guest.getFirstName());
        assertEquals("Johnson", guest.getLastName());
        assertEquals("Bob Johnson", guest.getFullName());
        assertEquals("bob@example.com", guest.getEmail());
        assertEquals("123 Main St", guest.getAddress());
        assertEquals("ID987654", guest.getIdentificationNumber());
        assertEquals("US", guest.getNationality());
        assertEquals(dob, guest.getDateOfBirth());
    }

    @Test
    void testToString() {
        Guest guest = new Guest("G002", "Charlie", "Brown", "0987654321");
        guest.setEmail("charlie@example.com");
        String str = guest.toString();

        assertTrue(str.contains("G002"));
        assertTrue(str.contains("Charlie Brown"));
        assertTrue(str.contains("0987654321"));
        assertTrue(str.contains("charlie@example.com"));
    }
}
