package app;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import dao.DaoConnection;
import model.Checkin;
import model.Lecture;
import model.Person;
import model.Professor;
import model.Room;
import service.lectures.CheckinManyService;
import service.lectures.DeleteLectureService;
import service.lectures.FetchLecturesService;
import service.lectures.GetLectureService;
import service.lectures.GetStudentInLectureService;
import service.lectures.RegisterLectureService;
import service.lectures.RegisterStudentInLectureService;
import service.lectures.RemoveStudentInLectureService;
import service.lectures.UpdateLectureService;
import service.resources.GetRoomByIdService;
import service.users.GetPersonByIdService;
import service.users.GetProfessorByIdService;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;
import util.JwtUtil;
import util.StudentInClass;

class CreateLectureRequest {
    private Room room;
    private Lecture lecture;
    private Professor professor;

    public Room getRoom() {
        return room;
    }

    public Professor getProfessor() {
        return professor;
    }

    public Lecture getLecture() {
        return lecture;
    }
}

public class LectureRoute {

    public static void routes(Gson gson, DaoConnection connectionObj) {

       get("api/lectures", (req, res) -> {
    res.type("application/json");
    try {
        FetchLecturesService service = new FetchLecturesService(connectionObj.getConnection());
        GetProfessorByIdService professorService = new GetProfessorByIdService(connectionObj.getConnection());
        GetPersonByIdService personService = new GetPersonByIdService(connectionObj.getConnection());
        GetRoomByIdService roomService = new GetRoomByIdService(connectionObj.getConnection());

        List<Lecture> lectures = service.execute();
        JsonArray lecturesWithDetails = new JsonArray();

        for (Lecture lecture : lectures) {
            JsonObject lectureJson = gson.toJsonTree(lecture).getAsJsonObject();

            // Buscar dados do professor
            try {
                Professor professor = professorService.execute(lecture.getProfessorId());
                Person person = personService.execute(professor.getPersonId());
                if (person != null) {
                    JsonObject professorData = new JsonObject();
                    professorData.addProperty("id", professor.getId().toString());
                    professorData.addProperty("name", person.getName());
                    professorData.addProperty("academicDegree", professor.getAcademicDegree());
                    professorData.addProperty("expertiseArea", professor.getExpertiseArea());

                    lectureJson.add("professor", professorData);
                }
            } catch (Exception e) {
                System.err.println("Professor não encontrado: " + lecture.getProfessorId());
            }

            // Buscar dados da sala
            try {
                Room room = roomService.execute(lecture.getRoomId());
                if (room != null) {
                    JsonObject roomData = new JsonObject();
                    roomData.addProperty("id", room.getId().toString());
                    roomData.addProperty("code", room.getCode());
                    roomData.addProperty("capacity", room.getCapacity());
                    roomData.addProperty("roomType", room.getRoomType());
                    roomData.addProperty("status", room.getStatus());
                    roomData.addProperty("floor", room.getFloor());
                    roomData.addProperty("buildingId", room.getBuildingId() != null ? room.getBuildingId().toString() : null);
                    roomData.addProperty("imageUrl", room.getImageUrl());

                    lectureJson.add("room", roomData);
                }
            } catch (Exception e) {
                System.err.println("Sala não encontrada: " + lecture.getRoomId());
            }

            lecturesWithDetails.add(lectureJson);
        }

        return gson.toJson(lecturesWithDetails);
    } catch (Exception e) {
        res.status(400);
        JsonObject erro = new JsonObject();
        erro.addProperty("erro", e.getMessage());
        return gson.toJson(erro);
    }
});

        get("api/lectures/:id", (req, res) -> {
            res.type("application/json");
            try {
                UUID lectureId = UUID.fromString(req.params("id"));
                GetLectureService service = new GetLectureService(connectionObj.getConnection());
                Lecture lecture = service.execute(lectureId);
                return gson.toJson(lecture);
            } catch (Exception e) {
                res.status(400);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", e.getMessage());
                return gson.toJson(erro);
            }
        });

        post("api/lectures", (req, res) -> {
            res.type("application/json");
            try {
                CreateLectureRequest body = gson.fromJson(req.body(), CreateLectureRequest.class);
                String accessToken = req.cookie("access_token");
                String role = JwtUtil.extractRole(accessToken);
                UUID personId = JwtUtil.extractPersonId(accessToken);

                Lecture lecture;
              
                RegisterLectureService service = new RegisterLectureService(connectionObj.getConnection());
                    lecture = service.execute( 
                        (UUID) personId,
                        body.getLecture().getSubjectName(), 
                        body.getProfessor().getId(),
                        body.getRoom().getId(), 
                        body.getLecture().getDate().getValue(), 
                        body.getLecture().getStudentQuantity(),
                        body.getLecture().getEndDate(),
                        body.getLecture().getCourseId(),
                        body.getLecture().getLectureType().getValue()
                    );

                

                return gson.toJson(lecture);
            } catch (Exception e) {
                res.status(400);
                JsonObject erro = new JsonObject();

                e.printStackTrace();
                erro.addProperty("erro", e.getMessage());
                return gson.toJson(erro);
            }
        });

        post("api/lectures/:id/checkin", (req, res) -> {
            res.type("application/json");
            try {
                UUID lectureId = UUID.fromString(req.params("id"));
                String accessToken = req.cookie("access_token");
                UUID personId = JwtUtil.extractPersonId(accessToken);
                CheckinManyService service = new CheckinManyService(connectionObj.getConnection());
                List<Checkin> checkins = service.execute(lectureId, Arrays.asList(personId));

                return gson.toJson(checkins);
            } catch (Exception e) {
                res.status(400);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", e.getMessage());
                return gson.toJson(erro);
            }
        });

        put("api/lectures/:id", (req, res) -> {
            res.type("application/json");
            try {
                CreateLectureRequest body = gson.fromJson(req.body(), CreateLectureRequest.class);
                UUID lectureId = UUID.fromString(req.params("id"));
                
                String accessToken = req.cookie("access_token");
                UUID currentUserId = JwtUtil.extractPersonId(accessToken);

                UpdateLectureService service = new UpdateLectureService(connectionObj.getConnection());
                Lecture updated = service.execute(
                       lectureId,
                       body.getLecture().getSubjectName(),
                       body.getProfessor().getId(),
                       currentUserId,
                       body.getRoom().getId(),
                       body.getLecture().getDate().getValue(),
                       true
                );

                return gson.toJson(updated);
            } catch (Exception e) {
                res.status(400);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", e.getMessage());
                return gson.toJson(erro);
            }
        });
        
        get("api/lecture/:id/person", (req, res) -> {
             res.type("application/json");
            try {
                UUID lectureId = UUID.fromString(req.params("id"));
                GetStudentInLectureService service = new GetStudentInLectureService(connectionObj.getConnection());
                List<StudentInClass>
                lecture = service.execute(lectureId);
                return gson.toJson(lecture);
            } catch (Exception e) {
                res.status(400);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", e.getMessage());
                return gson.toJson(erro);
            }
        });

        post("api/lecture/:lectureId/join", (req, res) -> {
            res.type("application/json");
            try {
                UUID lectureId = UUID.fromString(req.params("lectureId"));

                String accessToken = req.cookie("access_token");
                if (accessToken == null) {
                    res.status(401);
                    JsonObject erro = new JsonObject();
                    erro.addProperty("erro", "Token de autenticação ausente.");
                    return gson.toJson(erro);
                }

                UUID currentUserId = JwtUtil.extractPersonId(accessToken);

                RegisterStudentInLectureService service = new RegisterStudentInLectureService(connectionObj.getConnection());
                service.execute(currentUserId, lectureId);

                JsonObject resposta = new JsonObject();
                resposta.addProperty("mensagem", "Aula deletada com sucesso!");
                return gson.toJson(resposta);
            } catch (Exception e) {
                res.status(400);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", e.getMessage());
                return gson.toJson(erro);
            }
        });

         delete("api/lecture/:lectureId/join", (req, res) -> {
            res.type("application/json");
            try {
                UUID lectureId = UUID.fromString(req.params("lectureId"));

                String accessToken = req.cookie("access_token");
                if (accessToken == null) {
                    res.status(401);
                    JsonObject erro = new JsonObject();
                    erro.addProperty("erro", "Token de autenticação ausente.");
                    return gson.toJson(erro);
                }

                UUID currentUserId = JwtUtil.extractPersonId(accessToken);

                RemoveStudentInLectureService service = new RemoveStudentInLectureService(connectionObj.getConnection());
                service.execute(currentUserId, lectureId);

                JsonObject resposta = new JsonObject();
                resposta.addProperty("mensagem", "Aula deletada com sucesso!");
                return gson.toJson(resposta);
            } catch (Exception e) {
                res.status(400);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", e.getMessage());
                return gson.toJson(erro);
            }
        });

        delete("api/lectures/:id", (req, res) -> {
            res.type("application/json");
            try {
                UUID lectureId = UUID.fromString(req.params("id"));

                String accessToken = req.cookie("access_token");
                if (accessToken == null) {
                    res.status(401);
                    JsonObject erro = new JsonObject();
                    erro.addProperty("erro", "Token de autenticação ausente.");
                    return gson.toJson(erro);
                }

                String role = JwtUtil.extractRole(accessToken);
                UUID currentUserId = JwtUtil.extractPersonId(accessToken);
                boolean isAdmin = "Admin".equalsIgnoreCase(role);

                DeleteLectureService service = new DeleteLectureService(connectionObj.getConnection());
                service.execute(lectureId, currentUserId, isAdmin);

                JsonObject resposta = new JsonObject();
                resposta.addProperty("mensagem", "Aula deletada com sucesso!");
                return gson.toJson(resposta);
            } catch (Exception e) {
                res.status(400);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", e.getMessage());
                return gson.toJson(erro);
            }
        });
    }
}
