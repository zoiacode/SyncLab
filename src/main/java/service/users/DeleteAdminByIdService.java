package service.users;
import java.sql.Connection;
import java.util.UUID;

import dao.AdminDao;
import model.Admin;

public class DeleteAdminByIdService {
	private final AdminDao dao;
    public DeleteAdminByIdService(Connection connection) {
        this.dao = new AdminDao(connection);
    }
    public Admin execute(UUID id) throws Exception {
        try {
            Admin adminRef = this.dao.getById(id);
            
            if(adminRef == null) {
                throw new Exception("Admin not found!");
            }

            this.dao.deleteById(id);

            return adminRef;
        } catch (Exception err) {
            System.out.println(err);
            return null;
        }
    }
}
