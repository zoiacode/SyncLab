package service.reservation;

import java.sql.Connection;
import java.util.UUID;

import dao.AdminDao;
import dao.EquipmentDao;
import dao.ProfessorDao;
import dao.ReservationDao;
import dao.RoomDao;
import model.Admin;
import model.Equipment;
import model.Professor;
import model.Reservation;
import model.Room;
import service.lectures.RegisterLectureService;
import service.notification.CreateNotificationService;
import util.valueObject.DateHour;
import util.valueObject.TypeObj;

public class ApproveReservationService {
    ReservationDao reservationDao;
    EquipmentDao equipmentDao;
    RoomDao roomDao;
    AdminDao adminDao;
    ProfessorDao professorDao;
    Connection connection;

    public ApproveReservationService(Connection connection) {
        this.reservationDao = new ReservationDao(connection);
        this.adminDao = new AdminDao(connection);
        this.equipmentDao = new EquipmentDao(connection);
        this.roomDao = new RoomDao(connection); 
        this.professorDao = new ProfessorDao(connection);
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
            
            if (reservation.getStatus().equals("Approved")) {
                throw new Exception("A reserva já está aprovada.");
            }

            reservation.setStatus("Approved");

            boolean updated = reservationDao.save(reservation);
            if (!updated) {
                throw new Exception("Falha ao atualizar o status da reserva no banco.");
            }

            CreateNotificationService notification = new CreateNotificationService(this.connection);
            String notificationText;
            String notificationTitle;
            
            if (reservation.getRoomId() != null) {
                Room room = this.roomDao.getById(reservation.getRoomId());
                if (room == null) {
                    throw new Exception("Sala referenciada na reserva não encontrada.");
                }

                Professor professor = this.professorDao.getByPersonId(reservation.getPersonId());

                if(professor == null) {
                    throw new Exception("O usuário que fez a reserva não é um professor!");
                }

                if(reservation.getCourseId() != null) {
                    RegisterLectureService lectureService = new RegisterLectureService(connection);
    
                    lectureService.execute(adminId, reservation.getPurpose(), professor.getId(), room.getId(), DateHour.fromDate(reservation.getStartTime()).getValue(), room.getCapacity(), reservation.getEndTime(), reservation.getCourseId(), new TypeObj(room.getRoomType()).getValue());
                }


                notificationText = "O pedido da sala " + room.getCode() + " foi aprovado! Uma aula foi registrada.";
                notificationTitle = "Reserva da sala aprovada!";
            
            } else if (reservation.getEquipmentId() != null) {
                Equipment equipment = equipmentDao.getById(reservation.getEquipmentId());
                if (equipment == null) {
                    throw new Exception("Equipamento referenciado na reserva não encontrado.");
                }

                notificationText = "O pedido do item " + equipment.getName() + " foi aprovado!"; 
                notificationTitle = "Reserva de equipamento aprovada!";

            } else {
                throw new Exception("Reserva é inválida: não está associada a Sala nem a Equipamento.");
            }
            
            notification.execute(reservation.getPersonId(), notificationTitle, notificationText);

            return reservation;
            
        } catch (Exception e) {
            System.err.println("Erro em ApproveReservationService: " + e.getMessage());
            throw e;
        }
    }
}