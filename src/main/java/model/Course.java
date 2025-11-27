package model;

import java.util.Date;
import java.util.UUID;

public class Course {

    private UUID id;
    private String name;
    private String acg;
    private String schedule;
    private final Date createdAt;
    private Date updatedAt;

    public Course(String acg, String schedule, String name) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.acg = acg;
        this.schedule = schedule;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    // Construtor completo (para carregar do banco)
    public Course(UUID id, String name, String acg, String schedule, Date createdAt, Date updatedAt) {
        this.id = id;
        this.name = name;
        this.acg = acg;
        this.schedule = schedule;
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

    public String getAcg() {
        return acg;
    }

    public void setAcg(String acg) {
        this.acg = acg;
        touch();
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
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
