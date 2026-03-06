package com.oceanview.modal;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class ReservationTest {

    @Test
    void testReservationConstructors() {
        Reservation res1 = new Reservation();
        assertNotNull(res1.getCreatedDate());
        assertEquals("CONFIRMED", res1.getStatus());

        LocalDate in = LocalDate.now();
        LocalDate out = in.plusDays(3);
        Reservation res2 = new Reservation("RES-123", "G001", "R001", in, out);

        assertEquals("RES-123", res2.getReservationNumber());
        assertEquals("G001", res2.getGuestId());
        assertEquals("R001", res2.getRoomId());
        assertEquals(in, res2.getCheckInDate());
        assertEquals(out, res2.getCheckOutDate());
        assertEquals("CONFIRMED", res2.getStatus());
    }

    @Test
    void testSettersAndGetters() {
        Reservation res = new Reservation();
        res.setNumberOfGuests(2);
        res.setStatus("CHECKED_IN");
        res.setSpecialRequests("Late check-in");

        assertEquals(2, res.getNumberOfGuests());
        assertEquals("CHECKED_IN", res.getStatus());
        assertEquals("Late check-in", res.getSpecialRequests());
    }

    @Test
    void testCalculateStayDuration() {
        LocalDate in = LocalDate.of(2023, 10, 10);
        LocalDate out = LocalDate.of(2023, 10, 15);
        Reservation res = new Reservation("RES", "G", "R", in, out);

        assertEquals(5, res.calculateStayDuration());

        res.setCheckOutDate(null);
        assertEquals(0, res.calculateStayDuration());
    }

    @Test
    void testIsActive() {
        Reservation res = new Reservation();
        assertTrue(res.isActive()); // CONFIRMED

        res.setStatus("CHECKED_IN");
        assertTrue(res.isActive());

        res.setStatus("CANCELLED");
        assertFalse(res.isActive());

        res.setStatus("CHECKED_OUT");
        assertFalse(res.isActive());
    }

    @Test
    void testToString() {
        LocalDate in = LocalDate.of(2023, 10, 10);
        LocalDate out = LocalDate.of(2023, 10, 15);
        Reservation res = new Reservation("RES-123", "G001", "R001", in, out);
        String str = res.toString();

        assertTrue(str.contains("RES-123"));
        assertTrue(str.contains("G001"));
        assertTrue(str.contains("R001"));
        assertTrue(str.contains("CONFIRMED"));
    }
}
