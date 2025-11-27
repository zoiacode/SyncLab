package service.course;

import java.sql.Connection;
import java.util.Date;
import java.util.UUID;

import dao.CourseDao;
import model.Course;

public class UpdateCourseService {
    private final CourseDao courseDao;

    public UpdateCourseService(Connection connection) {
        this.courseDao = new CourseDao(connection);
    }

    public Course execute(
        UUID id,
        String name,
        String acg,
        String schedule
    ) throws Exception {
        try {
        Course course = this.courseDao.getById(id);

        if(course == null) {
            throw new Exception("Curso não cadastrado");
        }

        course.setName(name);
        course.setAcg(acg);
        course.setSchedule(schedule);
        courseDao.save(course);
        return course;
        } catch (Exception e) {
            throw new Exception("Falha ao criar Course: " + e.getMessage());
        }
    }
}