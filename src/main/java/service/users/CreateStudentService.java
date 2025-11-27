package service.users;

import java.sql.Connection;
import java.util.UUID;

import dao.PersonDao;
import dao.StudentDao;
import model.Person;
import model.Student;

public class CreateStudentService {
    private final StudentDao studentDao;
    private final PersonDao personDao;
    public CreateStudentService(Connection connection) {
        this.studentDao = new StudentDao(connection);
        this.personDao = new PersonDao(connection);
    }

    public Student execute(
        UUID personId,
        String registrationNumber,
        String course,
        String semester,
        String shift,
        String scholarshipType,
        String academicStatus
    ) throws Exception {

        try {
            // Verifica se já existe pessoa
            Person existing = this.personDao.getById(personId);
            if (existing == null) {
                throw new Exception("Pessoa não cadastrada");
            }

            // Cria a entidade Student com base nos parâmetros
            Student student = new Student(
                personId,
                registrationNumber,
                course,
                semester,
                shift,
                scholarshipType,
                academicStatus
            );

            // Persiste no banco
            boolean created = studentDao.create(student);
            if (!created) {
                throw new Exception("Falha ao cadastrartrar estudante no banco.");
            }

            return student;
        } catch (Exception e) {
            System.err.println("Erro em CreateStudentService: " + e.getMessage());
            throw e;
        }
    }
}
