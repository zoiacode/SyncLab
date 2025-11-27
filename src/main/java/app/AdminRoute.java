package app;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import at.favre.lib.crypto.bcrypt.BCrypt;
import dao.DaoConnection;
import model.Admin;
import model.Credential;
import model.Person;
import service.users.DeleteAdminByIdService;
import service.users.FetchAdminService;
import service.users.GetAdminByIdService;
import service.users.RegisterAdminService;
import service.users.UpdateAdminService;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

class CreateAdminRequest {
    private Person person;
    private Admin admin;
    private Credential credential;

    public Person getPerson() {
        return person;
    }

    public Credential getCredential() {
        return credential;
    }

    public Admin getAdmin() {
        return admin;
    }
}

public class AdminRoute {

    public static void routes(Gson gson, DaoConnection connectionObj) {

        get("api/admin", (req, res) -> {
            res.type("application/json");

            FetchAdminService service = new FetchAdminService(connectionObj.getConnection());

            try {

                List<Admin> people = service.execute();

                JsonArray listaPessoasJson = new JsonArray();

                for (Admin admin : people) {
                    listaPessoasJson.add(gson.toJsonTree(admin));
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

        get("api/admin/:id", (req, res) -> {
            res.type("application/json");
            try {
                UUID id = UUID.fromString(req.params("id"));
                GetAdminByIdService service = new GetAdminByIdService(connectionObj.getConnection());
                Admin admin = service.execute(id);

                if (admin == null) {
                    res.status(404);
                    JsonObject erro = new JsonObject();
                    erro.addProperty("erro", "Pessoa não encontrada!");
                    return gson.toJson(erro);
                }

                return gson.toJson(admin);

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

        post("api/admin", (req, res) -> {
            res.type("application/json");

            CreateAdminRequest bodyReq = gson.fromJson(req.body(), CreateAdminRequest.class);

            RegisterAdminService service = new RegisterAdminService(connectionObj.getConnection());

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
                        "ADMIN"
                );

                Admin admin = new Admin(
                    (UUID) person.getId(),
                    bodyReq.getAdmin().getJobTitle()
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

                service.execute(person, credential, admin);


                JsonObject resposta = new JsonObject();
                res.type("application/json");
                res.status(201);
                resposta.addProperty("mensagem", "Pessoa criada com sucesso!");
                resposta.addProperty("id", admin.getId().toString());
                return gson.toJson(resposta);

            }
            
            catch (Exception e) {
                res.status(400);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", e.getMessage());
                return gson.toJson(erro);
            }
        });

        put("api/admin/:id", (req, res) -> {
            res.type("application/json");

            try {
                Admin bodyReq = gson.fromJson(req.body(), Admin.class);

                UpdateAdminService service = new UpdateAdminService(connectionObj.getConnection());

                Admin admin = service.execute(
                        bodyReq.getId(),
                        bodyReq.getJobTitle()
                );

                res.status(200);
                JsonObject resposta = new JsonObject();
                resposta.addProperty("mensagem", "Pessoa atualizada com sucesso!");
                resposta.addProperty("id", admin.getId().toString());
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

        delete("api/admin/:id", (req, res) -> {
            res.type("application/json");

            try {
                UUID id = UUID.fromString(req.params("id"));
                DeleteAdminByIdService service = new DeleteAdminByIdService(connectionObj.getConnection());

                Admin admin = service.execute(id);

                if (admin == null) {
                    res.status(404);
                    JsonObject erro = new JsonObject();
                    erro.addProperty("erro", "Pessoa não encontrada para exclusão.");
                    return gson.toJson(erro);
                }

                res.status(200);
                JsonObject resposta = new JsonObject();
                resposta.addProperty("mensagem", "Pessoa excluída com sucesso!");
                resposta.addProperty("id", admin.getId().toString());
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
