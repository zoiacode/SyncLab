package model;

import java.util.Date;
import java.util.List;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

@PlanningSolution
public class ReservationSchedule {

    @ValueRangeProvider(id = "roomRange")
    @ProblemFactCollectionProperty
    private List<Room> rooms;

    // @ValueRangeProvider(id = "equipmentRange")
    // @ProblemFactCollectionProperty
    // private List<Equipment> equipments;

    @ValueRangeProvider(id = "timeRange")
    @ProblemFactCollectionProperty
    private List<Date> possibleStartTimes;

    @ProblemFactCollectionProperty
    private List<UserRoomHistory> userRoomHistory;

    @ProblemFactCollectionProperty
    private List<Lecture> lectures;


    // @ProblemFactCollectionProperty
    // private List<UserEquipmentHistory> userEquipmentHistory;

   

    @PlanningEntityCollectionProperty
    private List<Reservation> reservations;

    @PlanningScore
    private HardSoftScore score;

    public ReservationSchedule() {
    }

    public ReservationSchedule(
            List<Room> rooms,
            List<Date> possibleStartTimes,
            List<Reservation> reservations,
            List<UserRoomHistory> userRoomHistory,
            List<Lecture> lectures
            ) {
        this.rooms = rooms;
        // this.equipments = equipments;
        this.possibleStartTimes = possibleStartTimes;
        this.reservations = reservations;
        this.userRoomHistory = userRoomHistory;
        this.lectures = lectures;
        // this.userEquipmentHistory = userEquipmentHistory;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    // public List<Equipment> getEquipments() {
    //     return equipments;
    // }

    // public void setEquipments(List<Equipment> equipments) {
    //     this.equipments = equipments;
    // }

    public List<Date> getPossibleStartTimes() {
        return possibleStartTimes;
    }

    public void setPossibleStartTimes(List<Date> possibleStartTimes) {
        this.possibleStartTimes = possibleStartTimes;
    }

    public List<UserRoomHistory> getUserRoomHistory() {
        return userRoomHistory;
    }

    public void setUserRoomHistory(List<UserRoomHistory> userRoomHistory) {
        this.userRoomHistory = userRoomHistory;
    }

    // public List<UserEquipmentHistory> getUserEquipmentHistory() {
    //     return userEquipmentHistory;
    // }

    // public void setUserEquipmentHistory(List<UserEquipmentHistory> userEquipmentHistory) {
    //     this.userEquipmentHistory = userEquipmentHistory;
    // }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public HardSoftScore getScore() {
        return score;
    }

    public void setScore(HardSoftScore score) {
        this.score = score;
    }
     
    public List<Lecture> getLectures() {
        return lectures;
    }

    public void setLectures(List<Lecture> lectures) {
        this.lectures = lectures;
    }
}
