package service.users;

import java.sql.Connection;
import java.util.UUID;

import dao.AdminDao;
import dao.PersonDao;
import model.Admin;
import model.Person;

public class CreateAdminService {
    private final AdminDao adminDao;
    private final PersonDao personDao;
    public CreateAdminService(Connection connection) {
        this.adminDao = new AdminDao(connection);
        this.personDao = new PersonDao(connection);
    }

    public Admin execute(
        UUID personId, 
        String jobTitle
    ) throws Exception {

        try {
            // Verifica se já existe pessoa
            Person existing = this.personDao.getById(personId);
            if (existing == null) {
                throw new Exception("Pessoa não cadastrada");
            }

            // Cria a entidade Admin com base nos parâmetros
            Admin admin = new Admin(
                personId, 
                jobTitle 
            );

            // Persiste no banco
            boolean created = adminDao.create(admin);
            if (!created) {
                throw new Exception("Falha ao cadastrartrar administrador no banco.");
            }

            return admin;
        } catch (Exception e) {
            System.err.println("Erro em CreateAdminService: " + e.getMessage());
            throw e;
        }
    }
}
