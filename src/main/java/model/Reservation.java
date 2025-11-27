package model;

import java.util.Date;
import java.util.UUID;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.variable.PlanningVariable;


@PlanningEntity
public class Reservation {

    @PlanningId
    private UUID id;
    private UUID personId;
    private UUID equipmentId; // opcional

    private UUID roomId;      // opcional
    private String resourceType; // "Equipment" ou "Room"
    private String purpose;

    // @PlanningVariable(valueRangeProviderRefs = {"equipmentRange"})
    // private Equipment equipment;

    @PlanningVariable(valueRangeProviderRefs = {"roomRange"})
    private Room room;

    @PlanningVariable(valueRangeProviderRefs= {"timeRange"})
    private Date startTime;
    private Date endTime;
    private String status; // "Pending", "Approved", "Rejected"
    private Date createdAt;
    private Date updatedAt;
    private UUID courseId;

    private Person personRef;
    private Equipment equipmentRef;

    public Person getPersonRef() {
        return personRef;
    }

    public UUID getCourseId() {
        return this.courseId;
    }

    public void setCourseId(UUID id) {
        this.courseId = id; 
    }


    public void setPersonRef(Person personRef) {
        this.personRef = personRef;
    }


    public Equipment getEquipmentRef() {
        return equipmentRef;
    }


    public void setEquipmentRef(Equipment equipmentRef) {
        this.equipmentRef = equipmentRef;
    }


    public Reservation() {
        this.id = UUID.randomUUID();
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }


    // Construtor para novos objetos
    public Reservation(UUID personId, UUID equipmentId, UUID roomId, String resourceType, String purpose, Date startTime, Date endTime, String status) {
        if (endTime.before(startTime)) throw new IllegalArgumentException("End time must be after start time");
        if (!status.equals("Pending") && !status.equals("Approved") && !status.equals("Rejected"))
            throw new IllegalArgumentException("Invalid status");

        this.id = UUID.randomUUID();
        this.personId = personId;
        this.equipmentId = equipmentId;
        this.roomId = roomId;
        this.resourceType = resourceType;
        this.purpose = purpose;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    // Construtor completo (para carregar do banco)
    public Reservation(UUID id, UUID personId, UUID equipmentId, UUID roomId, String resourceType, String purpose, Date startTime, Date endTime, String status, Date createdAt, Date updatedAt) {
        this.id = id;
        this.personId = personId;
        this.equipmentId = equipmentId;
        this.roomId = roomId;
        this.resourceType = resourceType;
        this.purpose = purpose;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

      public Reservation(UUID id, UUID personId, UUID equipmentId, UUID roomId, String resourceType, String purpose, Date startTime, Date endTime, String status, Date createdAt, Date updatedAt, Room room) {
        this.id = id;
        this.personId = personId;
        this.equipmentId = equipmentId;
        this.roomId = roomId;
        this.resourceType = resourceType;
        this.purpose = purpose;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.room = room;
    }

    // Getters e Setters
    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
        touch();
    }

    public UUID getPersonId() {
        return personId;
    }

    public void setPersonId(UUID personId) {
        this.personId = personId;
        touch();
    }

    public UUID getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(UUID equipmentId) {
        this.equipmentId = equipmentId;
        touch();
    }

    public UUID getRoomId() {
        return roomId;
    }

    public void setRoomId(UUID roomId) {
        this.roomId = roomId;
        touch();
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
        touch();
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
        touch();
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
        touch();
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        if (startTime != null && endTime.before(startTime)) throw new IllegalArgumentException("End time must be after start time");
        this.endTime = endTime;
        touch();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (!status.equals("Pending") && !status.equals("Approved") && !status.equals("Rejected"))
            throw new IllegalArgumentException("Invalid status");
        this.status = status;
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

    //     public Equipment getEquipment() {
    //     return equipment;
    // }


    // public void setEquipment(Equipment equipment) {
    //     this.equipment = equipment;
    // }


    public Room getRoom() {
        return room;
    }


    public void setRoom(Room room) {
        this.room = room;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
