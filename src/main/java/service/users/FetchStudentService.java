package service.users;
import java.sql.Connection;
import java.util.List;

import dao.StudentDao;
import model.Person;
import model.Student;


public class FetchStudentService {
	private final StudentDao dao;
    public FetchStudentService(Connection connection) {
        this.dao = new StudentDao(connection);
    }
    public List<Student> execute() throws Exception {
        try {
            List<Student> studentArr = this.dao.getAll();
            return studentArr;
        } catch (Exception err) {
            System.out.println(err);
            return null;
        }
    }
}
