package app;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import at.favre.lib.crypto.bcrypt.BCrypt;
import dao.DaoConnection;
import model.Credential;
import model.Person;
import model.Student;
import service.users.DeleteStudentByIdService;
import service.users.FetchStudentService;
import service.users.GetStudentByIdService;
import service.users.RegisterStudentService;
import service.users.UpdateStudentService;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

class CreateStudentRequest {
    private Person person;
    private Student student;
    private Credential credential;

    public Credential getCredential() {
        return this.credential;
    }

    public Person getPerson() {
        return person;
    }

    public Student getStudent() {
        return student;
    }
}

public class StudentRoute {

    public static void routes(Gson gson, DaoConnection connectionObj) {

        get("api/student", (req, res) -> {
            res.type("application/json");

            FetchStudentService service = new FetchStudentService(connectionObj.getConnection());

            try {

                List<Student> people = service.execute();

                JsonArray listaPessoasJson = new JsonArray();

                for (Student student : people) {
                    listaPessoasJson.add(gson.toJsonTree(student));
                }

                if (listaPessoasJson.size() == 0) {
                    res.status(200);
                    return gson.toJson(listaPessoasJson);
                }

                return gson.toJson(listaPessoasJson);

            } catch (Exception e) {
                res.status(500);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", "Erro ao buscar a lista de pessoas: " + e.getMessage());
                return gson.toJson(erro);
            }
        });

        get("api/student/:id", (req, res) -> {
            res.type("application/json");
            try {
                UUID id = UUID.fromString(req.params("id"));
                GetStudentByIdService service = new GetStudentByIdService(connectionObj.getConnection());
                Student student = service.execute(id);

                if (student == null) {
                    res.status(404);
                    JsonObject erro = new JsonObject();
                    erro.addProperty("erro", "Pessoa não encontrada!");
                    return gson.toJson(erro);
                }

                return gson.toJson(student);

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

        post("api/student", (req, res) -> {
            res.type("application/json");

            CreateStudentRequest bodyReq = gson.fromJson(req.body(), CreateStudentRequest.class);

            RegisterStudentService service = new RegisterStudentService(connectionObj.getConnection()); 

            try {
                Person person = new Person(
                        bodyReq.getPerson().getName(),
                        bodyReq.getPerson().getPhoneNumber(),
                        bodyReq.getPerson().getCpf(),
                        bodyReq.getPerson().getBirthDate(),
                        bodyReq.getPerson().getProfileUrl(),
                        bodyReq.getPerson().getDescription(),
                        null, 
                        bodyReq.getPerson().getPersonCode(),
                        "STUDENT"
                );

                Student student = new Student(
                    (UUID) person.getId(),
                    bodyReq.getStudent().getRegistrationNumber(),
                    bodyReq.getStudent().getCourse(),
                    bodyReq.getStudent().getSemester(),
                    bodyReq.getStudent().getShift(),
                    bodyReq.getStudent().getScholarshipType(),
                    bodyReq.getStudent().getAcademicStatus()
                );
            
                String hashedPassword = BCrypt.withDefaults().hashToString(12,  bodyReq.getCredential().getPassword().toCharArray());

                String refreshToken = null;
                Date refreshTokenExpiration = null;

                Credential credential = new Credential(
                    bodyReq.getCredential().getEmail(),
                    hashedPassword,
                    person.getId(),
                    refreshToken,
                    refreshTokenExpiration
                );

                service.execute(person, credential, student);


                JsonObject resposta = new JsonObject();
                res.type("application/json");
                res.status(201);
                resposta.addProperty("mensagem", "Pessoa criada com sucesso!");
                resposta.addProperty("id", student.getId().toString());
                return gson.toJson(resposta);

            } catch (Exception e) {
                res.status(400);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", e.getMessage());
                return gson.toJson(erro);
            }
        });

        put("api/student/:id", (req, res) -> {
            res.type("application/json");

            try {
                Student bodyReq = gson.fromJson(req.body(), Student.class);

                UpdateStudentService service = new UpdateStudentService(connectionObj.getConnection());

                Student student = service.execute(
                        bodyReq.getId(),
                        bodyReq.getRegistrationNumber(),
                        bodyReq.getCourse(),
                        bodyReq.getSemester(),
                        bodyReq.getShift(),
                        bodyReq.getScholarshipType(),
                        bodyReq.getAcademicStatus()
                );

                res.status(200);
                JsonObject resposta = new JsonObject();
                resposta.addProperty("mensagem", "Pessoa atualizada com sucesso!");
                resposta.addProperty("id", student.getId().toString());
                return gson.toJson(resposta);

            } catch (IllegalArgumentException e) {
                res.status(400);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", "ID no formato inválido.");
                return gson.toJson(erro);
            } catch (Exception e) {
                res.status(500);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", e.getMessage());
                return gson.toJson(erro);
            }
        });

        delete("api/student/:id", (req, res) -> {
            res.type("application/json");

            try {
                UUID id = UUID.fromString(req.params("id"));
                DeleteStudentByIdService service = new DeleteStudentByIdService(connectionObj.getConnection());

                Student student = service.execute(id);

                if (student == null) {
                    res.status(404);
                    JsonObject erro = new JsonObject();
                    erro.addProperty("erro", "Pessoa não encontrada para exclusão.");
                    return gson.toJson(erro);
                }

                res.status(200);
                JsonObject resposta = new JsonObject();
                resposta.addProperty("mensagem", "Pessoa excluída com sucesso!");
                resposta.addProperty("id", student.getId().toString());
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
