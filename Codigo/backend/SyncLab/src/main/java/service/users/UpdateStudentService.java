package service.users;

import java.sql.Connection;
import java.util.UUID;

import dao.StudentDao;
import model.Student;

public class UpdateStudentService {
    private final StudentDao dao;

    public UpdateStudentService(Connection connection) {
        this.dao = new StudentDao(connection);
    }

    public Student execute(
        UUID id,
        String registrationNumber,
        String course,
        String semester,
        String shift,
        String scholarshipType,
        String academicStatus
    ) throws Exception {

        try {
            // Verifica se já existe pessoa com o mesmo CPF
            Student student = dao.getById(id);
            if (student == null) {
                throw new Exception("Usuario não encontrado");
            }  

            student.setRegistrationNumber(registrationNumber);
            student.setCourse(course);
            student.setSemester(semester);
            student.setShift(shift);
            student.setScholarshipType(scholarshipType);
            student.setAcademicStatus(academicStatus);

            // Persiste no banco
            boolean updated = dao.save(student);
            if (!updated) {
                throw new Exception("Falha ao editar estudante no banco.");
            }

            return student;
        } catch (Exception e) {
            System.err.println("Erro em CreateStudentService: " + e.getMessage());
            throw e;
        }
    }
}
