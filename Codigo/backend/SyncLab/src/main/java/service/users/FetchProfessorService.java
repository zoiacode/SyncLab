package service.users;
import java.sql.Connection;
import java.util.List;

import dao.ProfessorDao;
import model.Professor;


public class FetchProfessorService {
	private final ProfessorDao dao;
    public FetchProfessorService(Connection connection) {
        this.dao = new ProfessorDao(connection);
    }
    public List<Professor> execute() throws Exception {
        try {
            List<Professor> professorArr = this.dao.getAll();
            return professorArr;
        } catch (Exception err) {
            System.out.println(err);
            return null;
        }
    }
}
