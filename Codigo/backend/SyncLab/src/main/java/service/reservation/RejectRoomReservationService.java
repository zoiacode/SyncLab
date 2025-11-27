package service.reservation;

import java.sql.Connection;
import java.util.UUID;

import dao.AdminDao;
import dao.ReservationDao;
import dao.RoomDao;
import model.Admin;
import model.Reservation;
import model.Room;
import service.notification.CreateNotificationService;

public class RejectRoomReservationService {
     ReservationDao reservationDao;
    AdminDao adminDao;
    RoomDao roomDao;
    Connection connection;

    public RejectRoomReservationService(Connection connection) {
      this.reservationDao = new ReservationDao(connection);
        this.adminDao = new AdminDao(connection);
        this.roomDao = new RoomDao(connection);
        this.connection = connection;
    }

    public Reservation execute(
        UUID adminId,
        UUID reservationId
    ) throws Exception {
        try {
            Admin adminRef = this.adminDao.getByPersonId(adminId);
            if (adminRef == null) {
                throw new Exception("Admin não existe");
            }

            Reservation reservation = this.reservationDao.getById(reservationId);
            if (reservation == null) {
                throw new Exception("Reserva não cadastrada");
            }

            reservation.setStatus("Rejected");

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
            System.err.println("Erro em RejectRoomReservationService: " + e.getMessage());
            throw e;
        }
    }
}
