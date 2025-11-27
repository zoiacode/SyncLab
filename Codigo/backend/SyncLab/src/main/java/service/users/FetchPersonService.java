package service.users;
import java.sql.Connection;
import java.util.List;

import dao.PersonDao;
import model.Person;


public class FetchPersonService {
	private final PersonDao dao;
    public FetchPersonService(Connection connection) {
        this.dao = new PersonDao(connection);
    }
    public List<Person> execute() throws Exception {
        try {
            List<Person> personArr = this.dao.getAll();

            return personArr;
        } catch (Exception err) {
            System.out.println(err);
            return null;
        }
    }
}
