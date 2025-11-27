package model;

import java.util.Date;
import java.util.UUID;

public class Credential {

    private UUID id;
    private String email;
    private String password;
    private UUID personId;
    private final Date createdAt;
    private Date updatedAt;
    private Date refreshTokenExpiration;
    private String refreshToken;


    public Credential(String email, String password, UUID personId, String refreshToken, Date refreshTokenExpiration) {
        this.id = UUID.randomUUID();
        this.email = email;
        this.password = password;
        this.personId = personId;
        this.refreshToken = refreshToken;
        this.refreshTokenExpiration = refreshTokenExpiration;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    public Credential(UUID id, String email, String password, UUID personId, String refreshToken, Date refreshTokenExpiration, Date createdAt, Date updatedAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.personId = personId;
        this.refreshToken = refreshToken;
        this.refreshTokenExpiration = refreshTokenExpiration;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        touch();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        touch();
    }

    public UUID getPersonId() {
        return personId;
    }

    public void setPersonId(UUID personId) {
        this.personId = personId;
        touch();
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    
    public Date getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }
    
    public void setRefreshTokenExpiration(Date refreshTokenExpiration) {
        this.refreshTokenExpiration = refreshTokenExpiration;
        touch();
    }
    
    public String getRefreshToken() {
        return refreshToken;
    }
    
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        touch();
    }
    protected void touch() {
        this.updatedAt = new Date();
    }
}
