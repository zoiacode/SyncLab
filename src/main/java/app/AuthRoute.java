package app;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import dao.DaoConnection;
import model.Admin;
import model.Credential;
import model.Person;
import model.Professor;
import model.Student;
import service.auth.LoginService;
import service.auth.LoginService.AuthResponse;
import service.auth.RegisterCredentialService;
import static spark.Spark.post;
import util.JwtEncap;

class CreateAuthRequest {
    private Person person;
    private Student student;
    private Admin admin;
    private Professor professor;
    private Credential credential;

    public Person getPerson() { return person; }
    public Student getStudent() { return student; }
    public Admin getAdmin() { return admin; }
    public Professor getProfessor() { return professor; }
    public Credential getCredential() { return credential; }
}

public class AuthRoute {
    public static void routes(Gson gson, DaoConnection connectionObj) {
        post("auth/register", (req, res) -> {
            res.type("application/json");
            CreateAuthRequest bodyReq = gson.fromJson(req.body(), CreateAuthRequest.class);
            RegisterCredentialService service = new RegisterCredentialService(connectionObj.getConnection());

            try {
                service.execute(
                    bodyReq.getCredential().getEmail(),
                    bodyReq.getCredential().getPassword(),
                    bodyReq.getPerson().getId()
                );
                JsonObject resposta = new JsonObject();
                resposta.addProperty("mensagem", "Credenciais cadastradas com sucesso!");
                return gson.toJson(resposta);
            } catch (Exception e) {
                res.status(400);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", e.getMessage());
                return gson.toJson(erro);
            }
        });

    post("auth/login", (req, res) -> {
    res.type("application/json");
    CreateAuthRequest bodyReq = gson.fromJson(req.body(), CreateAuthRequest.class);
    LoginService service = new LoginService(connectionObj.getConnection());

    try {
        AuthResponse response = service.execute(
            bodyReq.getCredential().getEmail(),
            bodyReq.getCredential().getPassword()
        );
        
        String accessTokenCookie = String.format(
            "access_token=%s; Max-Age=%d; Path=/; SameSite=None",
            response.getAccessToken(),
            86400
        );

        String refreshTokenCookie = String.format(
            "refresh_token=%s; Max-Age=%d; Path=/; SameSite=None",
            response.getRefreshToken(),
            604800
        );

        JwtEncap jwtEncap = new JwtEncap(accessTokenCookie, refreshTokenCookie);

        JsonObject resposta = new JsonObject();
        
        resposta.add("token", gson.toJsonTree(jwtEncap));
        return gson.toJson(resposta);
    } catch (Exception e) {
        res.status(400);
        JsonObject erro = new JsonObject();
        erro.addProperty("erro", e.getMessage());
        return gson.toJson(erro);
    }
});
        post("auth/logout", (req, res) -> {
            res.type("application/json");
            try {
                res.removeCookie("/", "access_token");
                res.removeCookie("/", "refresh_token");
                JsonObject resposta = new JsonObject();
                resposta.addProperty("mensagem", "Logout realizado com sucesso!");
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