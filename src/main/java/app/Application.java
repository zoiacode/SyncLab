package app;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import dao.DaoConnection;
import static spark.Spark.after;
import static spark.Spark.awaitInitialization;
import static spark.Spark.exception;
import static spark.Spark.init;
import static spark.Spark.port;
import util.AuthMiddleware;
import util.Cors;

public class Application {
    public static void main(String[] args) {
        port(8080);

        DaoConnection connectionObj = new DaoConnection();
        connectionObj.conectar();
        Gson gson = new Gson();

        Cors.enableCORS(); // <-- agora funciona

        AuthMiddleware.register(connectionObj.getConnection());

        after((req, res) -> res.type("application/json"));

        exception(Exception.class, (e, req, res) -> {
            res.type("application/json");
            res.status(500);
            JsonObject erro = new JsonObject();
            erro.addProperty("erro", e.getMessage() != null ? e.getMessage() : "Erro interno do servidor");
            res.body(gson.toJson(erro));
        });

        PersonRoute.routes(gson, connectionObj);
        ProfessorRoute.routes(gson, connectionObj);
        StudentRoute.routes(gson, connectionObj);
        AdminRoute.routes(gson, connectionObj);
        AuthRoute.routes(gson, connectionObj);
        LectureRoute.routes(gson, connectionObj);
        NotificationRoute.routes(gson, connectionObj);
        ReservationRoute.routes(gson, connectionObj);
        ResourceRoute.routes(gson, connectionObj);
        BuildingRoute.routes(gson, connectionObj);
        UploadRoute.routes(gson);
        CourseRoute.routes(gson, connectionObj);

        init();
        awaitInitialization();
    }
}
