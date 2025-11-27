package service.users;
import java.sql.Connection;
import java.util.UUID;

import dao.PersonDao;
import model.Person;


public class GetPersonByIdService {
	private final PersonDao dao;
    public GetPersonByIdService(Connection connection) {
        this.dao = new PersonDao(connection);
    }
    public Person execute(UUID personId) throws Exception {
        try {
            Person personRef = this.dao.getById(personId);
    
            if(personRef == null) {
                throw new Exception("Person not found!");
            }

            return personRef;
        } catch (Exception err) {
            System.out.println(err);
            return null;
        }
    }
}
