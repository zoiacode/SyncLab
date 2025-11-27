package service.lectures;

import java.sql.Connection;
import java.util.List;
import java.util.UUID;

import dao.LectureDao;
import model.Lecture;

public class GetLectureServiceByProfessorId {

    private final LectureDao lectureDao;

    public GetLectureServiceByProfessorId(Connection connection) {
        this.lectureDao = new LectureDao(connection);
    }

    public List<Lecture> execute(UUID professorId) throws Exception {
        try {
            List<Lecture> lecture = lectureDao.getAllByProfessorId(professorId);
            if (lecture == null) {
                throw new Exception("Aula não encontrada");
            }
            return lecture;
        } catch (Exception e) {
            System.err.println("Erro em GetLectureService: " + e.getMessage());
            throw e;
        }
    }
}
