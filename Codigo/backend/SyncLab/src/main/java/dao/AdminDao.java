package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import model.Admin;
import model.Credential;
import model.Person;

public class AdminDao {
    private final Connection connection;

    public AdminDao(Connection connection) {
        this.connection = connection;
    }
    public boolean create(Admin admin) throws SQLException {
        String sql = "INSERT INTO administrator (id, person_id, job_title, created_at, updated_at) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setObject(1, admin.getId());
        ps.setObject(2, admin.getPersonId());
        ps.setString(3, admin.getJobTitle());
        ps.setTimestamp(4, new java.sql.Timestamp(admin.getCreatedAt().getTime()));
        ps.setTimestamp(5, new java.sql.Timestamp(admin.getUpdatedAt().getTime()));
        int touchedLines = ps.executeUpdate();
        return touchedLines > 0;
    }

    public Admin getById(UUID id) throws SQLException {
        String sql = "SELECT * FROM administrator WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setObject(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return mapAdmin(rs);
        }
        return null;
    }

        public Admin getByPersonId(UUID personId) throws SQLException {
        String sql = "SELECT * FROM administrator WHERE person_id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setObject(1, personId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return mapAdmin(rs);
        }
        return null;
    }

    public UUID createAdminTransactional(Person personObj, Credential credential, Admin admin) throws Exception {

    try {
        connection.setAutoCommit(false); // BEGIN

        String sqlPerson = "INSERT INTO person (id, name, phone_number, cpf, birth_date, profile_url, description, desactivated_at, person_code, role, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, NULL, ?, 'ADMIN', NOW(), NOW());";

        try (PreparedStatement ps = connection.prepareStatement(sqlPerson)) {
            ps.setObject(1, personObj.getId());
            ps.setString(2, personObj.getName());
            ps.setString(3, personObj.getPhoneNumber());
            ps.setString(4, personObj.getCpf());
            ps.setDate(5, new java.sql.Date(personObj.getBirthDate().getTime()));
            ps.setString(6, personObj.getProfileUrl());
            ps.setString(7, personObj.getDescription());
            ps.setString(8, personObj.getPersonCode());
            ps.executeUpdate();
        }

        String sqlAdmin = "INSERT INTO administrator (id, person_id, job_title, created_at, updated_at) VALUES (?, ?, ?, NOW(), NOW());";

        try (PreparedStatement ps = connection.prepareStatement(sqlAdmin)) {
            ps.setObject(1, admin.getId());
            ps.setObject(2, admin.getPersonId());
            ps.setString(3, admin.getJobTitle());
            ps.executeUpdate();
        }

        String sqlCredential = "INSERT INTO credential (id, email, password, created_at, updated_at, person_id) VALUES (?, ?, ?, NOW(), NOW(), ?);";

        try (PreparedStatement ps = connection.prepareStatement(sqlCredential)) {
            ps.setObject(1, credential.getId());
            ps.setString(2, credential.getEmail());
            ps.setString(3, credential.getPassword());
            ps.setObject(4, credential.getPersonId());
            ps.executeUpdate();
        }

        connection.commit();
        return admin.getId();

    } catch (Exception e) {
        e.printStackTrace();
        connection.rollback(); // desfaz tudo
        throw e;
    } finally {
        connection.setAutoCommit(true);
    }
}


    public List<Admin> getAll() throws SQLException {
        List<Admin> admins = new ArrayList<>();
        String sql = "SELECT * FROM administrator";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            admins.add(mapAdmin(rs));
        }
        return admins;
    }

    public boolean save(Admin admin) throws SQLException {
        String sql = "UPDATE administrator SET id = ?, person_id = ?, job_title = ?, created_at = ?, updated_at = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setObject(1, admin.getId());
        ps.setObject(2, admin.getPersonId());
        ps.setString(3, admin.getJobTitle());
        ps.setTimestamp(4, new java.sql.Timestamp(admin.getCreatedAt().getTime()));
        ps.setTimestamp(5, new java.sql.Timestamp(admin.getUpdatedAt().getTime()));
        int linesChanged = ps.executeUpdate();
        return linesChanged > 0;
    }

    public void deleteById(UUID id) throws SQLException {
        String sql = "DELETE FROM administrator WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setObject(1, id);
        ps.executeUpdate();
    }

    private Admin mapAdmin(ResultSet rs) throws SQLException {
        Admin ad = new Admin(
            (UUID) rs.getObject("id"),
            (UUID) rs.getObject("person_id"),
            rs.getString("job_title"),
            rs.getTimestamp("created_at"),
            rs.getTimestamp("updated_at")
        );
        
        return ad;
    }
}
