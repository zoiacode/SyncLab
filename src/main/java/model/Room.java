package model;

import java.util.Date;
import java.util.UUID;

import org.optaplanner.core.api.domain.lookup.PlanningId;

public class Room {

    @PlanningId // identificação de entidade unico via optaplaner
    private UUID id;

    private int capacity;
    private String roomType; // "CLASS", "LAB", "AUDITORIUM"
    private String code;     // código único
    private String status;   // "FREE", "PARTIALLY_OCCUPIED", "OCCUPIED"
    private String imageUrl;
    private UUID buildingId;
    private int floor;
    private Date createdAt;
    private Date updatedAt;

  

    public Room() {

    }

    public Room(int capacity, String roomType, String code, String status, String imageUrl, UUID buildingId, int floor) {
        if (capacity <= 0) throw new IllegalArgumentException("Capacity must be greater than 0");
        if (!roomType.equals("CLASS") && !roomType.equals("LAB") && !roomType.equals("AUDITORIUM"))
            throw new IllegalArgumentException("Invalid room type");
        if (!status.equals("FREE") && !status.equals("PARTIALLY_OCCUPIED") && !status.equals("OCCUPIED"))
            throw new IllegalArgumentException("Invalid status");

        this.id = UUID.randomUUID();
        this.capacity = capacity;
        this.roomType = roomType;
        this.code = code;
        this.status = status;
        this.imageUrl = imageUrl;
        this.buildingId = buildingId;
        this.floor = floor;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    public Room(UUID id, int capacity, String roomType, String code, String status, String imageUrl, UUID buildingId, int floor, Date createdAt, Date updatedAt) {
        this.id = id;
        this.capacity = capacity;
        this.roomType = roomType;
        this.code = code;
        this.status = status;
        this.imageUrl = imageUrl;
        this.buildingId = buildingId;
        this.floor = floor;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
        touch();
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException("Capacity must be greater than 0");
        this.capacity = capacity;
        touch();
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        if (!roomType.equals("CLASS") && !roomType.equals("LAB") && !roomType.equals("AUDITORIUM"))
            throw new IllegalArgumentException("Invalid room type");
        this.roomType = roomType;
        touch();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
        touch();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (!status.equals("FREE") && !status.equals("PARTIALLY_OCCUPIED") && !status.equals("OCCUPIED"))
            throw new IllegalArgumentException("Invalid status");
        this.status = status;
        touch();
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        touch();
    }

    public UUID getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(UUID buildingId) {
        this.buildingId = buildingId;
        touch();
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
        touch();
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    protected void touch() {
        this.updatedAt = new Date();
    }

    @Override
    public String toString() {
        return "Room{" + code + " (" + roomType + ")}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Room)) return false;
        Room other = (Room) o;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

      public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
