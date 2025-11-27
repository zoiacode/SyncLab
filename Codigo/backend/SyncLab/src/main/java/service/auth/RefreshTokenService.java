package service.auth;

import java.sql.Connection;
import java.util.UUID;

import dao.CredentialDao;
import model.Credential;
import util.JwtUtil;

public class RefreshTokenService {
    private final CredentialDao credentialDao;

    public RefreshTokenService(Connection connection) {
        this.credentialDao = new CredentialDao(connection);
    }

    public String execute(String refreshToken) throws Exception {
        try {
            Credential credential = credentialDao.getByRefreshToken(refreshToken);

            if (credential == null) {
                throw new Exception("Refresh token inválido");
            }

            if (credential.getRefreshTokenExpiration() != null &&
                credential.getRefreshTokenExpiration().before(new java.util.Date())) {
                throw new Exception("Refresh token expirado");
            }

            UUID personId = JwtUtil.extractPersonId(refreshToken);
            String role = JwtUtil.extractRole
            (refreshToken);

            String newAccessToken = JwtUtil.generateAccessToken(personId, role);
            
            return newAccessToken;

        } catch (Exception e) {
            System.err.println("Erro em RefreshTokenService: " + e.getMessage());
            throw e;
        }
    }
}
