package app;

import java.util.List;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import dao.DaoConnection;
import dao.ReservationDao;
import model.Equipment;
import model.Lecture;
import model.RecommendationResult;
import model.Reservation;
import model.Room;
import service.inteligentSystem.ReservationSolverService;
import service.reservation.ApproveReservationService;
import service.reservation.FetchByStatusReservationService;
import service.reservation.FetchReservationByPersonIdService;
import service.reservation.FetchReservationService;
import service.reservation.GetReservationByIdService;
import service.reservation.RegisterEquipmentReservationService;
import service.reservation.RegisterRoomReservationAdminService;
import service.reservation.RegisterRoomReservationProfessorService;
import service.reservation.RejectReservationService;
import service.reservation.UpdateReservationsService;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;
import util.JwtUtil;

class CreateReservationRequest {

    private Reservation reservation;
    private Room room;
    private Equipment equipment;
    private Lecture lecture;

    public Reservation getReservation() {
        return reservation;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public Room getRoom() {
        return room;
    }

    public Lecture getLecture() {
        return lecture;
    }
}

public class ReservationRoute {

    public static void routes(Gson gson, DaoConnection connectionObj) {

        get("api/reservation/recommendation", (req, res) -> {
            res.type("application/json");
            try {
                String jwtToken = req.cookie("access_token");
                if (jwtToken == null) {
                    res.status(401);
                    JsonObject erro = new JsonObject();
                    erro.addProperty("erro", "Token de autenticação ausente.");
                    return gson.toJson(erro);
                }

                UUID currentUserId = JwtUtil.extractPersonId(jwtToken);
                String role = JwtUtil.extractRole(jwtToken);
                ReservationSolverService service = new ReservationSolverService(connectionObj.getConnection());
                RecommendationResult result = service.execute(currentUserId, role);

                JsonObject json = new JsonObject();
                json.add("roomRecommendations", gson.toJsonTree(result.getRoomRecommendations()));
                // json.add("equipmentRecommendations", gson.toJsonTree(result.getEquipmentRecommendations()));

                res.status(200);
                return gson.toJson(json);

            } catch (Exception e) {
                e.printStackTrace();
                res.status(500);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", "Erro ao gerar recomendações: " + e.getMessage());
                return gson.toJson(erro);
            }
        });

        get("api/reservation/person", (req, res) -> {
            res.type("application/json");
            try {
                String jwtToken = req.cookie("access_token");
                if (jwtToken == null) {
                    res.status(401);
                    JsonObject erro = new JsonObject();
                    erro.addProperty("erro", "Token de autenticação ausente.");
                    return gson.toJson(erro);
                }

                UUID personId = JwtUtil.extractPersonId(jwtToken);

                FetchReservationByPersonIdService service = new FetchReservationByPersonIdService(connectionObj.getConnection());

                List<Reservation> reservations = service.execute(personId);

                JsonArray jsonList = new JsonArray();

                for (Reservation r : reservations) {
                    jsonList.add(gson.toJsonTree(r));
                }

                res.status(200);

                return gson.toJson(jsonList);
            } catch (Exception e) {
                res.status(500);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", "Erro ao buscar reservas do usuário: " + e.getMessage());
                return gson.toJson(erro);
            }
        });

        get("api/reservation", (req, res) -> {
            res.type("application/json");
            FetchReservationService service = new FetchReservationService(connectionObj.getConnection());

            try {
                List<Reservation> reservations = service.execute();

                JsonArray jsonList = new JsonArray();

                for (Reservation r : reservations) {
                    jsonList.add(gson.toJsonTree(r));
                }

                res.status(200);

                return gson.toJson(jsonList);
            } catch (Exception e) {
                res.status(500);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", "Erro ao buscar reservas: " + e.getMessage());
                return gson.toJson(erro);
            }
        });

        get("api/reservation/:id", (req, res) -> {
            res.type("application/json");
            try {
                UUID id = UUID.fromString(req.params("id"));

                GetReservationByIdService service = new GetReservationByIdService(connectionObj.getConnection());

                Reservation reservation = service.execute(id);

                if (reservation == null) {
                    res.status(404);
                    JsonObject erro = new JsonObject();
                    erro.addProperty("erro", "Reserva não encontrada!");
                    return gson.toJson(erro);
                }

                return gson.toJson(reservation);
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

        delete("api/reservation/:id", (req, res) -> {
            res.type("application/json");
            try {
                UUID id = UUID.fromString(req.params("id"));

                ReservationDao dao = new ReservationDao(connectionObj.getConnection());
                dao.deleteById(id);

                return gson.toJson("Deletado");
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

        get("api/reservation/status/:status", (req, res) -> {
            res.type("application/json");
            try {
                String status = req.params("status");

                FetchByStatusReservationService service = new FetchByStatusReservationService(connectionObj.getConnection());

                List<Reservation> reservations = service.execute(status);

                JsonArray jsonList = new JsonArray();

                for (Reservation r : reservations) {
                    jsonList.add(gson.toJsonTree(r));
                }

                res.status(200);

                return gson.toJson(jsonList);
            } catch (Exception e) {
                res.status(500);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", e.getMessage());
                return gson.toJson(erro);
            }
        });

        post("api/reservation/equipment", (req, res) -> {
            res.type("application/json");
            try {
                String jwtToken = req.cookie("access_token");
                UUID userId = JwtUtil.extractPersonId(jwtToken);
                CreateReservationRequest bodyReq = gson.fromJson(req.body(), CreateReservationRequest.class);
                RegisterEquipmentReservationService service = new RegisterEquipmentReservationService(connectionObj.getConnection());
                Reservation reservation = service.execute(
                        userId,
                        bodyReq.getEquipment().getId(),
                        bodyReq.getReservation().getPurpose(),
                        bodyReq.getReservation().getStartTime()
                );
                res.status(201);
                JsonObject resposta = new JsonObject();
                resposta.addProperty("mensagem", "Reserva realizada com sucesso! Aguardando confirmação.");
                resposta.addProperty("id", reservation.getId().toString());
                return gson.toJson(resposta);
            } catch (Exception e) {
                res.status(400);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", e.getMessage());
                return gson.toJson(erro);
            }
        });

        post("api/reservation/room", (req, res) -> {
            res.type("application/json");

            try {
                String jwtToken = req.cookie("access_token");
                if (jwtToken == null) {
                    res.status(401);
                    JsonObject erro = new JsonObject();
                    erro.addProperty("erro", "Token de autenticação ausente.");
                    return gson.toJson(erro);
                }

                UUID userId = JwtUtil.extractPersonId(jwtToken);
                String role = JwtUtil.extractRole(jwtToken);

                CreateReservationRequest bodyReq = gson.fromJson(req.body(), CreateReservationRequest.class);
                Reservation reservation;

                switch (role.toUpperCase()) {
                    case "ADMIN":
                        RegisterRoomReservationAdminService adminService
                                = new RegisterRoomReservationAdminService(connectionObj.getConnection());
                        reservation = adminService.execute(
                                userId,
                                bodyReq.getRoom().getId(),
                                bodyReq.getReservation().getPurpose(),
                                bodyReq.getReservation().getStartTime(),
                                bodyReq.getLecture().getCourseId()
                        );
                        break;

                    case "PROFESSOR":
                        RegisterRoomReservationProfessorService professorService
                                = new RegisterRoomReservationProfessorService(connectionObj.getConnection());
                        reservation = professorService.execute(
                                userId,
                                bodyReq.getRoom().getId(),
                                bodyReq.getReservation().getPurpose(),
                                bodyReq.getReservation().getStartTime(),
                                bodyReq.getLecture().getCourseId()
                        );
                        break;

                    default:
                        res.status(403);
                        JsonObject erro = new JsonObject();
                        erro.addProperty("erro", "Permissão negada para este tipo de usuário.");
                        return gson.toJson(erro);
                }

                res.status(201);
                JsonObject resposta = new JsonObject();
                resposta.addProperty("mensagem", "Reserva criada com sucesso!");
                resposta.addProperty("id", reservation.getId().toString());
                return gson.toJson(resposta);

            } catch (Exception e) {
                res.status(400);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", e.getMessage());
                return gson.toJson(erro);
            }
        });

        put("api/reservation/:id", (req, res) -> {
            res.type("application/json");
            try {
                String jwtToken = req.cookie("access_token");
                UUID approverId = JwtUtil.extractPersonId(jwtToken);
                UUID reservationId = UUID.fromString(req.params("id"));
                UpdateReservationsService service = new UpdateReservationsService(connectionObj.getConnection());
                CreateReservationRequest bodyReq = gson.fromJson(req.body(), CreateReservationRequest.class);
                Reservation reservation = service.execute(
                        (UUID) reservationId,
                        bodyReq.getReservation().getPurpose()
                );
                res.status(200);
                JsonObject resposta = new JsonObject();
                resposta.addProperty("mensagem", "Reserva aprovada!");
                resposta.addProperty("id", reservation.getId().toString());
                return gson.toJson(resposta);
            } catch (Exception e) {
                res.status(500);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", e.getMessage());
                return gson.toJson(erro);
            }
        });

        put("api/reservation/approve/:id", (req, res) -> {
            res.type("application/json");
            try {
                String jwtToken = req.cookie("access_token");
                UUID approverId = JwtUtil.extractPersonId(jwtToken);
                UUID reservationId = UUID.fromString(req.params("id"));

                ApproveReservationService service = new ApproveReservationService(connectionObj.getConnection());
                Reservation reservation = service.execute(approverId, reservationId);
                res.status(200);
                JsonObject resposta = new JsonObject();
                resposta.addProperty("mensagem", "Reserva aprovada!");
                resposta.addProperty("id", reservation.getId().toString());
                return gson.toJson(resposta);
            } catch (Exception e) {
                res.status(500);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", e.getMessage());
                return gson.toJson(erro);
            }
        });

        put("api/reservation/reject/:id", (req, res) -> {
            res.type("application/json");
            try {
                String jwtToken = req.cookie("access_token");
                UUID rejecterId = JwtUtil.extractPersonId(jwtToken);
                UUID reservationId = UUID.fromString(req.params("id"));

                RejectReservationService service = new RejectReservationService(connectionObj.getConnection());
                Reservation reservation = service.execute(rejecterId, reservationId);
                res.status(200);
                JsonObject resposta = new JsonObject();
                resposta.addProperty("mensagem", "Reserva Rejeitada!");
                resposta.addProperty("id", reservation.getId().toString());
                return gson.toJson(resposta);
            } catch (Exception e) {
                res.status(500);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", e.getMessage());
                return gson.toJson(erro);
            }
        });

    }
}
