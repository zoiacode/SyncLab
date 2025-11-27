package model;

import java.util.Date;
import java.util.UUID;

public class Building {

    private UUID id;
    private String buildCode;
    private int floor;
    private String campus;
    private final Date createdAt;
    private Date updatedAt;

    // Construtor para novos objetos
    public Building(String buildCode, int floor, String campus) {
        this.id = UUID.randomUUID();
        this.buildCode = buildCode;
        this.floor = floor;
        this.campus = campus;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    // Construtor completo (para carregar do banco)
    public Building(UUID id, String buildCode, int floor, String campus, Date createdAt, Date updatedAt) {
        this.id = id;
        this.buildCode = buildCode;
        this.floor = floor;
        this.campus = campus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters e Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
        touch();
    }

    public String getBuildCode() {
        return buildCode;
    }

    public void setBuildCode(String buildCode) {
        this.buildCode = buildCode;
        touch();
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
        touch();
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
        touch();
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    // Atualiza updatedAt
    protected void touch() {
        this.updatedAt = new Date();
    }
}
