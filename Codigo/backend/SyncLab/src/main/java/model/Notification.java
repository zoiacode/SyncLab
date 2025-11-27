package model;

import java.util.Date;
import java.util.UUID;

public class Notification {

    private UUID id;
    private UUID personId;
    private String title;
    private String message;
    private Date sentAt;
    private Date readAt;
    private final Date createdAt;
    private Date updatedAt;

    public Notification(UUID personId, String title, String message) {
        this.id = UUID.randomUUID();
        this.personId = personId;
        this.title = title;
        this.message = message;
        this.sentAt = new Date();
        this.readAt = null;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    public Notification(UUID id, UUID personId, String title, String message, Date sentAt, Date readAt, Date createdAt, Date updatedAt) {
        this.id = id;
        this.personId = personId;
        this.title = title;
        this.message = message;
        this.sentAt = sentAt;
        this.readAt = readAt;
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

    public UUID getPersonId() {
        return personId;
    }

    public void setPersonId(UUID personId) {
        this.personId = personId;
        touch();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        touch();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        touch();
    }

    public Date getSentAt() {
        return sentAt;
    }

    public void setSentAt(Date sentAt) {
        this.sentAt = sentAt;
        touch();
    }

    public Date getReadAt() {
        return this.readAt;
    }

    public void setReadAt(Date readAt) {
        this.readAt = readAt;
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
