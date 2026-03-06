package com.oceanview.modal;

import java.io.Serializable;


public class RoomType implements Serializable {
    private static final long serialVersionUID = 1L;

    private String typeId;
    private String typeName; // SINGLE, DOUBLE, SUITE, DELUXE
    private double baseRate;
    private String description;
    private int maxOccupancy;
    private String amenities; // JSON or comma-separated

    // Constructors
    public RoomType() {
    }

    public RoomType(String typeId, String typeName, double baseRate, int maxOccupancy) {
        this.typeId = typeId;
        this.typeName = typeName;
        this.baseRate = baseRate;
        this.maxOccupancy = maxOccupancy;
    }

    // Getters and Setters
    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public double getBaseRate() {
        return baseRate;
    }

    public void setBaseRate(double baseRate) {
        this.baseRate = baseRate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMaxOccupancy() {
        return maxOccupancy;
    }

    public void setMaxOccupancy(int maxOccupancy) {
        this.maxOccupancy = maxOccupancy;
    }

    public String getAmenities() {
        return amenities;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    @Override
    public String toString() {
        return "RoomType{" +
                "typeId='" + typeId + '\'' +
                ", typeName='" + typeName + '\'' +
                ", baseRate=" + baseRate +
                ", maxOccupancy=" + maxOccupancy +
                '}';
    }
}
