package util;

import java.sql.Connection;

import service.auth.RefreshTokenService;
import static spark.Spark.before;
import static spark.Spark.halt;

public class AuthMiddleware {
    public static void register(Connection connection) {
        before("/api/*", (req, res) -> {

            // Liberar OPTIONS antes de qualquer validação
            if ("OPTIONS".equalsIgnoreCase(req.requestMethod())) {
                res.status(200);
                return;
            }

            String accessToken = req.cookie("access_token");
            String refreshToken = req.cookie("refresh_token");

            if (accessToken == null || accessToken.isEmpty()) {

                if (refreshToken != null && !refreshToken.isEmpty()) {
                    try {
                        RefreshTokenService refreshService = new RefreshTokenService(connection);
                        String newAccess = refreshService.execute(refreshToken);

                        res.raw().addHeader(
                                "Set-Cookie",
                                "access_token=" + newAccess +
                                        "; Max-Age=86400; Path=/; SameSite=None; Secure");

                        // NÃO fazer halt aqui — permitir que a rota continue
                        return;

                    } catch (Exception e) {
                        halt(401, "{\"erro\":\"Refresh inválido. Faça login novamente.\"}");
                    }
                }

                halt(401, "{\"erro\":\"Token ausente\"}");
            } else {

                try {
                    JwtUtil.validateToken(accessToken);
                } catch (Exception e) {
                    halt(401, "{\"erro\":\"Token inválido\"}");
                }
            }
        });
    }
}
