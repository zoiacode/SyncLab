package service.users;
import java.sql.Connection;
import java.util.UUID;

import dao.AdminDao;
import model.Admin;


public class GetAdminByIdService {
	private final AdminDao dao;
    public GetAdminByIdService(Connection connection) {
        this.dao = new AdminDao(connection);
    }
    public Admin execute(UUID adminId) throws Exception {
        try {
            Admin adminRef = this.dao.getById(adminId);
    
            if(adminRef == null) {
                throw new Exception("Admin not found!");
            }

            return adminRef;
        } catch (Exception err) {
            System.out.println(err);
            return null;
        }
    }
}
