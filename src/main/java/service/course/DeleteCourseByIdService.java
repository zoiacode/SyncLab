package service.course;

import java.sql.Connection;
import java.util.List;
import java.util.UUID;

import dao.CourseDao;
import model.Course;

public class DeleteCourseByIdService {
    private final CourseDao courseDao;

    public DeleteCourseByIdService(Connection connection) {
        this.courseDao = new CourseDao(connection);
    }

    public void execute(
        UUID courseId
    ) throws Exception {
        try {
            courseDao.deleteById(courseId);
        } catch (Exception e) {
            throw new Exception("Falha ao criar Course: " + e.getMessage());
        }
    }
}