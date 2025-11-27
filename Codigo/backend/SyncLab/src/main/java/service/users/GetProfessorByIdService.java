package service.users;
import java.sql.Connection;
import java.util.UUID;

import dao.ProfessorDao;
import model.Professor;


public class GetProfessorByIdService {
	private final ProfessorDao dao;
    public GetProfessorByIdService(Connection connection) {
        this.dao = new ProfessorDao(connection);
    }
    public Professor execute(UUID professorId) throws Exception {
        try {
            Professor professorRef = this.dao.getById(professorId);
    
            if(professorRef == null) {
                throw new Exception("Professor not found!");
            }

            return professorRef;
        } catch (Exception err) {
            System.out.println(err);
            return null;
        }
    }
}
