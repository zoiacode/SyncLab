package service.users;

import java.sql.Connection;
import java.util.UUID;

import dao.ProfessorDao;
import model.Professor;

public class UpdateProfessorService {
    private final ProfessorDao dao;

    public UpdateProfessorService(Connection connection) {
        this.dao = new ProfessorDao(connection);
    }

    public Professor execute(
        UUID id,
        String academicDegree,
        String expertiseArea,
        String employmentStatus
    ) throws Exception {

        try {
            Professor professor = dao.getById(id);
            if (professor == null) {
                throw new Exception("Usuario não encontrado");
            }  
            
            professor.setAcademicDegree(academicDegree);
            professor.setExpertiseArea(expertiseArea);
            professor.setEmploymentStatus(employmentStatus);

            // Persiste no banco
            boolean updated = dao.save(professor);
            if (!updated) {
                throw new Exception("Falha ao editar estudante no banco.");
            }

            return professor;
        } catch (Exception e) {
            System.err.println("Erro em CreateProfessorService: " + e.getMessage());
            throw e;
        }
    }
}
