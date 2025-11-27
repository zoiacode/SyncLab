package model;

import java.util.Date;
import java.util.UUID;

import org.optaplanner.core.api.domain.lookup.PlanningId;

public class Equipment {

    @PlanningId
    private UUID id;
    private String name;
    private String description;
    private int quantity;
    private String status; 
    private int maxLoanDuration; 
    private String imageUrl;
    private Date createdAt;
    private Date updatedAt;



    public Equipment() {}

    public Equipment(String name, String description, int quantity, String status, int maxLoanDuration, String imageUrl) {
        if (quantity < 0) throw new IllegalArgumentException("Quantity cannot be negative");
        if (maxLoanDuration <= 0) throw new IllegalArgumentException("Max loan duration must be greater than 0");
        if (!status.equals("Available") && !status.equals("Reserved") && !status.equals("Under Maintenance"))
            throw new IllegalArgumentException("Invalid status");

        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.status = status;
        this.maxLoanDuration = maxLoanDuration;
        this.imageUrl = imageUrl;
        this.createdAt = new Date();
        this.updatedAt = new Date();

    }

    public Equipment(UUID id, String name, String description, int quantity, String status, int maxLoanDuration, String imageUrl, Date createdAt, Date updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.status = status;
        this.maxLoanDuration = maxLoanDuration;
        this.imageUrl = imageUrl;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        touch();
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) throw new IllegalArgumentException("Quantity cannot be negative");
        this.quantity = quantity;
        touch();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (!status.equals("Available") && !status.equals("Reserved") && !status.equals("Under Maintenance"))
            throw new IllegalArgumentException("Invalid status");
        this.status = status;
        touch();
    }

    public int getMaxLoanDuration() {
        return maxLoanDuration;
    }

    public void setMaxLoanDuration(int maxLoanDuration) {
        if (maxLoanDuration <= 0) throw new IllegalArgumentException("Max loan duration must be greater than 0");
        this.maxLoanDuration = maxLoanDuration;
        touch();
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Equipment)) return false;
        Equipment other = (Equipment) obj;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
}

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

}
