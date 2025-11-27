package app;

import java.util.List;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import dao.DaoConnection;
import model.Course;
import service.course.CreateCourseService;
import service.course.DeleteCourseByIdService;
import service.course.FetchCourseService;
import service.course.GetCourseByIdService;
import service.course.UpdateCourseService;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

class CreateCourseRequest {
    private Course course;

    public Course getCourse() {
        return course;
    }
}

public class CourseRoute {

    public static void routes(Gson gson, DaoConnection connectionObj) {

        get("api/course", (req, res) -> {
            res.type("application/json");

            FetchCourseService service = new FetchCourseService(connectionObj.getConnection());

            try {

                List<Course> courses = service.execute();

                JsonArray listCoursesJson = new JsonArray();

                for (Course course : courses) {
                    listCoursesJson.add(gson.toJsonTree(course));
                }

                if (listCoursesJson.size() == 0) {
                    res.status(200);
                    return gson.toJson(listCoursesJson);
                }

                return gson.toJson(listCoursesJson);

            } catch (Exception e) {
                res.status(500);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", "Erro ao buscar a lista de pessoas: " + e.getMessage());
                return gson.toJson(erro);
            }
        });

        get("api/course/:id", (req, res) -> {
            res.type("application/json");
            try {
                UUID id = UUID.fromString(req.params("id"));
                GetCourseByIdService service = new GetCourseByIdService(connectionObj.getConnection());
                Course course = service.execute(id);

                if (course == null) {
                    res.status(404);
                    JsonObject erro = new JsonObject();
                    erro.addProperty("erro", "Pessoa não encontrada!");
                    return gson.toJson(erro);
                }

                return gson.toJson(course);

            } catch (IllegalArgumentException e) {
                res.status(400);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", "ID no formato inválido.");
                return gson.toJson(erro);
            } catch (Exception e) {
                res.status(400);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", e.getMessage());
                return gson.toJson(erro);
            }
        });

        post("api/course", (req, res) -> {
            res.type("application/json");

            CreateCourseRequest bodyReq = gson.fromJson(req.body(), CreateCourseRequest.class);
            CreateCourseService service = new CreateCourseService(connectionObj.getConnection());

            try {
                Course course = service.execute(
                    bodyReq.getCourse().getName(),
                    bodyReq.getCourse().getAcg(),
                    bodyReq.getCourse().getSchedule()
                );


                JsonObject resposta = new JsonObject();
                res.type("application/json");
                res.status(201);
                resposta.addProperty("mensagem", "Curso criada com sucesso!");
                resposta.addProperty("id", course.getId().toString());
                return gson.toJson(resposta);

            } catch (Exception e) {
                res.status(400);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", e.getMessage());
                return gson.toJson(erro);
            }
        });

          put("api/course/:id", (req, res) -> {
            res.type("application/json");

            UUID id = UUID.fromString(req.params("id"));

            CreateCourseRequest bodyReq = gson.fromJson(req.body(), CreateCourseRequest.class);
            UpdateCourseService service = new UpdateCourseService(connectionObj.getConnection());

            try {
                Course course = service.execute(
                    id,
                    bodyReq.getCourse().getName(),
                    bodyReq.getCourse().getAcg(),
                    bodyReq.getCourse().getSchedule()
                );


                JsonObject resposta = new JsonObject();
                res.type("application/json");
                res.status(201);
                resposta.addProperty("mensagem", "Curso criada com sucesso!");
                resposta.addProperty("id", course.getId().toString());
                return gson.toJson(resposta);

            } catch (Exception e) {
                res.status(400);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", e.getMessage());
                return gson.toJson(erro);
            }
        });


        delete("api/course/:id", (req, res) -> {
            res.type("application/json");

            try {
                UUID id = UUID.fromString(req.params("id"));
                DeleteCourseByIdService service = new DeleteCourseByIdService(connectionObj.getConnection());

                service.execute(id);

                res.status(200);
                JsonObject resposta = new JsonObject();
                return gson.toJson(resposta);

            } catch (IllegalArgumentException e) {
                res.status(400);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", "ID no formato inválido.");
                return gson.toJson(erro);
            } catch (Exception e) {
                res.status(500);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", "Erro ao excluir a pessoa: " + e.getMessage());
                return gson.toJson(erro);
            }
        });

    }

}
