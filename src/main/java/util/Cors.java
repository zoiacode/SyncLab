package util;

import static spark.Spark.before;
import static spark.Spark.options;

public class Cors {
    public static void enableCORS() {
        before((req, res) -> {
            res.header("Access-Control-Allow-Origin", "https://sync-lab-front-olive.vercel.app");
            res.header("Access-Control-Allow-Credentials", "true");
            res.header("Access-Control-Allow-Headers", "Content-Type,Authorization");
            res.header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
        });

        options("/*", (req, res) -> {
            res.status(200);

            String headers = req.headers("Access-Control-Request-Headers");
            if (headers != null)
                res.header("Access-Control-Allow-Headers", headers);

            String methods = req.headers("Access-Control-Request-Method");
            if (methods != null)
                res.header("Access-Control-Allow-Methods", methods);

            return "OK";
        });
    }
}