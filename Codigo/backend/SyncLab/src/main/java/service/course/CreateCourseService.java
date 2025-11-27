package service.course;

import java.sql.Connection;
import java.util.Date;
import java.util.UUID;

import dao.CourseDao;
import model.Course;

public class CreateCourseService {
    private final CourseDao courseDao;

    public CreateCourseService(Connection connection) {
        this.courseDao = new CourseDao(connection);
    }

    public Course execute(
        String name,
        String acg,
        String schedule
    ) throws Exception {
        Course course = new Course(
            acg,
            schedule,
            name
        );

        try {
            courseDao.create(course);
            return course;
        } catch (Exception e) {
            throw new Exception("Falha ao criar Course: " + e.getMessage());
        }
    }
}