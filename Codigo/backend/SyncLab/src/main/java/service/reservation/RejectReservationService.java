package service.reservation;

import java.sql.Connection;
import java.util.UUID;

import dao.AdminDao;
import dao.EquipmentDao;
import dao.ReservationDao;
import dao.RoomDao;
import model.Admin;
import model.Equipment;
import model.Reservation;
import model.Room;
import service.notification.CreateNotificationService;

public class RejectReservationService {
    ReservationDao reservationDao;
    AdminDao adminDao;
    EquipmentDao equipmentDao;
    RoomDao roomDao;
    Connection connection;

    public RejectReservationService(Connection connection) {
        this.reservationDao = new ReservationDao(connection);
        this.adminDao = new AdminDao(connection);
        this.equipmentDao = new EquipmentDao(connection);
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

            boolean updated = reservationDao.save(reservation);
            if (!updated) {
                throw new Exception("Falha ao atualizar reserva no banco.");
            }

            CreateNotificationService notification = new CreateNotificationService(this.connection);
            String notificationText;
            String notificationTitle;

            if (reservation.getEquipmentId() != null) {
                Equipment equipment = equipmentDao.getById(reservation.getEquipmentId());
                if (equipment == null) {
                    throw new Exception("Equipamento referenciado na reserva não encontrado.");
                }
                notificationText = "O pedido do item " + equipment.getName() + " foi rejeitado!";
                notificationTitle = "Reserva de equipamento rejeitada!";
            } else if (reservation.getRoomId() != null) {
                Room room = this.roomDao.getById(reservation.getRoomId());
                if (room == null) {
                    throw new Exception("Sala referenciada na reserva não encontrada.");
                }
                notificationText = "O pedido da sala " + room.getCode() + " foi rejeitado!";
                notificationTitle = "Reserva da sala rejeitada!";
            } else {
                throw new Exception("Reserva é inválida: não está associada a Sala nem a Equipamento.");
            }
            
            notification.execute(reservation.getPersonId(), notificationTitle, notificationText);

            return reservation;
        } catch (Exception e) {
            System.err.println("Erro em RejectReservationService: " + e.getMessage());
            throw e;
        }
    }
}