package service.users;

import java.sql.Connection;
import java.util.UUID;

import dao.AdminDao;
import model.Admin;

public class UpdateAdminService {
    private final AdminDao dao;

    public UpdateAdminService(Connection connection) {
        this.dao = new AdminDao(connection);
    }

    public Admin execute(
        UUID id,
        String jobTitle 
    ) throws Exception {

        try {
            Admin admin = dao.getById(id);
            if (admin == null) {
                throw new Exception("Usuario não encontrado");
            }  
            
            admin.setJobTitle(jobTitle);

            // Persiste no banco
            boolean updated = dao.save(admin);
            if (!updated) {
                throw new Exception("Falha ao editar estudante no banco.");
            }

            return admin;
        } catch (Exception e) {
            System.err.println("Erro em CreateAdminService: " + e.getMessage());
            throw e;
        }
    }
}
