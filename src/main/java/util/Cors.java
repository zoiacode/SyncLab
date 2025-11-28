package util;

import static spark.Spark.before;
import static spark.Spark.options;

public class Cors {
public static void enableCORS() {

        // OPTIONS deve ser liberado ANTES de qualquer AuthMiddleware
        options("/*", (req, res) -> {
            res.header("Access-Control-Allow-Origin", "https://sync-lab-front-olive.vercel.app");
            res.header("Access-Control-Allow-Credentials", "true");
            res.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
            res.header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
            res.header("Vary", "Origin");
            return "OK";
        });

        // ONLY CORS headers (não mexe em Set-Cookie)
        before((req, res) -> {
            res.header("Access-Control-Allow-Origin", "https://sync-lab-front-olive.vercel.app");
            res.header("Access-Control-Allow-Credentials", "true");
            res.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
            res.header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
            res.header("Vary", "Origin");

            // IMPORTANTE: não retornar 401 aqui, deixar AuthMiddleware fazer
        });
    }   
}