package service.auth;

import java.sql.Connection;
import java.util.UUID;

import com.auth0.jwt.interfaces.DecodedJWT;

import dao.CredentialDao;
import model.Credential;
import util.JwtUtil;

public class AuthenticateService {
    private final CredentialDao credentialDao;

    public AuthenticateService(Connection connection) {
        this.credentialDao = new CredentialDao(connection);
    }

    public Credential execute(String token) throws Exception {
        try {
            DecodedJWT decoded = JwtUtil.validateToken(token);
            UUID personId = UUID.fromString(decoded.getSubject());
            String role = decoded.getClaim("role").asString();

            if (personId == null || role == null) {
                throw new Exception("Token inválido ou expirado");
            }

            Credential credential = credentialDao.getByPersonId(personId);
            if (credential == null) {
                throw new Exception("Usuário não encontrado");
            }

            return credential;
        } catch (Exception e) {
            System.err.println("Erro em AuthenticateService: " + e.getMessage());
            throw e;
        }
    }
}
