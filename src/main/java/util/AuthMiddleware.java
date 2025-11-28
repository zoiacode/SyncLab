package util;

import java.sql.Connection;

import service.auth.RefreshTokenService;
import static spark.Spark.before;
import static spark.Spark.halt;

public class AuthMiddleware {
    public static void register(Connection connection) {
        before("/api/*", (req, res) -> {
            if ("OPTIONS".equalsIgnoreCase(req.requestMethod())) return;

            String accessToken = req.cookie("access_token");
            String refreshToken = req.cookie("refresh_token");

            if (accessToken == null || accessToken.isEmpty()) {
                if (refreshToken != null && !refreshToken.isEmpty()) {
                    try {
                        RefreshTokenService refreshService = new RefreshTokenService(connection);
                        accessToken = refreshService.execute(refreshToken);
                        
                        // Max-Age = 24 horas * 60 minutos * 60 segundos = 86400
                        int maxAgeInSeconds = 86400; 

                        String newAccessTokenCookie = String.format(
                            "access_token=%s; Max-Age=%d; Path=/; SameSite=None",
                            accessToken,
                            maxAgeInSeconds // AGORA DURA 1 DIA
                        );
                        res.header("Set-Cookie", newAccessTokenCookie);
                        
                    } catch (Exception e) {
                        halt(401, "{\"erro\":\"Token inválido ou expirado. Faça login novamente.\"}");
                    }
                } else {
                    halt(401, "{\"erro\":\"Token ausente\"}");
                }
            }
        });
    }
}