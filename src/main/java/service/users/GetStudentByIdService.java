package service.users;
import java.sql.Connection;
import java.util.UUID;

import dao.StudentDao;
import model.Student;


public class GetStudentByIdService {
	private final StudentDao dao;
    public GetStudentByIdService(Connection connection) {
        this.dao = new StudentDao(connection);
    }
    public Student execute(UUID studentId) throws Exception {
        try {
            Student studentRef = this.dao.getById(studentId);
    
            if(studentRef == null) {
                throw new Exception("Student not found!");
            }

            return studentRef;
        } catch (Exception err) {
            System.out.println(err);
            return null;
        }
    }
}
