package app;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import service.upload.UploadImageService;
import static spark.Spark.post;

public class UploadRoute {

    public static void routes(Gson gson) {

        post("api/upload", (req, res) -> {
            res.type("application/json");

            try {
                // Spark requer configuração para aceitar multipart/form-data
                req.attribute("org.eclipse.jetty.multipartConfig", new javax.servlet.MultipartConfigElement("/temp"));

                // Obtém o arquivo enviado
                try (InputStream is = req.raw().getPart("image").getInputStream()) {

                    // Converte InputStream -> byte[]
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                    byte[] data = new byte[1024];
                    int nRead;
                    while ((nRead = is.read(data, 0, data.length)) != -1) {
                        buffer.write(data, 0, nRead);
                    }
                    byte[] imageBytes = buffer.toByteArray();

                    // Faz o upload no Cloudinary
                    UploadImageService uploadService = new UploadImageService();
                    String uploadedUrl = uploadService.uploadFile(imageBytes);

                    if (uploadedUrl == null) {
                        res.status(500);
                        JsonObject erro = new JsonObject();
                        erro.addProperty("erro", "Falha ao enviar imagem para o Cloudinary");
                        return gson.toJson(erro);
                    }

                    // Retorna URL pública
                    JsonObject response = new JsonObject();
                    response.addProperty("url", uploadedUrl);
                    response.addProperty("mensagem", "Upload realizado com sucesso");

                    return gson.toJson(response);
                }

            } catch (Exception e) {
                e.printStackTrace();
                res.status(500);
                JsonObject erro = new JsonObject();
                erro.addProperty("erro", "Erro ao processar upload: " + e.getMessage());
                return gson.toJson(erro);
            }
        });
    }
}
