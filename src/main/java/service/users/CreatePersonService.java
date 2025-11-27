package service.users;

import java.sql.Connection;
import java.util.Date;

import dao.PersonDao;
import model.Person;

public class CreatePersonService {
    private final PersonDao dao;

    public CreatePersonService(Connection connection) {
        this.dao = new PersonDao(connection);
    }

    public Person execute(
        String name,
        String phoneNumber,
        String cpf,
        Date birthDate,
        String profileUrl,
        String description,
        Date desactivatedAt,
        String personCode,
        String role
    ) throws Exception {

        try {
            // Verifica se já existe pessoa com o mesmo CPF
            Person existing = dao.getByCpf(cpf);
            if (existing != null) {
                throw new Exception("CPF já cadastrado!");
            }

            // Cria a entidade Person com base nos parâmetros
            Person person = new Person(
                name,
                phoneNumber,
                cpf,
                birthDate,
                profileUrl,
                description,
                desactivatedAt,
                personCode,
                role
            );

            // Persiste no banco
            boolean created = dao.create(person);
            if (!created) {
                throw new Exception("Falha ao cadastrar pessoa no banco.");
            }

            return person;
        } catch (Exception e) {
            System.err.println("Erro em CreatePersonService: " + e.getMessage());
            throw e;
        }
    }
}
