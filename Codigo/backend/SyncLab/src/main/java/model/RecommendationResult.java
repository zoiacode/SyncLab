package model;

import java.util.List;

public class RecommendationResult {
    private List<Room> roomRecommendations;
    // private List<Equipment> equipmentRecommendations;

    public RecommendationResult(List<Room> roomRecommendations) {
        this.roomRecommendations = roomRecommendations;
        // this.equipmentRecommendations = equipmentRecommendations;
    }

    public List<Room> getRoomRecommendations() {
        return roomRecommendations;
    }

    // public List<Equipment> getEquipmentRecommendations() {
    //     return equipmentRecommendations;
    // }
}
