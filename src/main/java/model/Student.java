package model;

import java.util.Date;
import java.util.UUID;

public class Student {

    private UUID id;
    private UUID personId;
    private String registrationNumber;
    private String semester;
    private String shift;
    private String scholarshipType;
    private String academicStatus;
    private String course;
    private final Date createdAt;
    private Date updatedAt;

    // Construtor para novos objetos
    public Student(
            UUID personId,
            String registrationNumber,
            String course,
            String semester,
            String shift,
            String scholarshipType,
            String academicStatus
    ) {
        this.id = UUID.randomUUID();
        this.personId = personId;
        this.course = course;
        this.registrationNumber = registrationNumber;
        this.semester = semester;
        this.shift = shift;
        this.scholarshipType = scholarshipType;
        this.academicStatus = academicStatus;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    // Construtor completo (para carregar do banco)
    public Student(
            UUID id,
            UUID personId,
            String registrationNumber,
            String course,
            String semester,
            String shift,
            String scholarshipType,
            String academicStatus,
            Date createdAt,
            Date updatedAt
    ) {
        this.id = id;
        this.personId = personId;
        this.registrationNumber = registrationNumber;
        this.semester = semester;
        this.shift = shift;
        this.scholarshipType = scholarshipType;
        this.academicStatus = academicStatus;
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

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
        touch();
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
        touch();
    }

    public String getCourse() {
        return this.course;
    }

    public void setCourse(String value) {
        this.course = value;
        this.touch();
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
        touch();
    }

    public String getScholarshipType() {
        return scholarshipType;
    }

    public void setScholarshipType(String scholarshipType) {
        this.scholarshipType = scholarshipType;
        touch();
    }

    public String getAcademicStatus() {
        return academicStatus;
    }

    public void setAcademicStatus(String academicStatus) {
        this.academicStatus = academicStatus;
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
