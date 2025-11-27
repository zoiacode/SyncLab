package app;

import java.util.List;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import dao.DaoConnection;
import model.Building;
import service.building.CreateBuildingService;
import service.building.DeleteBuildingService;
import service.building.GetAllBuildingsService;
import service.building.GetBuildingByIdService;
import service.building.UpdateBuildingService;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

class CreateUpdateBuildingRequest {
    private String buildCode;
    private int floor;
    private String campus;

    public String getBuildCode() {
        return buildCode;
    }

    public int getFloor() {
        return floor;
    }

    public String getCampus() {
        return campus;
    }
}

public class BuildingRoute {

    public static void routes(Gson gson, DaoConnection connectionObj) {

        post("api/building", (req, res) -> {
            res.type("application/json");
            try {
                CreateUpdateBuildingRequest bodyReq = gson.fromJson(req.body(), CreateUpdateBuildingRequest.class);

                CreateBuildingService service = new CreateBuildingService(connectionObj.getConnection());
                
                Building building = service.execute(
                    bodyReq.getBuildCode(), 
                    bodyReq.getFloor(), 
                    bodyReq.getCampus()
                );
                
                res.status(201);
                JsonObject resposta = new JsonObject();
                resposta.addProperty("mensagem", "Building criado com sucesso!");
                resposta.addProperty("id", building.getId().toString());
                return gson.toJson(resposta);

            } catch (Exception e) {
                res.status(400);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", e.getMessage());
                return gson.toJson(erro);
            }
        });

        get("api/building", (req, res) -> {
            res.type("application/json");
            try {
                GetAllBuildingsService service = new GetAllBuildingsService(connectionObj.getConnection());

                List<Building> buildings = service.execute();

                JsonArray jsonList = new JsonArray();
                for (Building b : buildings) {
                    jsonList.add(gson.toJsonTree(b));
                }

                res.status(200);
                return gson.toJson(jsonList);

            } catch (Exception e) {
                res.status(500);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", "Erro ao buscar buildings: " + e.getMessage());
                return gson.toJson(erro);
            }
        });

        get("api/building/:id", (req, res) -> {
            res.type("application/json");
            try {
                UUID id = UUID.fromString(req.params("id"));

                GetBuildingByIdService service = new GetBuildingByIdService(connectionObj.getConnection());

                Building building = service.execute(id);

                res.status(200);
                return gson.toJson(building);

            } catch (IllegalArgumentException e) {
                res.status(400);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", "ID inválido.");
                return gson.toJson(erro);
            } catch (Exception e) {
                if (e.getMessage().contains("não encontrado")) {
                    res.status(404);
                } else {
                    res.status(500);
                }
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", e.getMessage());
                return gson.toJson(erro);
            }
        });
        
        put("api/building/:id", (req, res) -> {
            res.type("application/json");
            try {
                UUID id = UUID.fromString(req.params("id"));
                CreateUpdateBuildingRequest bodyReq = gson.fromJson(req.body(), CreateUpdateBuildingRequest.class);

                UpdateBuildingService service = new UpdateBuildingService(connectionObj.getConnection());
                
                Building building = service.execute(
                    id,
                    bodyReq.getBuildCode(), 
                    bodyReq.getFloor(), 
                    bodyReq.getCampus()
                );
                
                res.status(200);
                JsonObject resposta = new JsonObject();
                resposta.addProperty("mensagem", "Building atualizado com sucesso!");
                resposta.addProperty("id", building.getId().toString());
                return gson.toJson(resposta);

            } catch (IllegalArgumentException e) {
                res.status(400);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", "ID inválido.");
                return gson.toJson(erro);
            } catch (Exception e) {
                if (e.getMessage().contains("não encontrado")) {
                    res.status(404);
                } else {
                    res.status(400);
                }
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", e.getMessage());
                return gson.toJson(erro);
            }
        });

        delete("api/building/:id", (req, res) -> {
            res.type("application/json");
            try {
                UUID id = UUID.fromString(req.params("id"));

                DeleteBuildingService service = new DeleteBuildingService(connectionObj.getConnection());
                service.execute(id);

                res.status(200);
                JsonObject resposta = new JsonObject();
                resposta.addProperty("mensagem", "Building deletado com sucesso!");
                return gson.toJson(resposta);

            } catch (IllegalArgumentException e) {
                res.status(400);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", "ID inválido.");
                return gson.toJson(erro);
            } catch (Exception e) {
                if (e.getMessage().contains("não encontrado")) {
                    res.status(404);
                } else {
                    res.status(500);
                }
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", e.getMessage());
                return gson.toJson(erro);
            }
        });
    }
}