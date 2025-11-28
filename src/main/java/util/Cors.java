package util;

import static spark.Spark.before;
import static spark.Spark.options;

public class Cors {
    public static void enableCORS() {
        String origin = "https://sync-lab-front-fifz5d8uk-pedroitalobcs-projects.vercel.app";

        before((req, res) -> {
            res.header("Access-Control-Allow-Origin", origin);
            res.header("Access-Control-Allow-Credentials", "true");
            res.header("Access-Control-Allow-Headers", "Content-Type,Authorization");
            res.header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
        });

        options("/*", (req, res) -> {
            res.header("Access-Control-Allow-Origin", origin);
            res.header("Access-Control-Allow-Credentials", "true");
            res.header("Access-Control-Allow-Headers", "Content-Type,Authorization");
            res.header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
            return "OK";
        });
    }
}
