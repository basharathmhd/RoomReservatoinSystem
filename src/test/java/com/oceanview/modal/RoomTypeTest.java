package com.oceanview.modal;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RoomTypeTest {

    @Test
    void testRoomTypeConstructors() {
        RoomType rt1 = new RoomType();
        assertNull(rt1.getTypeId());

        RoomType rt2 = new RoomType("T001", "DELUXE", 150.0, 2);
        assertEquals("T001", rt2.getTypeId());
        assertEquals("DELUXE", rt2.getTypeName());
        assertEquals(150.0, rt2.getBaseRate());
        assertEquals(2, rt2.getMaxOccupancy());
    }

    @Test
    void testSettersAndGetters() {
        RoomType rt = new RoomType();
        rt.setTypeId("T002");
        rt.setTypeName("SUITE");
        rt.setBaseRate(300.0);
        rt.setDescription("Luxury suite with ocean view");
        rt.setMaxOccupancy(4);
        rt.setAmenities("WIFI, POOL, AC");

        assertEquals("T002", rt.getTypeId());
        assertEquals("SUITE", rt.getTypeName());
        assertEquals(300.0, rt.getBaseRate());
        assertEquals("Luxury suite with ocean view", rt.getDescription());
        assertEquals(4, rt.getMaxOccupancy());
        assertEquals("WIFI, POOL, AC", rt.getAmenities());
    }

    @Test
    void testToString() {
        RoomType rt = new RoomType("T005", "SINGLE", 80.0, 1);
        String str = rt.toString();

        assertTrue(str.contains("T005"));
        assertTrue(str.contains("SINGLE"));
        assertTrue(str.contains("80.0"));
        assertTrue(str.contains("1"));
    }
}
