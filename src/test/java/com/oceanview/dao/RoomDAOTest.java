package com.oceanview.dao;

import com.oceanview.modal.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class RoomDAOTest {

    private RoomDAO roomDAO;
    private Room room;

    @BeforeEach
    void setUp() {
        roomDAO = new RoomDAO();
        room = new Room("R001", "101", "T001", 1);
    }


    @Test
    void testRoomDefaultConstructorSetsAvailableStatus() {
        Room defaultRoom = new Room();
        assertEquals("AVAILABLE", defaultRoom.getStatus(),
                "Default constructor should set status to AVAILABLE");
    }

    @Test
    void testRoomParameterizedConstructor() {
        assertEquals("R001", room.getRoomId());
        assertEquals("101", room.getRoomNumber());
        assertEquals("T001", room.getTypeId());
        assertEquals(1, room.getFloor());
    }

    @Test
    void testRoomParameterizedConstructorInheritsDefaultStatus() {
        assertEquals("AVAILABLE", room.getStatus(),
                "Parameterized constructor should also default status to AVAILABLE");
    }

    // --- Getter and Setter Tests ---

    @Test
    void testSetAndGetRoomId() {
        room.setRoomId("R999");
        assertEquals("R999", room.getRoomId());
    }

    @Test
    void testSetAndGetRoomNumber() {
        room.setRoomNumber("505");
        assertEquals("505", room.getRoomNumber());
    }

    @Test
    void testSetAndGetTypeId() {
        room.setTypeId("T003");
        assertEquals("T003", room.getTypeId());
    }

    @Test
    void testSetAndGetFloor() {
        room.setFloor(5);
        assertEquals(5, room.getFloor());
    }

    @Test
    void testSetAndGetCapacity() {
        room.setCapacity(4);
        assertEquals(4, room.getCapacity());
    }

    @Test
    void testSetAndGetStatus() {
        room.setStatus("OCCUPIED");
        assertEquals("OCCUPIED", room.getStatus());
    }

    @Test
    void testSetAndGetAmenities() {
        room.setAmenities("WIFI, AC, Mini Bar");
        assertEquals("WIFI, AC, Mini Bar", room.getAmenities());
    }

    // --- Business Logic Tests ---

    @Test
    void testIsAvailableWhenStatusIsAvailable() {
        room.setStatus("AVAILABLE");
        assertTrue(room.isAvailable(), "Room with AVAILABLE status should return true");
    }

    @Test
    void testIsAvailableWhenStatusIsOccupied() {
        room.setStatus("OCCUPIED");
        assertFalse(room.isAvailable(), "Room with OCCUPIED status should return false");
    }

    @Test
    void testIsAvailableWhenStatusIsMaintenance() {
        room.setStatus("MAINTENANCE");
        assertFalse(room.isAvailable(), "Room with MAINTENANCE status should return false");
    }

    @Test
    void testIsAvailableWhenStatusIsReserved() {
        room.setStatus("RESERVED");
        assertFalse(room.isAvailable(), "Room with RESERVED status should return false");
    }

    @Test
    void testIsAvailableWithNullStatus() {
        room.setStatus(null);
        assertFalse(room.isAvailable(), "Room with null status should return false");
    }

    // --- toString Tests ---

    @Test
    void testToStringContainsRoomId() {
        assertTrue(room.toString().contains("R001"),
                "toString should contain the room ID");
    }

    @Test
    void testToStringContainsRoomNumber() {
        assertTrue(room.toString().contains("101"),
                "toString should contain the room number");
    }

    @Test
    void testToStringContainsStatus() {
        assertTrue(room.toString().contains("AVAILABLE"),
                "toString should contain the status");
    }

    // --- Edge Case Tests ---

    @Test
    void testCapacityDefaultsToZero() {
        Room newRoom = new Room();
        assertEquals(0, newRoom.getCapacity(),
                "Default capacity should be 0");
    }

    @Test
    void testFloorDefaultsToZero() {
        Room newRoom = new Room();
        assertEquals(0, newRoom.getFloor(),
                "Default floor should be 0");
    }

    @Test
    void testNullAmenitiesByDefault() {
        Room newRoom = new Room();
        assertNull(newRoom.getAmenities(),
                "Amenities should be null by default");
    }

    @Test
    void testRoomDAOInstantiation() {
        assertNotNull(roomDAO, "RoomDAO should be instantiable");
    }

    @Test
    void testMultipleStatusTransitions() {
        room.setStatus("AVAILABLE");
        assertTrue(room.isAvailable());

        room.setStatus("RESERVED");
        assertFalse(room.isAvailable());

        room.setStatus("OCCUPIED");
        assertFalse(room.isAvailable());

        room.setStatus("MAINTENANCE");
        assertFalse(room.isAvailable());

        room.setStatus("AVAILABLE");
        assertTrue(room.isAvailable());
    }

    @Test
    void testRoomTypeAssociation() {
        assertNull(room.getRoomType(), "RoomType should be null by default");
    }
}
