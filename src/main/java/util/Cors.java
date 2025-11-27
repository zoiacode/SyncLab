package util;

import static spark.Spark.before;
import static spark.Spark.options;

public class Cors {
    public static void enableCORS() {
        before((req, res) -> {
            res.header("Access-Control-Allow-Origin",
                    "https://sync-lab-front-olive.vercel.app");
            res.header("Access-Control-Allow-Credentials", "true");

            res.header("Access-Control-Allow-Headers",
                    "Content-Type, Authorization, Cookie, Set-Cookie, X-Requested-With");

            res.header("Access-Control-Allow-Methods",
                    "GET, POST, PUT, DELETE, OPTIONS");

            res.header("Access-Control-Expose-Headers", "Set-Cookie");
        });

        options("/*", (req, res) -> {
            res.status(200);

            String reqHeaders = req.headers("Access-Control-Request-Headers");
            if (reqHeaders != null)
                res.header("Access-Control-Allow-Headers", reqHeaders);

            String reqMethod = req.headers("Access-Control-Request-Method");
            if (reqMethod != null)
                res.header("Access-Control-Allow-Methods", reqMethod);

            res.header("Access-Control-Allow-Origin",
                    "https://sync-lab-front-olive.vercel.app");

            res.header("Access-Control-Allow-Credentials", "true");

            return "OK";
        });
    }
}
