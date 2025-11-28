package util;

import java.sql.Connection;

import javax.servlet.http.Cookie;

import service.auth.RefreshTokenService;
import static spark.Spark.before;
import static spark.Spark.halt;

public class AuthMiddleware {
    public static void register(Connection connection) {
        before("/api/*", (req, res) -> {
            String accessToken = null;

            Cookie[] cookie = req.raw().getCookies();
            for (Cookie c : cookie) {
                if ("access_token".equals(c.getName())) {
                    accessToken = c.getValue();
                }
            }
            System.out.println(accessToken);
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

                    } catch (Exception e) {
                        halt(401, "{\"erro\":\"Refresh inválido. Faça login novamente.\"}");
                    }
                } else {
                    halt(401, "{\"erro\":\"Token ausente\"}");

                }
            } else {

                JwtUtil.validateToken(accessToken);

            }

            if ("OPTIONS".equalsIgnoreCase(req.requestMethod())) {
                return;
            }

        });

    }
}