package model;

import java.util.Date;
import java.util.UUID;

public class Person {

    private UUID id;
    private String name;
    private String phoneNumber;
    private String cpf;
    private Date birthDate;
    private String profileUrl;
    private String description;
    private Date desactivatedAt;
    private String personCode;
    private Date createdAt;
    private Date updatedAt;
  

    private String role;

    
    public Person() {
    }
    public Person(
            String name,
            String phoneNumber,
            String cpf,
            Date birthDate,
            String profileUrl,
            String description,
            Date desactivatedAt,
            String personCode,
            String role
    ) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.cpf = cpf;
        this.birthDate = birthDate;
        this.profileUrl = profileUrl;
        this.description = description;
        this.desactivatedAt = desactivatedAt;
        this.personCode = personCode;
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.role = role;
    }

    public Person(
            UUID id,
            Date createdAt,
            Date updatedAt,
            String name,
            String phoneNumber,
            String cpf,
            Date birthDate,
            String profileUrl,
            String description,
            Date desactivatedAt,
            String personCode,
            String role
    ) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.cpf = cpf;
        this.birthDate = birthDate;
        this.profileUrl = profileUrl;
        this.description = description;
        this.desactivatedAt = desactivatedAt;
        this.personCode = personCode;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.role = role;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
        this.updatedAt = new Date();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.updatedAt = new Date();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        this.updatedAt = new Date();
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
        this.updatedAt = new Date();
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
        this.updatedAt = new Date();
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
        this.updatedAt = new Date();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        this.updatedAt = new Date();
    }

    public Date getDesactivatedAt() {
        return desactivatedAt;
    }

    public void setDesactivatedAt(Date desactivatedAt) {
        this.desactivatedAt = desactivatedAt;
        this.updatedAt = new Date();
    }

    public String getPersonCode() {
        return personCode;
    }

    public void setPersonCode(String personCode) {
        this.personCode = personCode;
        this.updatedAt = new Date();
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
        this.touch();
    }

    protected void touch() {
        this.updatedAt = new Date();
    }

      public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
