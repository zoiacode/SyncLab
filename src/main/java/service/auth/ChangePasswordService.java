package service.auth;

import java.sql.Connection;

import at.favre.lib.crypto.bcrypt.BCrypt;
import dao.CredentialDao;
import model.Credential;

public class ChangePasswordService {

    private final CredentialDao credentialDao;

    public ChangePasswordService(Connection connection) {
        this.credentialDao = new CredentialDao(connection);
    }

    public void execute(String email, String oldPassword, String newPassword) throws Exception {
        try {
            Credential credential = credentialDao.getByEmail(email);
            if (credential == null) {
                throw new Exception("Usuário não encontrado");
            }

            BCrypt.Result result = BCrypt.verifyer().verify(oldPassword.toCharArray(), credential.getPassword());
            if (!result.verified) {
                throw new Exception("Senha atual incorreta");
            }

            String hashedNewPassword = BCrypt.withDefaults().hashToString(12, newPassword.toCharArray());
            credential.setPassword(hashedNewPassword);

            credentialDao.save(credential);

        } catch (Exception e) {
            System.err.println("Erro em ChangePasswordService: " + e.getMessage());
            throw e;
        }
    }
}
