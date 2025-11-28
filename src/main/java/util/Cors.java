package util;

import static spark.Spark.before;
import static spark.Spark.options;

public class Cors {
    public static void enableCORS() {
        String origin = "https://sync-lab-front-olive.vercel.app";

        before((req, res) -> {
            res.header("Access-Control-Allow-Origin", origin);
            res.header("Access-Control-Allow-Credentials", "true");
            res.header("Access-Control-Allow-Headers", "Content-Type,Authorization,Cookie,Set-Cookie");
            res.header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
            res.header("Access-Control-Expose-Headers", "Set-Cookie");
        });

        options("/*", (req, res) -> {
            res.status(200);

            String requestHeaders = req.headers("Access-Control-Request-Headers");
            if (requestHeaders != null)
                res.header("Access-Control-Allow-Headers", requestHeaders);

            String requestMethod = req.headers("Access-Control-Request-Method");
            if (requestMethod != null)
                res.header("Access-Control-Allow-Methods", requestMethod);

            return "OK";
        });
    }
}
