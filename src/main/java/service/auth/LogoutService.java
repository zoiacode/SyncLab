package service.auth;

import java.sql.Connection;
import java.util.UUID;

import dao.CredentialDao;
import model.Credential;

public class LogoutService {
    private final CredentialDao credentialDao;

    public LogoutService(Connection connection) {
        this.credentialDao = new CredentialDao(connection);
    }

    public void execute(UUID personId) throws Exception {
        try {
            Credential crendetial = credentialDao.getByPersonId(personId);
            credentialDao.updateRefreshToken(crendetial.getId(), null, null);
        } catch (Exception e) {
            System.err.println("Erro em LogoutService: " + e.getMessage());
            throw e;
        }
    }
}
