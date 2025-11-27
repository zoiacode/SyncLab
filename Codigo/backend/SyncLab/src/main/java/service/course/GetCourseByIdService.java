package service.course;

import java.sql.Connection;
import java.util.UUID;

import dao.CourseDao;
import model.Course;

public class GetCourseByIdService {
    private final CourseDao courseDao;

    public GetCourseByIdService(Connection connection) {
        this.courseDao = new CourseDao(connection);
    }

    public Course execute(
        UUID courseId
    ) throws Exception {
        try {
            return courseDao.getById(courseId);
        } catch (Exception e) {
            throw new Exception("Falha ao criar Course: " + e.getMessage());
        }
    }
}