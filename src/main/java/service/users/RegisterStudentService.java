package service.users;

import java.sql.Connection;

import dao.CredentialDao;
import dao.PersonDao;
import dao.StudentDao;
import model.Credential;
import model.Person;
import model.Student;

public class RegisterStudentService {
    private final StudentDao studentDao;
    private final CredentialDao credentialDao;
    private final PersonDao personDao;
    public RegisterStudentService(Connection connection) {
        this.studentDao = new StudentDao(connection);
        this.credentialDao = new CredentialDao(connection); 
        this.personDao = new PersonDao(connection);
    }

    public Student execute(
        Person person,
        Credential credential,
        Student student
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
            Student existingStudent = this.studentDao.getById(person.getId());
            if (existingStudent != null) {
                throw new Exception("Student já existe");
            }

            this.studentDao.createStudentTransactional(person, credential, student);

            return student;
        } catch (Exception e) {
            System.err.println("Erro em CreateStudentService: " + e.getMessage());
            throw e;
        }
    }
}
