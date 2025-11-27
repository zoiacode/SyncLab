package service.users;

import java.sql.Connection;
import java.util.UUID;

import dao.PersonDao;
import model.Person;

public class UpdatePersonService {
    private final PersonDao dao;

    public UpdatePersonService(Connection connection) {
        this.dao = new PersonDao(connection);
    }

    public Person execute(
        UUID id,
        String name,
        String profileUrl,
        String description,
        String personCode
    ) throws Exception {

        try {
            // Verifica se já existe pessoa com o mesmo CPF
            Person person = dao.getById(id);
            if (person == null) {
                throw new Exception("Usuario não encontrado");
            }  

            person.setName(name);
            person.setProfileUrl(profileUrl);
            person.setDescription(description);
            person.setPersonCode(personCode);

            // Persiste no banco
            boolean updated = dao.save(person);
            if (!updated) {
                throw new Exception("Falha ao cadastrar pessoa no banco.");
            }

            return person;
        } catch (Exception e) {
            System.err.println("Erro em CreatePersonService: " + e.getMessage());
            throw e;
        }
    }
}
