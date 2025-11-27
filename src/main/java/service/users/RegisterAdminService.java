package service.users;

import java.sql.Connection;

import dao.AdminDao;
import dao.CredentialDao;
import dao.PersonDao;
import model.Admin;
import model.Credential;
import model.Person;

public class RegisterAdminService {
    private final AdminDao adminDao;
    private final CredentialDao credentialDao;
    private final PersonDao personDao;
    public RegisterAdminService(Connection connection) {
        this.adminDao = new AdminDao(connection);
        this.credentialDao = new CredentialDao(connection); 
        this.personDao = new PersonDao(connection);
    }

    public Admin execute(
        Person person,
        Credential credential,
        Admin admin
    ) throws Exception {

        try {
            // Verifica se já existe pessoa
            Person existingPerson = this.personDao.getById(person.getId());
            if (existingPerson != null) {
                throw new Exception("Pessoa não cadastrada");
            }
            Credential existingCredential = this.credentialDao.getById(credential.getId());
            if (existingCredential != null) {
                throw new Exception("Cadastro já existe");
            }
            Admin existingAdmin = this.adminDao.getById(person.getId());
            if (existingAdmin != null) {
                throw new Exception("Admin já existe");
            }

            this.adminDao.createAdminTransactional(person, credential, admin);

            return admin;
        } catch (Exception e) {
            System.err.println("Erro em CreateAdminService: " + e.getMessage());
            throw e;
        }
    }
}
