package service.users;
import java.sql.Connection;
import java.util.UUID;

import dao.ProfessorDao;
import model.Professor;

public class DeleteProfessorByIdService {
	private final ProfessorDao dao;
    public DeleteProfessorByIdService(Connection connection) {
        this.dao = new ProfessorDao(connection);
    }
    public Professor execute(UUID id) throws Exception {
        try {
            Professor professorRef = this.dao.getById(id);
            
            if(professorRef == null) {
                throw new Exception("Professor not found!");
            }

            this.dao.deleteById(id);

            return professorRef;
        } catch (Exception err) {
            System.out.println(err);
            return null;
        }
    }
}
