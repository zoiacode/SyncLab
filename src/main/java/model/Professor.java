package model;

import java.util.Date;
import java.util.UUID;

public class Professor {

    private UUID id;
    private UUID personId;
    private String academicDegree;
    private String expertiseArea;
    private String employmentStatus;
    private final Date createdAt;
    private Date updatedAt;
    private String name;


    // Construtor com novos objetos
    public Professor(
            UUID personId,
            String academicDegree,
            String expertiseArea,
            String employmentStatus
    ) {
        this.id = UUID.randomUUID();
        this.personId = personId;
        this.academicDegree = academicDegree;
        this.expertiseArea = expertiseArea;
        this.employmentStatus = employmentStatus;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    // Construtor completo (ex: ao carregar do banco)
    public Professor(
            UUID id,
            UUID personId,
            String academicDegree,
            String expertiseArea,
            String employmentStatus,
            Date createdAt,
            Date updatedAt
    ) {
        this.id = id;
        this.personId = personId;
        this.academicDegree = academicDegree;
        this.expertiseArea = expertiseArea;
        this.employmentStatus = employmentStatus;
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
    
    public String getAcademicDegree() {
        return academicDegree;
    }

    public void setAcademicDegree(String academicDegree) {
        this.academicDegree = academicDegree;
        touch();
    }

    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExpertiseArea() {
        return expertiseArea;
    }

    public void setExpertiseArea(String expertiseArea) {
        this.expertiseArea = expertiseArea;
        touch();
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
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
