package service.inteligentSystem;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;

import dao.EquipmentDao;
import dao.LectureDao;
import dao.ProfessorDao;
import dao.ReservationDao;
import dao.RoomDao;
import model.Lecture;
import model.Professor;
import model.RecommendationResult;
import model.Reservation;
import model.ReservationSchedule;
import model.Room;
import model.UserRoomHistory;

public class ReservationSolverService {

    private final SolverFactory<ReservationSchedule> solverFactory;
    private final RoomDao roomDao;
    private final EquipmentDao equipmentDao;
    private final ReservationDao reservationDao;
    private final ProfessorDao professorDao;
    private final LectureDao lectureDao;

    public ReservationSolverService(Connection connection) {
        this.solverFactory = SolverFactory.createFromXmlResource("solverConfig.xml");
        this.lectureDao = new LectureDao(connection);
        this.roomDao = new RoomDao(connection);
        this.equipmentDao = new EquipmentDao(connection);
        this.reservationDao = new ReservationDao(connection);
        this.professorDao = new ProfessorDao(connection);
    }

    public RecommendationResult execute(UUID currentUserId, String role) throws Exception {
        // Buscar dados do DB
        List<Room> rooms = roomDao.getAll();
        List<Date> times = reservationDao.getPossibleStartTimes();
        List<UserRoomHistory> roomHistory = reservationDao.getUserRoomHistory(currentUserId);
        Professor professor = null;
        List<Lecture> lectures = null;
        
        if(role == "PROFESSOR") {
            professor = professorDao.getByPersonId(currentUserId);
           lectures =  lectureDao.getAllByProfessorId(professor.getId());
        }
        List<Reservation> allReservations = reservationDao.getAllByPersonId(currentUserId);

        Map<UUID, Room> roomMap = new HashMap<>();
        for (Room room : rooms) roomMap.put(room.getId(), room);


        for (Reservation r : allReservations) {
            if (r.getRoomId() != null && roomMap.containsKey(r.getRoomId())) {
                r.setRoom(roomMap.get(r.getRoomId()));
            }
        }

        if(lectures != null && !lectures.isEmpty()) {
            for(Lecture l : lectures) {
                if(l.getRoomId() != null && roomMap.containsKey(l.getRoomId())) {
                    l.setRoom(roomMap.get(l.getRoomId()));
                }
            }
        }

        List<Room> roomRecommendations = solveRooms(currentUserId, rooms, times, roomHistory, allReservations, lectures);

        return new RecommendationResult(roomRecommendations);
    }

    private List<Room> solveRooms(UUID userId, List<Room> rooms, List<Date> times,
        List<UserRoomHistory> history, List<Reservation> reservations, List<Lecture> lectures) {
        if (rooms.isEmpty()) return new ArrayList<>();
        if (times.isEmpty()) times = List.of(new Date());
        if (lectures == null || lectures.isEmpty()) lectures = List.of(new Lecture(null, null, null,null,0, null, null, null));

        ReservationSchedule schedule = new ReservationSchedule(rooms, times, reservations, history, lectures);
        Solver<ReservationSchedule> solver = solverFactory.buildSolver();
        ReservationSchedule solved = solver.solve(schedule);

        return solved.getRooms();
    }
}
