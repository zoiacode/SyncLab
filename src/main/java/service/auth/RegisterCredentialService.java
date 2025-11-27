package service.auth;

import java.sql.Connection;
import java.util.Date;
import java.util.UUID;

import at.favre.lib.crypto.bcrypt.BCrypt;
import dao.CredentialDao;
import dao.PersonDao;
import model.Credential;
import model.Person;

public class RegisterCredentialService {

    private final CredentialDao credentialDao;
    private final PersonDao personDao;

    public RegisterCredentialService(Connection connection) {
        this.credentialDao = new CredentialDao(connection);
        this.personDao = new PersonDao(connection);
    }

    public Credential execute(String email, String password, UUID personId) throws Exception {
        try {
            Person person = this.personDao.getById(personId);
            if(person == null) {
                throw new Exception("Erro ao registrar");
            }

            Credential existing = credentialDao.getByEmail(email);
            if (existing != null) {
                throw new Exception("Email já cadastrado");
            }

            String hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());

            String refreshToken = null;
            Date refreshTokenExpiration = null;

            Credential credential = new Credential(
                email,
                hashedPassword,
                personId,
                refreshToken,
                refreshTokenExpiration
            );

            credentialDao.create(credential);

            return credential;
        } catch (Exception e) {
            System.err.println("Erro em RegisterCredentialService: " + e.getMessage());
            throw e;
        }
    }
}
