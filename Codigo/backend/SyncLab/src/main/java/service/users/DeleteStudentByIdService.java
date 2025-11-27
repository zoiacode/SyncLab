package service.users;
import java.sql.Connection;
import java.util.UUID;

import dao.StudentDao;
import model.Student;

public class DeleteStudentByIdService {
	private final StudentDao dao;
    public DeleteStudentByIdService(Connection connection) {
        this.dao = new StudentDao(connection);
    }
    public Student execute(UUID id) throws Exception {
        try {
            Student studentRef = this.dao.getById(id);
            
            if(studentRef == null) {
                throw new Exception("Student not found!");
            }

            this.dao.deleteById(id);

            return studentRef;
        } catch (Exception err) {
            System.out.println(err);
            return null;
        }
    }
}
