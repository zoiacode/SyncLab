package model;

import java.util.UUID;

public class UserEquipmentHistory {
    private UUID userId;
    private UUID equipmentId;
    private int usageCount;

    public UserEquipmentHistory(UUID userId, UUID equipmentId, int usageCount) {
        this.userId = userId;
        this.equipmentId = equipmentId;
        this.usageCount = usageCount;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getEquipmentId() {
        return equipmentId;
    }

    public int getUsageCount() {
        return usageCount;
    }
}
