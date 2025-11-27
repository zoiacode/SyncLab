package app;

import java.util.List;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import dao.DaoConnection;
import model.Equipment;
import model.Room;
import service.resources.CreateEquipmentService;
import service.resources.CreateRoomService;
import service.resources.DeleteEquipmentService;
import service.resources.DeleteRoomService;
import service.resources.FetchEquipmentService;
import service.resources.FetchRoomService;
import service.resources.GetEquipmentByIdService;
import service.resources.GetRoomByIdService;
import service.resources.UpdateEquipmentService;
import service.resources.UpdateRoomService;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

public class ResourceRoute {

    public static void routes(Gson gson, DaoConnection connectionObj) {


        post("api/equipment", (req, res) -> {
            res.type("application/json");
            try {
                Equipment bodyReq = gson.fromJson(req.body(), Equipment.class);
                CreateEquipmentService service = new CreateEquipmentService(connectionObj.getConnection());
                Equipment created = service.execute(
                        bodyReq.getName(),
                        bodyReq.getDescription(),
                        bodyReq.getQuantity(),
                        bodyReq.getStatus(),
                        bodyReq.getMaxLoanDuration(),
                        bodyReq.getImageUrl()
                );
                return gson.toJson(created);
            } catch (Exception e) {
                res.status(400);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", e.getMessage());
                return gson.toJson(erro);
            }
        });

        get("api/equipment", (req, res) -> {
            res.type("application/json");
            try {
                FetchEquipmentService service = new FetchEquipmentService(connectionObj.getConnection());
                List<Equipment> list = service.execute(null);
                return gson.toJson(list);
            } catch (Exception e) {
                res.status(400);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", e.getMessage());
                return gson.toJson(erro);
            }
        });

        get("api/equipment/:id", (req, res) -> {
            res.type("application/json");
            try {
                UUID id = UUID.fromString(req.params("id"));
                GetEquipmentByIdService service = new GetEquipmentByIdService(connectionObj.getConnection());
                Equipment equipment = service.execute(id);
                return gson.toJson(equipment);
            } catch (Exception e) {
                res.status(400);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", e.getMessage());
                return gson.toJson(erro);
            }
        });

        put("api/equipment/:id", (req, res) -> {
            res.type("application/json");
            try {
                UUID id = UUID.fromString(req.params("id"));
                Equipment bodyReq = gson.fromJson(req.body(), Equipment.class);
                UpdateEquipmentService service = new UpdateEquipmentService(connectionObj.getConnection());
                Equipment updated = service.execute(
                        id,
                        bodyReq.getName(),
                        bodyReq.getDescription(),
                        bodyReq.getQuantity(),
                        bodyReq.getStatus(),
                        bodyReq.getMaxLoanDuration(),
                        bodyReq.getImageUrl()
                );
                return gson.toJson(updated);
            } catch (Exception e) {
                res.status(400);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", e.getMessage());
                return gson.toJson(erro);
            }
        });

        delete("api/equipment/:id", (req, res) -> {
            res.type("application/json");
            try {
                UUID id = UUID.fromString(req.params("id"));
                DeleteEquipmentService service = new DeleteEquipmentService(connectionObj.getConnection());
                Equipment deleted = service.execute(id);
                return gson.toJson(deleted);
            } catch (Exception e) {
                res.status(400);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", e.getMessage());
                return gson.toJson(erro);
            }
        });


        post("api/room", (req, res) -> {
            res.type("application/json");
            try {
                Room bodyReq = gson.fromJson(req.body(), Room.class);
                CreateRoomService service = new CreateRoomService(connectionObj.getConnection());
                Room created = service.execute(
                        bodyReq.getCapacity(),
                        bodyReq.getRoomType(),
                        bodyReq.getCode(),
                        bodyReq.getStatus(),
                        bodyReq.getImageUrl(),
                        bodyReq.getBuildingId(),
                        bodyReq.getFloor()
                );
                return gson.toJson(created);
            } catch (Exception e) {
                res.status(400);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", e.getMessage());
                return gson.toJson(erro);
            }
        });

        get("api/room", (req, res) -> {
            res.type("application/json");
            try {
                FetchRoomService service = new FetchRoomService(connectionObj.getConnection());
                List<Room> list = service.execute(null);
                return gson.toJson(list);
            } catch (Exception e) {
                res.status(400);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", e.getMessage());
                return gson.toJson(erro);
            }
        });

        get("api/room/:id", (req, res) -> {
            res.type("application/json");
            try {
                UUID id = UUID.fromString(req.params("id"));
                GetRoomByIdService service = new GetRoomByIdService(connectionObj.getConnection());
                Room room = service.execute(id);
                return gson.toJson(room);
            } catch (Exception e) {
                res.status(400);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", e.getMessage());
                return gson.toJson(erro);
            }
        });

        put("api/room/:id", (req, res) -> {
            res.type("application/json");
            try {
                UUID id = UUID.fromString(req.params("id"));
                Room bodyReq = gson.fromJson(req.body(), Room.class);
                UpdateRoomService service = new UpdateRoomService(connectionObj.getConnection());
                Room updated = service.execute(
                        id,
                        bodyReq.getCapacity(),
                        bodyReq.getRoomType(),
                        bodyReq.getCode(),
                        bodyReq.getStatus(),
                        bodyReq.getImageUrl(),
                        bodyReq.getBuildingId(),
                        bodyReq.getFloor()
                );
                return gson.toJson(updated);
            } catch (Exception e) {
                res.status(400);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", e.getMessage());
                return gson.toJson(erro);
            }
        });

        delete("api/room/:id", (req, res) -> {
            res.type("application/json");
            try {
                UUID id = UUID.fromString(req.params("id"));
                DeleteRoomService service = new DeleteRoomService(connectionObj.getConnection());
                Room deleted = service.execute(id);
                return gson.toJson(deleted);
            } catch (Exception e) {
                res.status(400);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", e.getMessage());
                return gson.toJson(erro);
            }
        });

    }
}
