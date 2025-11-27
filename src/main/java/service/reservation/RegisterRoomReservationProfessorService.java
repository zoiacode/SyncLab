package service.reservation;

import java.sql.Connection;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import dao.ProfessorDao;
import dao.ReservationDao;
import dao.RoomDao;
import model.Professor;
import model.Reservation;
import model.Room;

public class RegisterRoomReservationProfessorService {
    RoomDao roomDao;
    ReservationDao reservationDao;
    ProfessorDao professorDao;

    public RegisterRoomReservationProfessorService(Connection connection) {
        this.roomDao = new RoomDao(connection);
        this.reservationDao = new ReservationDao(connection);
        this.professorDao = new ProfessorDao(connection);
    }

    public Reservation execute(
        UUID professorId,
        UUID roomId, 
        String purpose, 
        Date startTime,
        UUID courseId
    ) throws Exception {
        try {
            Professor professorRef = this.professorDao.getByPersonId(professorId);
            if (professorRef == null) {
                throw new Exception("Professor não existe");
            }

            Room roomRef = this.roomDao.getById(roomId);
            if (roomRef == null) {
                throw new Exception("Sala não cadastrado");
            }

            // Calcula endTime = startTime + 1h40min
            Instant endInstant = startTime.toInstant()
                    .plus(1, ChronoUnit.HOURS)
                    .plus(40, ChronoUnit.MINUTES);
            Date endTime = Date.from(endInstant);

            List<Reservation> existingReservations = reservationDao.getApprovedByRoomAndTime(roomId, startTime, endTime);
            if (!existingReservations.isEmpty()) {
                throw new Exception("Sala já reservado neste horário!");
            }

            Reservation reservation = new Reservation(
                    professorRef.getPersonId(),
                    null,
                    roomId,
                    "Room",
                    purpose,
                    startTime,
                    endTime,
                    "Pending"
            );

            reservation.setCourseId(courseId);

            boolean created = reservationDao.create(reservation);
            if (!created) {
                throw new Exception("Falha ao cadastrar equipamento no banco.");
            }

            return reservation;
        } catch (Exception e) {
            System.err.println("Erro em RegisterRoomReservationService: " + e.getMessage());
            throw e;
        }
    }
}
