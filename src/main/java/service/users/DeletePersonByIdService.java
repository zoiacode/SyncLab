package service.users;
import java.sql.Connection;
import java.util.UUID;

import dao.PersonDao;
import model.Person;

public class DeletePersonByIdService {
	private final PersonDao dao;
    public DeletePersonByIdService(Connection connection) {
        this.dao = new PersonDao(connection);
    }
    public Person execute(UUID id) throws Exception {
        try {
            Person personRef = this.dao.getById(id);
            
            if(personRef == null) {
                throw new Exception("Person not found!");
            }

            this.dao.deleteById(id);

            return personRef;
        } catch (Exception err) {
            System.out.println(err);
            return null;
        }
    }
}
