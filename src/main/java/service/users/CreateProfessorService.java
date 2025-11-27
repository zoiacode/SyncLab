package service.users;

import java.sql.Connection;
import java.util.UUID;

import dao.PersonDao;
import dao.ProfessorDao;
import model.Person;
import model.Professor;

public class CreateProfessorService {
    private final ProfessorDao professorDao;
    private final PersonDao personDao;
    public CreateProfessorService(Connection connection) {
        this.professorDao = new ProfessorDao(connection);
        this.personDao = new PersonDao(connection);
    }

    public Professor execute(
        UUID personId,
        String academicDegree,
        String expertiseArea,
        String employmentStatus
    ) throws Exception {

        try {
            Person existing = this.personDao.getById(personId);
            if (existing == null) {
                throw new Exception("Pessoa não cadastrada");
            }

            Professor professor = new Professor(
                personId,
                academicDegree,
                expertiseArea,
                employmentStatus
            );

            boolean created = professorDao.create(professor);
            if (!created) {
                throw new Exception("Falha ao cadastrar professor no banco.");
            }

            return professor;
        } catch (Exception e) {
            System.err.println("Erro em CreateProfessorService: " + e.getMessage());
            throw e;
        }
    }
}
