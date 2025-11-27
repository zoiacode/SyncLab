package app;

import java.util.List;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import dao.DaoConnection;
import model.Person;
import service.users.CreatePersonService;
import service.users.DeletePersonByIdService;
import service.users.FetchPersonService;
import service.users.GetPersonByCpfService;
import service.users.GetPersonByIdService;
import service.users.UpdatePersonService;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

public class PersonRoute {
    public static void routes(Gson gson, DaoConnection connectionObj) {
        
        get("api/person", (req, res) -> {
            res.type("application/json");

            FetchPersonService service = new FetchPersonService(connectionObj.getConnection());

            try {
                
                List<Person> people = service.execute();

                
                JsonArray listaPessoasJson = new JsonArray();

                
                for (Person person : people) {
                    listaPessoasJson.add(gson.toJsonTree(person));
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
        
        
        get("api/person/:id", (req, res) -> {
            res.type("application/json");
            
            try {
                UUID id = UUID.fromString(req.params("id")); 
                GetPersonByIdService service = new GetPersonByIdService(connectionObj.getConnection());
                Person person = service.execute(id);

                if (person == null) {
                    res.status(404);
                    JsonObject erro = new JsonObject();
                    erro.addProperty("erro", "Pessoa não encontrada!");
                    return gson.toJson(erro);
                }

                return gson.toJson(person);

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
        
      
        
        get("api/person/cpf/:cpf", (req, res) -> {
            res.type("application/json");
            String cpfParam = req.params("cpf"); 

            GetPersonByCpfService service = new GetPersonByCpfService(connectionObj.getConnection());

            try {
                Person person = service.execute(cpfParam);

                if (person == null) {
                    res.status(404);
                    JsonObject erro = new JsonObject();
                    erro.addProperty("erro", "Pessoa não encontrada!");
                    return gson.toJson(erro);
                }

                return gson.toJson(person);

            } catch (Exception e) {
                res.status(400);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", e.getMessage());
                return gson.toJson(erro);
            }
        });
        
        
        
        post("api/person", (req, res) -> {
            res.type("application/json");

            Person bodyReq = gson.fromJson(req.body(), Person.class);

            
        
            CreatePersonService service = new CreatePersonService(connectionObj.getConnection());

            try {
                Person person = service.execute(
                        bodyReq.getName(),
                        bodyReq.getPhoneNumber(),
                        bodyReq.getCpf(),
                        bodyReq.getBirthDate(),
                        bodyReq.getProfileUrl(),
                        bodyReq.getDescription(),
                        null, 
                        bodyReq.getPersonCode(),
                        bodyReq.getRole()
                );

                res.status(201);
                return gson.toJson(person);

            } catch (Exception e) {
                res.status(400);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", e.getMessage());
                return gson.toJson(erro);
            }
        });

        
        put("api/person/:id", (req, res) -> {
            res.type("application/json");
            try {
                String bodyString = req.attribute("rawBody");
                if (bodyString == null || bodyString.isEmpty()) {
                    bodyString = req.body();
                }
                if (bodyString == null || bodyString.isEmpty() || bodyString.equals("null")) {
                    res.status(400);
                    JsonObject erro = new JsonObject();
                    erro.addProperty("erro", "Corpo da requisição ausente ou vazio. Verifique Content-Type no frontend.");
                    res.body(gson.toJson(erro)); // Define o corpo de erro
                    return res.body(); // Retorna o corpo (String JSON)
                }

                // Desserializa o JSON a partir da string armazenada/lida
                Person jsonReq = gson.fromJson(bodyString, Person.class);

                // Se a desserialização funcionar, o fluxo continua
                UpdatePersonService service = new UpdatePersonService(connectionObj.getConnection());

                Person person = service.execute(
                    jsonReq.getId(),
                    jsonReq.getName(),
                    jsonReq.getProfileUrl(),
                    jsonReq.getDescription(),
                    jsonReq.getPersonCode()
                );

                // CAMINHO DE SUCESSO
                res.status(200);
                JsonObject resposta = new JsonObject();
                resposta.addProperty("mensagem", "Pessoa atualizada com sucesso!");
                resposta.addProperty("id", person.getId().toString());
                resposta.addProperty("cpf", person.getCpf());
                
                res.body(gson.toJson(resposta)); // Define o corpo de sucesso
                return res.body(); // Retorna o corpo (String JSON)


            } catch (IllegalArgumentException e) {
                res.status(400);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", "ID no formato inválido.");
                return gson.toJson(erro);
            } catch (Exception e) {
                res.status(500);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", e.getMessage());
                e.printStackTrace();
                return gson.toJson(erro);
            }
        });


        delete("api/person/:id", (req, res) -> {
            res.type("application/json");
            
            try {
                UUID id = UUID.fromString(req.params("id")); 
                DeletePersonByIdService service = new DeletePersonByIdService(connectionObj.getConnection());
                
                Person person = service.execute(id);

                if (person == null) {
                    res.status(404);
                    JsonObject erro = new JsonObject();
                    erro.addProperty("erro", "Pessoa não encontrada para exclusão.");
                    return gson.toJson(erro);
                }

                res.status(200);
                JsonObject resposta = new JsonObject();
                resposta.addProperty("mensagem", "Pessoa excluída com sucesso!");
                resposta.addProperty("id", person.getId().toString());
                resposta.addProperty("cpf", person.getCpf());
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
