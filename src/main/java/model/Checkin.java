package model;

import java.util.Date;
import java.util.UUID;

public class Checkin {

    private UUID id;
    private UUID studentId;
    private Date timestamp;
    private boolean isConfirmed;
    private UUID lectureId;
    private final Date createdAt;
    private Date updatedAt;

    // Construtor para novos objetos
    public Checkin(UUID studentId, UUID lectureId) {
        this.id = UUID.randomUUID();
        this.studentId = studentId;
        this.lectureId = lectureId;
        this.timestamp = new Date();
        this.isConfirmed = false;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    // Construtor completo (para carregar do banco)
    public Checkin(UUID id, UUID studentId, Date timestamp, boolean isConfirmed, UUID lectureId, Date createdAt, Date updatedAt) {
        this.id = id;
        this.studentId = studentId;
        this.timestamp = timestamp;
        this.isConfirmed = isConfirmed;
        this.lectureId = lectureId;
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

    public UUID getstudentId() {
        return studentId;
    }

    public void setstudentId(UUID studentId) {
        this.studentId = studentId;
        touch();
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
        touch();
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
        touch();
    }

    public UUID getLectureId() {
        return lectureId;
    }

    public void setLectureId(UUID lectureId) {
        this.lectureId = lectureId;
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
