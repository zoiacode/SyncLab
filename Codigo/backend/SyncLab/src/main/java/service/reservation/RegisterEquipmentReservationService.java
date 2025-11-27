package service.reservation;

import java.sql.Connection;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import dao.EquipmentDao;
import dao.PersonDao;
import dao.ReservationDao;
import model.Equipment;
import model.Person;
import model.Reservation;

public class RegisterEquipmentReservationService {
    EquipmentDao equipmentDao;
    ReservationDao reservationDao;
    PersonDao personDao;

    public RegisterEquipmentReservationService(Connection connection) {
        this.equipmentDao = new EquipmentDao(connection);
        this.reservationDao = new ReservationDao(connection);
        this.personDao = new PersonDao(connection);
    }

    public Reservation execute(
        UUID personId, 
        UUID equipmentId, 
        String purpose, 
        Date startTime
    ) throws Exception {
        try {
            Person personRef = this.personDao.getById(personId);
            if (personRef == null) {
                throw new Exception("Pessoa inválida");
            }

            Equipment equipmentRef = this.equipmentDao.getById(equipmentId);
            if (equipmentRef == null) {
                throw new Exception("Equipamento não cadastrado");
            }

            // Calcula endTime = startTime + 1h40min
            Instant endInstant = startTime.toInstant()
                    .plus(1, ChronoUnit.HOURS)
                    .plus(40, ChronoUnit.MINUTES);
            Date endTime = Date.from(endInstant);

            List<Reservation> existingReservations = reservationDao.getApprovedByEquipmentAndTime(equipmentId, startTime, endTime);
            if (!existingReservations.isEmpty()) {
                throw new Exception("Equipamento já reservado neste horário!");
            }

            Reservation reservation = new Reservation(
                    personId,
                    equipmentId,
                    null,
                    "Equipment",
                    purpose,
                    startTime,
                    endTime,
                    "Pending"
            );

            boolean created = reservationDao.create(reservation);
            if (!created) {
                throw new Exception("Falha ao cadastrar equipamento no banco.");
            }

            return reservation;
        } catch (Exception e) {
            System.err.println("Erro em RegisterEquipmentReservationService: " + e.getMessage());
            throw e;
        }
    }
}
