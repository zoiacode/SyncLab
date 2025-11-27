package model;

import java.util.Date;
import java.util.UUID;

public class Department {

    private UUID id;
    private String name;
    private String abbreviation;
    private final Date createdAt;
    private Date updatedAt;

    public Department(String name, String abbreviation) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.abbreviation = abbreviation;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    public Department(UUID id, String name, String abbreviation, Date createdAt, Date updatedAt) {
        this.id = id;
        this.name = name;
        this.abbreviation = abbreviation;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        touch();
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
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
}
