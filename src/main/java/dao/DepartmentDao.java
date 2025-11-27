package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import model.Department;

public class DepartmentDao {
    private final Connection connection;

    public DepartmentDao(Connection connection) {
        this.connection = connection;
    }

    public void create(Department department) throws SQLException {
        String sql = "INSERT INTO department (id, name, abbreviation, created_at, updated_at) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, department.getId());
            ps.setString(2, department.getName());
            ps.setString(3, department.getAbbreviation());
            ps.setTimestamp(4, new java.sql.Timestamp(department.getCreatedAt().getTime()));
            ps.setTimestamp(5, new java.sql.Timestamp(department.getUpdatedAt().getTime()));
            ps.executeUpdate();
        }
    }

    public Department getById(UUID id) throws SQLException {
        String sql = "SELECT * FROM department WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapDepartment(rs);
            }
        }
        return null;
    }

    public List<Department> getAll() throws SQLException {
        List<Department> departments = new ArrayList<>();
        String sql = "SELECT * FROM department";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                departments.add(mapDepartment(rs));
            }
        }
        return departments;
    }

    public void save(Department department) throws SQLException {
        String sql = "UPDATE department SET name = ?, abbreviation = ?, created_at = ?, updated_at = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, department.getName());
            ps.setString(2, department.getAbbreviation());
            ps.setTimestamp(3, new java.sql.Timestamp(department.getCreatedAt().getTime()));
            ps.setTimestamp(4, new java.sql.Timestamp(department.getUpdatedAt().getTime()));
            ps.setObject(5, department.getId());
            ps.executeUpdate();
        }
    }

    public void deleteById(UUID id) throws SQLException {
        String sql = "DELETE FROM department WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, id);
            ps.executeUpdate();
        }
    }

    private Department mapDepartment(ResultSet rs) throws SQLException {
        return new Department(
            (UUID) rs.getObject("id"),
            rs.getString("name"),
            rs.getString("abbreviation"),
            rs.getTimestamp("created_at"),
            rs.getTimestamp("updated_at")
        );
    }
}
