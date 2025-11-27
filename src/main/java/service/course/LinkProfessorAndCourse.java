package service.course;

import java.sql.Connection;
import java.util.List;
import java.util.UUID;

import dao.CourseDao;
import dao.ProfessorDao;
import model.Professor;

public class LinkProfessorAndCourse {
    private final CourseDao courseDao;
    private final ProfessorDao professorDao;

    public LinkProfessorAndCourse(Connection connection) {
        this.courseDao = new CourseDao(connection);
        this.professorDao = new ProfessorDao(connection);
    }

    public void execute(
        UUID professorId,
        List<UUID> coursesId
    ) throws Exception {
        try {
            Professor professor = professorDao.getByPersonId(professorId);
            if(professor == null) {
                    throw new Exception("Professor não cadastrado");
            }
            
            courseDao.linkCoursesAndProfessor(professor.getId(), coursesId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Falha ao criar Course: " + e.getMessage());
        }
    }
}