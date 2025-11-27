package service.lectures;

import java.sql.Connection;
import java.util.UUID;

import dao.LectureDao;
import model.Lecture;

public class GetLectureService {

    private final LectureDao lectureDao;

    public GetLectureService(Connection connection) {
        this.lectureDao = new LectureDao(connection);
    }

    public Lecture execute(UUID lectureId) throws Exception {
        try {
            Lecture lecture = lectureDao.getById(lectureId);
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
