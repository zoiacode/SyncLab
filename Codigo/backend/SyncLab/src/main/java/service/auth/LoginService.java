package service.auth;

import java.sql.Connection;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import at.favre.lib.crypto.bcrypt.BCrypt;
import dao.CredentialDao;
import dao.PersonDao;
import model.Credential;
import model.Person;
import util.JwtUtil;

public class LoginService {
    private final CredentialDao credentialDao;
    private final PersonDao personDao;

    public LoginService(Connection connection) {
        this.credentialDao = new CredentialDao(connection);
        this.personDao = new PersonDao(connection);
    }

    public AuthResponse execute(String email, String password) throws Exception {
        try {
            Credential credential = credentialDao.getByEmail(email);
            if (credential == null) {
                throw new Exception("Crendencial não encontrado");
            }

            Person person = personDao.getById(credential.getPersonId());

            if (person == null) {
                throw new Exception("Usuário não encontrado");
            }
            BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), credential.getPassword());
            if (!result.verified) {
                throw new Exception("Senha incorreta");
            }

            String accessToken = JwtUtil.generateAccessToken(credential.getPersonId(), person.getRole());
            String refreshToken = JwtUtil.generateRefreshToken(credential.getPersonId(), person.getRole());

            Instant refreshExp = Instant.now().plus(7, ChronoUnit.DAYS);
            Timestamp expDate = Timestamp.from(refreshExp);
            credentialDao.updateRefreshToken(credential.getId(), refreshToken, expDate);

            return new AuthResponse(accessToken, refreshToken);
        } catch (Exception e) {
            System.err.println("Erro em LoginService: " + e.getMessage());
            throw e;
        }
    }

    public static class AuthResponse {
        private final String accessToken;
        private final String refreshToken;

        public AuthResponse(String accessToken, String refreshToken) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }

        public String getAccessToken() { return accessToken; }
        public String getRefreshToken() { return refreshToken; }
    }
}
