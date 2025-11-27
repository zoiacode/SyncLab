package service.lectures;

import java.sql.Connection;
import java.util.List;

import dao.LectureDao;
import model.Lecture;

public class FetchLecturesService {

    private final LectureDao lectureDao;

    public FetchLecturesService(Connection connection) {
        this.lectureDao = new LectureDao(connection);
    }

    public List<Lecture> execute() throws Exception {
        try {
            return lectureDao.getAllFromNow();
        } catch (Exception e) {
            System.err.println("Erro em FetchLecturesService: " + e.getMessage());
            throw e;
        }
    }
}
