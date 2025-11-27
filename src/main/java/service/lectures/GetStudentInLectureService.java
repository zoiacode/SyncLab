package service.lectures;

import java.sql.Connection;
import java.util.List;
import java.util.UUID;

import dao.LectureDao;
import util.StudentInClass;

public class GetStudentInLectureService {

    private final LectureDao lectureDao;

    public GetStudentInLectureService(Connection connection) {
        this.lectureDao = new LectureDao(connection);
    }

    public List<StudentInClass> execute(UUID classId) throws Exception {
        try {
            return lectureDao.getPersonsInClass(classId);
        } catch (Exception e) {
            System.err.println("Erro em FetchLecturesService: " + e.getMessage());
            throw e;
        }
    }
}
