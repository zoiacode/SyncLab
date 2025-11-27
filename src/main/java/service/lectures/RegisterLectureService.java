package service.lectures;

import java.sql.Connection;
import java.util.Date;
import java.util.UUID;

import dao.LectureDao;
import dao.ProfessorDao;
import dao.ReservationDao;
import model.Lecture;
import model.Professor;
import service.notification.CreateNotificationService;
import util.valueObject.DateHour;
import util.valueObject.TypeObj;

public class RegisterLectureService {
    final LectureDao lectureDao;
    final ReservationDao reservationDao;
    final ProfessorDao professorDao;
    final Connection connection;

    public RegisterLectureService(Connection connection) {
        this.lectureDao = new LectureDao(connection);
        this.reservationDao = new ReservationDao(connection);
        professorDao = new ProfessorDao(connection);
        this.connection = connection;
    }

    public Lecture execute(
        UUID personId,
        String subjectName, 
        UUID professorId, 
        UUID roomId, 
        String date, 
        int studentQuantity, 
        Date endDate,
        UUID courseId, 
        String lectureType
    ) throws Exception {
        try {
            Professor professor = this.professorDao.getById(professorId);
            if(professor == null) {
                  throw new Exception("Professor não encontrado");
            }

            Lecture lecture = new Lecture(
                subjectName,
                professor.getId(),
                roomId,
                new DateHour(date),
                studentQuantity,
                endDate,
                courseId,
                new TypeObj(lectureType)
            );

            this.lectureDao.create(lecture);

            CreateNotificationService notification = new CreateNotificationService(this.connection);
            String NotificationText = "Nova aula de: " + subjectName; 
            notification.execute(professor.getPersonId(), "Nova aula cadastrada", NotificationText);
            notification.execute(personId, "Nova aula cadastrada", NotificationText);

            return lecture;
        } catch (Exception e) {
            System.err.println("Erro em RegisterLectureService: " + e.getMessage());
            throw e;
        } 
    }
}
