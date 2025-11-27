package model;

import java.util.UUID;

public class UserRoomHistory {
    private UUID userId;
    private UUID roomId;
    private int usageCount;

    public UserRoomHistory(UUID userId, UUID roomId, int usageCount) {
        this.userId = userId;
        this.roomId = roomId;
        this.usageCount = usageCount;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getRoomId() {
        return roomId;
    }

    public int getUsageCount() {
        return usageCount;
    }
}
