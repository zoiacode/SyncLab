package service.reservation;

import java.sql.Connection;
import java.util.UUID;

import dao.AdminDao;
import dao.EquipmentDao;
import dao.ReservationDao;
import model.Admin;
import model.Equipment;
import model.Reservation;
import service.notification.CreateNotificationService;

public class RejectEquipmentReservationService {
    ReservationDao reservationDao;
    AdminDao adminDao;
        EquipmentDao equipmentDao;
    Connection connection;

    

    public RejectEquipmentReservationService(Connection connection) {
        this.reservationDao = new ReservationDao(connection);
        this.adminDao = new AdminDao(connection);
        this.equipmentDao = new EquipmentDao(connection);
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
            Equipment equipment = equipmentDao.getById(reservation.getEquipmentId());

            CreateNotificationService notification = new CreateNotificationService(this.connection);
            String NotificationText = "O pedido do item " + equipment.getName() + " foi rejeitado!"; 
            notification.execute(reservation.getPersonId(), "Reserva de equipamento rejeitada!", NotificationText);
            return reservation;
        } catch (Exception e) {
            System.err.println("Erro em RejectEquipmentReservationService: " + e.getMessage());
            throw e;
        }
    }
}
