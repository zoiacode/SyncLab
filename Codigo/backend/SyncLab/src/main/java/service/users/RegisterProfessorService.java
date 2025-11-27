package service.users;

import java.sql.Connection;

import dao.CredentialDao;
import dao.PersonDao;
import dao.ProfessorDao;
import model.Credential;
import model.Person;
import model.Professor;

public class RegisterProfessorService {
    private final ProfessorDao professorDao;
    private final CredentialDao credentialDao;
    private final PersonDao personDao;
    public RegisterProfessorService(Connection connection) {
        this.professorDao = new ProfessorDao(connection);
        this.credentialDao = new CredentialDao(connection); 
        this.personDao = new PersonDao(connection);
    }

    public Professor execute(
        Person person,
        Credential credential,
        Professor professor
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
            Professor existingProfessor = this.professorDao.getById(person.getId());
            if (existingProfessor != null) {
                throw new Exception("Professor já existe");
            }

            this.professorDao.createProfessorTransactional(person, credential, professor);

            return professor;
        } catch (Exception e) {
            System.err.println("Erro em CreateProfessorService: " + e.getMessage());
            throw e;
        }
    }
}
