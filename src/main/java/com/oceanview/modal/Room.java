package com.oceanview.modal;

import java.io.Serializable;

/**
 * Room entity representing individual hotel rooms
 */
public class Room implements Serializable {
    private static final long serialVersionUID = 1L;

    private String roomId;
    private String roomNumber;
    private String typeId;
    private int floor;
    private int capacity;
    private String status; // AVAILABLE, OCCUPIED, MAINTENANCE, RESERVED
    private String amenities;

    // Additional field for joins
    private RoomType roomType;

    // Constructors
    public Room() {
        this.status = "AVAILABLE";
    }

    public Room(String roomId, String roomNumber, String typeId, int floor) {
        this();
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.typeId = typeId;
        this.floor = floor;
    }

    // Business methods
    public boolean isAvailable() {
        return "AVAILABLE".equals(status);
    }

    // Getters and Setters
    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAmenities() {
        return amenities;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomId='" + roomId + '\'' +
                ", roomNumber='" + roomNumber + '\'' +
                ", typeId='" + typeId + '\'' +
                ", floor=" + floor +
                ", status='" + status + '\'' +
                '}';
    }
}
