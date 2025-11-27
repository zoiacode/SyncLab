package util;

import java.util.Date;
import java.util.UUID;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JwtUtil {
    private static final String SECRET = "chave-muito-segura-e-grande";
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET);

    public static String generateAccessToken(UUID personId, String role) {
        int expirationMillis = 1000 * 60 * 60 * 24;
        return JWT.create()
                .withSubject(personId.toString())
                .withClaim("role", role)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationMillis))
                .sign(ALGORITHM);
    }

    public static String generateRefreshToken(UUID personId, String role) {
        int expirationMillis = 1000 * 60 * 60 * 24 * 7;
        return JWT.create()
                .withSubject(personId.toString())
                .withClaim("role", role)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationMillis))
                .sign(ALGORITHM);
    }

    public static DecodedJWT validateToken(String token) throws Exception {
        try {
            return JWT.require(ALGORITHM).build().verify(token);
        } catch (Exception e) {
            throw new Exception("Token inválido ou expirado");
        }
    }

    public static UUID extractPersonId(String token) throws Exception {
        DecodedJWT decoded = validateToken(token);
        return UUID.fromString(decoded.getSubject());
    }

    public static String extractRole(String token) throws Exception {
        DecodedJWT decoded = validateToken(token);
        return decoded.getClaim("role").asString();
    }
}
