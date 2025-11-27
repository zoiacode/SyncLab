package app;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import at.favre.lib.crypto.bcrypt.BCrypt;
import dao.DaoConnection;
import dao.ProfessorDao;
import model.Credential;
import model.Person;
import model.Professor;
import service.users.DeleteProfessorByIdService;
import service.users.FetchProfessorService;
import service.users.GetProfessorByIdService;
import service.users.RegisterProfessorService;
import service.users.UpdateProfessorService;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

class CreateProfessorRequest {
    private Person person;
    private Professor professor;
    private Credential credential;
    private List<UUID> courses;

    public Person getPerson() {
        return person;
    }

    public Credential getCredential() {
        return credential;
    }

    public List<UUID> getCourses() {
        return courses;
    };

    public Professor getProfessor() {
        return professor;
    }
}

public class ProfessorRoute {

    public static void routes(Gson gson, DaoConnection connectionObj) {

        get("api/professor", (req, res) -> {
            res.type("application/json");

            FetchProfessorService service = new FetchProfessorService(connectionObj.getConnection());

            try {

                List<Professor> people = service.execute();

                JsonArray listaPessoasJson = new JsonArray();

                for (Professor professor : people) {
                    listaPessoasJson.add(gson.toJsonTree(professor));
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

        get("api/professor/:id", (req, res) -> {
            res.type("application/json");
            try {
                UUID id = UUID.fromString(req.params("id"));
                GetProfessorByIdService service = new GetProfessorByIdService(connectionObj.getConnection());
                Professor professor = service.execute(id);

                if (professor == null) {
                    res.status(404);
                    JsonObject erro = new JsonObject();
                    erro.addProperty("erro", "Pessoa não encontrada!");
                    return gson.toJson(erro);
                }

                return gson.toJson(professor);

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

        post("api/professor", (req, res) -> {
            res.type("application/json");

            CreateProfessorRequest bodyReq = gson.fromJson(req.body(), CreateProfessorRequest.class);

            RegisterProfessorService service = new RegisterProfessorService(connectionObj.getConnection());

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
                        "PROFESSOR"
                );

                Professor professor = new Professor(
                     (UUID) person.getId(),
                        bodyReq.getProfessor().getAcademicDegree(),
                        bodyReq.getProfessor().getExpertiseArea(),
                        bodyReq.getProfessor().getEmploymentStatus()
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

                service.execute(person, credential, professor);


                JsonObject resposta = new JsonObject();
                res.type("application/json");
                res.status(201);
                resposta.addProperty("mensagem", "Pessoa criada com sucesso!");
                resposta.addProperty("id", professor.getId().toString());
                return gson.toJson(resposta);

            
            } catch (Exception e) {
                res.status(400);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", e.getMessage());
                return gson.toJson(erro);
            }
        });

          get("api/professorn", (req, res) -> {
            res.type("application/json");

            ProfessorDao dao = new ProfessorDao(connectionObj.getConnection());
            try {

                List<Professor> people = dao.getAndNameAll();

                JsonArray listaPessoasJson = new JsonArray();

                for (Professor professor : people) {
                    listaPessoasJson.add(gson.toJsonTree(professor));
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

        put("api/professor/:id", (req, res) -> {
            res.type("application/json");

            try {
                Professor bodyReq = gson.fromJson(req.body(), Professor.class);

                UpdateProfessorService service = new UpdateProfessorService(connectionObj.getConnection());

                Professor professor = service.execute(
                        bodyReq.getId(),
                        bodyReq.getAcademicDegree(),
                        bodyReq.getExpertiseArea(),
                        bodyReq.getEmploymentStatus()
                );

                res.status(200);
                JsonObject resposta = new JsonObject();
                resposta.addProperty("mensagem", "Pessoa atualizada com sucesso!");
                resposta.addProperty("id", professor.getId().toString());
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

        delete("api/professor/:id", (req, res) -> {
            res.type("application/json");

            try {
                UUID id = UUID.fromString(req.params("id"));
                DeleteProfessorByIdService service = new DeleteProfessorByIdService(connectionObj.getConnection());

                Professor professor = service.execute(id);

                if (professor == null) {
                    res.status(404);
                    JsonObject erro = new JsonObject();
                    erro.addProperty("erro", "Pessoa não encontrada para exclusão.");
                    return gson.toJson(erro);
                }

                res.status(200);
                JsonObject resposta = new JsonObject();
                resposta.addProperty("mensagem", "Pessoa excluída com sucesso!");
                resposta.addProperty("id", professor.getId().toString());
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
