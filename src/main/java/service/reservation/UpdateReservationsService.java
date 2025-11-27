package service.reservation;

import java.sql.Connection;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

import dao.AdminDao;
import dao.ReservationDao;
import dao.RoomDao;
import model.Reservation;
import model.Room;
import service.notification.CreateNotificationService;

public class UpdateReservationsService {
     ReservationDao reservationDao;
    AdminDao adminDao;
    RoomDao roomDao;
    Connection connection;

    public UpdateReservationsService(Connection connection) {
      this.reservationDao = new ReservationDao(connection);
        this.adminDao = new AdminDao(connection);
        this.roomDao = new RoomDao(connection);
        this.connection = connection;
    }

    public Reservation execute(
        UUID id,
        String purpose

    ) throws Exception {
        try {
            Reservation reservation = reservationDao.getById(id);

            reservation.setPurpose(purpose);

            Instant endInstant = reservation.getStartTime().toInstant().plus(1, ChronoUnit.HOURS).plus(40, ChronoUnit.MINUTES);
            Date endTime = Date.from(endInstant);
            reservation.setEndTime(endTime);

            boolean created = reservationDao.save(reservation);
            if (!created) {
                throw new Exception("Falha ao atualizar equipamento no banco.");
            }

             Room room = this.roomDao.getById(reservation.getRoomId());

            CreateNotificationService notification = new CreateNotificationService(connection);

            String NotificationText = "O pedido da sala " + room.getCode() + " foi rejeitado!";
            notification.execute(reservation.getPersonId(), "Reserva da sala rejeitada!", NotificationText);


            return reservation;
        } catch (Exception e) {
            System.err.println("Erro em UpdateReservationsService: " + e.getMessage());
            throw e;
        }
    }
}
