package service.users;
import java.sql.Connection;
import java.util.List;

import dao.AdminDao;
import model.Person;
import model.Admin;


public class FetchAdminService {
	private final AdminDao dao;
    public FetchAdminService(Connection connection) {
        this.dao = new AdminDao(connection);
    }
    public List<Admin> execute() throws Exception {
        try {
            List<Admin> adminArr = this.dao.getAll();
            return adminArr;
        } catch (Exception err) {
            System.out.println(err);
            return null;
        }
    }
}
