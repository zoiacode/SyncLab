package service.lectures;

import java.sql.Connection;
import java.util.UUID;

import dao.LectureDao;
import dao.ProfessorDao;
import dao.ReservationDao;
import model.Lecture;
import model.Professor;
import util.valueObject.DateHour;

public class UpdateLectureService {

    private final LectureDao lectureDao;
    private final ProfessorDao professorDao;
    private final ReservationDao reservationDao;

    public UpdateLectureService(Connection connection) {
        this.lectureDao = new LectureDao(connection);
        this.professorDao = new ProfessorDao(connection);
        this.reservationDao = new ReservationDao(connection);
    }

    public Lecture execute(
        UUID lectureId,
        String newSubjectName,
        UUID newProfessorId,
        UUID currentUserId,
        UUID roomId,
        String date,
        boolean isAdmin
    ) throws Exception {
        try {
            Lecture lecture = lectureDao.getById(lectureId);
            if (lecture == null) throw new Exception("Aula não encontrada");

            if (!isAdmin && !lecture.getProfessorId().equals(currentUserId)) {
                throw new Exception("Não permitido editar esta aula");
            }

            Professor professor = professorDao.getById(newProfessorId);
            if (professor == null) throw new Exception("Professor não encontrado");


            lecture.setSubjectName(newSubjectName);
            lecture.setProfessorId(professor.getId());
            lecture.setRoomId(roomId);
            lecture.setDate(new DateHour(date));

            lectureDao.save(lecture);

            return lecture;
        } catch (Exception e) {
            System.err.println("Erro em UpdateLectureService: " + e.getMessage());
            throw e;
        }
    }
}
