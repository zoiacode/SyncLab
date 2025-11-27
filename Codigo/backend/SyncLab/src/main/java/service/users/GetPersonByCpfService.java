package service.users;
import java.sql.Connection;

import dao.PersonDao;
import model.Person;


public class GetPersonByCpfService {
	private final PersonDao dao;
    public GetPersonByCpfService(Connection connection) {
        this.dao = new PersonDao(connection);
    }
    public Person execute(String cpf) throws Exception {
        try {
            Person personRef = this.dao.getByCpf(cpf);
    
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
