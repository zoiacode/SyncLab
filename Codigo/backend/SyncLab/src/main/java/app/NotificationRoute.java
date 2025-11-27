package app;

import java.util.List;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import dao.DaoConnection;
import model.Notification;
import service.notification.CreateNotificationService;
import service.notification.FetchNotificationsByPersonService;
import service.notification.FetchPublicNotificationService;
import service.notification.FetchVisibleNotificationsService;
import service.notification.GetNotificationService;
import service.notification.ReadNotificationService;
import service.notification.UpdateNotificationService;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;
import util.JwtUtil;

public class NotificationRoute {

    public static void routes(Gson gson, DaoConnection connectionObj) {

        post("api/notification", (req, res) -> {
            res.type("application/json");
            try {
                JsonObject body = gson.fromJson(req.body(), JsonObject.class);
                UUID personId = body.has("personId") && !body.get("personId").isJsonNull()
                        ? UUID.fromString(body.get("personId").getAsString())
                        : null;
                String title = body.has("title") ? body.get("title").getAsString() : null;
                String message = body.has("message") ? body.get("message").getAsString() : null;

                CreateNotificationService service = new CreateNotificationService(connectionObj.getConnection());
                Notification notification = service.execute(personId, title, message);

                res.status(201);
                JsonObject resposta = new JsonObject();
                resposta.addProperty("mensagem", "Notificação criada com sucesso!");
                resposta.addProperty("id", notification.getId().toString());
                return gson.toJson(resposta);

            } catch (Exception e) {
                res.status(400);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", "Erro ao criar notificação: " + e.getMessage());
                return gson.toJson(erro);
            }
        });

        get("api/notification/public", (req, res) -> {
            res.type("application/json");
            try {
                FetchPublicNotificationService service = new FetchPublicNotificationService(connectionObj.getConnection());
                List<Notification> list = service.execute();
                JsonArray arr = new JsonArray();
                for (Notification n : list) arr.add(gson.toJsonTree(n));
                res.status(200);
                return gson.toJson(arr);
            } catch (Exception e) {
                res.status(500);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", "Erro ao buscar notificações públicas: " + e.getMessage());
                return gson.toJson(erro);
            }
        });

        get("api/notification/person", (req, res) -> {
            res.type("application/json");
            try {
                String jwtToken = req.cookie("access_token");
                UUID personId = JwtUtil.extractPersonId(jwtToken);
                FetchNotificationsByPersonService service = new FetchNotificationsByPersonService(connectionObj.getConnection());
                List<Notification> list = service.execute(personId);
                JsonArray arr = new JsonArray();
                for (Notification n : list) arr.add(gson.toJsonTree(n));
                res.status(200);
                return gson.toJson(arr);
            } catch (IllegalArgumentException e) {
                res.status(400);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", "ID inválido.");
                return gson.toJson(erro);
            } catch (Exception e) {
                res.status(500);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", "Erro ao buscar notificações: " + e.getMessage());
                return gson.toJson(erro);
            }
        });

        get("api/notification/visible", (req, res) -> {
            res.type("application/json");
            try {
                String jwtToken = req.cookie("access_token");
                UUID userId = JwtUtil.extractPersonId(jwtToken);
                FetchVisibleNotificationsService service = new FetchVisibleNotificationsService(connectionObj.getConnection());
                List<Notification> list = service.execute(userId);
                JsonArray arr = new JsonArray();
                for (Notification n : list) arr.add(gson.toJsonTree(n));
                res.status(200);
                return gson.toJson(arr);
            } catch (IllegalArgumentException e) {
                res.status(400);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", "User ID inválido.");
                return gson.toJson(erro);
            } catch (Exception e) {
                res.status(500);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", "Erro ao buscar notificações visíveis: " + e.getMessage());
                return gson.toJson(erro);
            }
        });

        get("api/notification/:id", (req, res) -> {
            res.type("application/json");
            try {
                UUID id = UUID.fromString(req.params("id"));
                GetNotificationService service = new GetNotificationService(connectionObj.getConnection());
                Notification notification = service.execute(id);
                if (notification == null) {
                    res.status(404);
                    JsonObject erro = new JsonObject();
                    erro.addProperty("erro", "Notificação não encontrada.");
                    return gson.toJson(erro);
                }
                return gson.toJson(notification);
            } catch (IllegalArgumentException e) {
                res.status(400);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", "ID inválido.");
                return gson.toJson(erro);
            } catch (Exception e) {
                res.status(500);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", e.getMessage());
                return gson.toJson(erro);
            }
        });

        put("api/notification/read/:id", (req, res) -> {
            res.type("application/json");
            try {
                JsonObject body = gson.fromJson(req.body(), JsonObject.class);
                UUID userId = UUID.fromString(body.get("userId").getAsString());
                UUID notificationId = UUID.fromString(req.params("id"));
                ReadNotificationService service = new ReadNotificationService(connectionObj.getConnection());
                Notification notification = service.execute(notificationId, userId);
                res.status(200);
                JsonObject resposta = new JsonObject();
                resposta.addProperty("mensagem", "Notificação marcada como lida!");
                resposta.addProperty("id", notification.getId().toString());
                return gson.toJson(resposta);
            } catch (Exception e) {
                res.status(400);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", e.getMessage());
                return gson.toJson(erro);
            }
        });

        put("api/notification/:id", (req, res) -> {
            res.type("application/json");
            try {
                UUID id = UUID.fromString(req.params("id"));
                JsonObject body = gson.fromJson(req.body(), JsonObject.class);
                String newTitle = body.has("title") ? body.get("title").getAsString() : null;
                String newMessage = body.has("message") ? body.get("message").getAsString() : null;
                UpdateNotificationService service = new UpdateNotificationService(connectionObj.getConnection());
                Notification updated = service.execute(id, newTitle, newMessage);
                res.status(200);
                JsonObject resposta = new JsonObject();
                resposta.addProperty("mensagem", "Notificação atualizada com sucesso!");
                resposta.addProperty("id", updated.getId().toString());
                return gson.toJson(resposta);
            } catch (IllegalArgumentException e) {
                res.status(400);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", "ID inválido.");
                return gson.toJson(erro);
            } catch (Exception e) {
                res.status(500);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", e.getMessage());
                return gson.toJson(erro);
            }
        });
    }
}
