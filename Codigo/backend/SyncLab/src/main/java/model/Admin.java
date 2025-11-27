package model;

import java.util.Date;
import java.util.UUID;

public class Admin {

    private UUID id;
    private UUID personId;
    private String jobTitle;
    private final Date createdAt;
    private Date updatedAt;

    public Admin(UUID personId, String jobTitle) {
        this.id = UUID.randomUUID();
        this.personId = personId;
        this.jobTitle = jobTitle;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    public Admin(UUID id, UUID personId, String jobTitle, Date createdAt, Date updatedAt) {
        this.id = id;
        this.personId = personId;
        this.jobTitle = jobTitle;
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

    public UUID getPersonId() {
        return personId;
    }

    public void setPersonId(UUID personId) {
        this.personId = personId;
        touch();
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
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
