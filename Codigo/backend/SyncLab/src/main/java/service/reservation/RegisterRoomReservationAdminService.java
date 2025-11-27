package service.reservation;

import java.sql.Connection;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import dao.AdminDao;
import dao.ReservationDao;
import dao.RoomDao;
import model.Admin;
import model.Reservation;
import model.Room;

public class RegisterRoomReservationAdminService {
    RoomDao roomDao;
    ReservationDao reservationDao;
    AdminDao adminDao;

    public RegisterRoomReservationAdminService(Connection connection) {
        this.roomDao = new RoomDao(connection);
        this.reservationDao = new ReservationDao(connection);
        this.adminDao = new AdminDao(connection);
    }

    public Reservation execute(
        UUID adminId,
        UUID roomId, 
        String purpose, 
        Date startTime,
        UUID courseId
    ) throws Exception {
        try {
            Admin adminRef = this.adminDao.getByPersonId(adminId);
            if (adminRef == null) {
                throw new Exception("Admin não existe");
            }

            Room roomRef = this.roomDao.getById(roomId);
            if (roomRef == null) {
                throw new Exception("Sala não cadastrado");
            }

            Instant endInstant = startTime.toInstant()
                    .plus(1, ChronoUnit.HOURS)
                    .plus(40, ChronoUnit.MINUTES);
            Date endTime = Date.from(endInstant);

            List<Reservation> existingReservations = reservationDao.getApprovedByRoomAndTime(roomId, startTime, endTime);
            if (!existingReservations.isEmpty()) {
                throw new Exception("Sala já reservado neste horário!");
            }

            Reservation reservation = new Reservation(
                    adminRef.getPersonId(),
                    null,
                    roomId,
                    "Room",
                    purpose,
                    startTime,
                    endTime,
                    "Approved"
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
