package util;

import static spark.Spark.before;
import static spark.Spark.options;

public class Cors {
    public static void enableCORS() {
        String origin = "https://sync-lab-front-olive.vercel.app";

        before((req, res) -> {
            res.header("Access-Control-Allow-Origin", origin);
            res.header("Access-Control-Allow-Credentials", "true");
            res.header("Access-Control-Allow-Headers", "Content-Type,Authorization");
            res.header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");

            System.out.print(req.cookie("access_token"));
        });

        options("/*", (req, res) -> {
            return "OK";
        });
    }
}
