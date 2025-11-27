package  service.upload;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

public class UploadImageService {
    private final Cloudinary cloudinary;

    public UploadImageService() {      
      this.cloudinary = new Cloudinary("cloudinary://382339132186774:EsguUySfzHNv6PSKmDOIwxoweT8@dd1vsohhn");   
    }

    public String uploadFile(byte[] imageBytes) {
        if (imageBytes == null || imageBytes.length == 0) {
            System.err.println("Erro: Dados da imagem vazios.");
            return null;
        }

        String newFileName = "img_" + UUID.randomUUID().toString();

        Map<String, Object> params = ObjectUtils.asMap(
            newFileName, true,
            "unique_filename", false,
            "overwrite", true,
            // Adicionando um formato de recurso para o Cloudinary inferir o tipo
            "resource_type", "auto" 
        );
        
        String newUrl = null;

        try {
            Map result = cloudinary.uploader().upload(imageBytes, params);
            
            if (result.containsKey("url")) {
                newUrl = (String) result.get("url");
                System.out.println("Upload concluído com sucesso. Public ID: " + result.get("public_id"));
            } else {
                System.err.println("Erro: A chave 'url' não foi encontrada no resultado do upload.");
            }
        
        } catch (IOException e) {
            System.err.println("Erro de IO durante o upload: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro desconhecido durante o upload: " + e.getMessage());
        }

        return newUrl;
    }
}
