package com.oceanview.modal;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RoomTest {

    @Test
    void testRoomConstructors() {
        Room room1 = new Room();
        assertEquals("AVAILABLE", room1.getStatus());

        Room room2 = new Room("R001", "101", "T001", 1);
        assertEquals("R001", room2.getRoomId());
        assertEquals("101", room2.getRoomNumber());
        assertEquals("T001", room2.getTypeId());
        assertEquals(1, room2.getFloor());
        assertEquals("AVAILABLE", room2.getStatus());
    }

    @Test
    void testRoomSettersAndGetters() {
        Room room = new Room();
        room.setRoomId("R002");
        room.setRoomNumber("202");
        room.setTypeId("T002");
        room.setFloor(2);
        room.setCapacity(2);
        room.setStatus("OCCUPIED");
        room.setAmenities("WIFI, AC");

        assertEquals("R002", room.getRoomId());
        assertEquals("202", room.getRoomNumber());
        assertEquals("T002", room.getTypeId());
        assertEquals(2, room.getFloor());
        assertEquals(2, room.getCapacity());
        assertEquals("OCCUPIED", room.getStatus());
        assertEquals("WIFI, AC", room.getAmenities());
    }

    @Test
    void testIsAvailable() {
        Room room = new Room();
        assertTrue(room.isAvailable());

        room.setStatus("MAINTENANCE");
        assertFalse(room.isAvailable());
    }

    @Test
    void testRoomToString() {
        Room room = new Room("R001", "101", "T001", 1);
        String str = room.toString();
        assertTrue(str.contains("R001"));
        assertTrue(str.contains("101"));
        assertTrue(str.contains("T001"));
        assertTrue(str.contains("1"));
        assertTrue(str.contains("AVAILABLE"));
    }
}
