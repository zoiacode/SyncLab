package service.course;

import java.sql.Connection;
import java.util.List;
import java.util.UUID;

import dao.CourseDao;
import model.Course;

public class FetchCourseService {
    private final CourseDao courseDao;

    public FetchCourseService(Connection connection) {
        this.courseDao = new CourseDao(connection);
    }

    public List<Course> execute(
    ) throws Exception {
        try {
            return courseDao.getAll();
        } catch (Exception e) {
            throw new Exception("Falha ao criar Course: " + e.getMessage());
        }
    }
}