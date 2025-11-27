package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import model.Equipment;

public class EquipmentDao {
    private final Connection connection;

    public EquipmentDao(Connection connection) {
        this.connection = connection;
    }

    public boolean create(Equipment equipment) throws SQLException {
        String sql = "INSERT INTO equipment (id, name, description, quantity, status, max_loan_duration, image_url, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, equipment.getId());
            ps.setString(2, equipment.getName());
            ps.setString(3, equipment.getDescription());
            ps.setInt(4, equipment.getQuantity());
            ps.setString(5, equipment.getStatus());
            ps.setInt(6, equipment.getMaxLoanDuration());
            ps.setString(7, equipment.getImageUrl());
            ps.setTimestamp(8, new java.sql.Timestamp(equipment.getCreatedAt().getTime()));
            ps.setTimestamp(9, new java.sql.Timestamp(equipment.getUpdatedAt().getTime()));
            int lines = ps.executeUpdate();
            return lines > 0;
        }
    }

    public Equipment getById(UUID id) throws SQLException {
        String sql = "SELECT * FROM equipment WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapEquipment(rs);
            }
        }
        return null;
    }

    public List<Equipment> getAll() throws SQLException {
        List<Equipment> equipments = new ArrayList<>();
        String sql = "SELECT * FROM equipment";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                equipments.add(mapEquipment(rs));
            }
        }
        return equipments;
    }

    public boolean save(Equipment equipment) throws SQLException {
        String sql = "UPDATE equipment SET name = ?, description = ?, quantity = ?, status = ?, max_loan_duration = ?, image_url = ?, updated_at = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, equipment.getName());
            ps.setString(2, equipment.getDescription());
            ps.setInt(3, equipment.getQuantity());
            ps.setString(4, equipment.getStatus());
            ps.setInt(5, equipment.getMaxLoanDuration());
            ps.setString(6, equipment.getImageUrl());
            ps.setTimestamp(7, new java.sql.Timestamp(equipment.getUpdatedAt().getTime()));
            ps.setObject(8, equipment.getId());
            int lines = ps.executeUpdate(); 
            return lines > 0;
        }
    }

    public boolean deleteById(UUID id) throws SQLException {
        String sql = "DELETE FROM equipment WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, id);
            int lines = ps.executeUpdate();
            return lines > 0;
        }
    }

    private Equipment mapEquipment(ResultSet rs) throws SQLException {
        return new Equipment(
            (UUID) rs.getObject("id"),
            rs.getString("name"),
            rs.getString("description"),
            rs.getInt("quantity"),
            rs.getString("status"),
            rs.getInt("max_loan_duration"),
            rs.getString("image_url"),
            rs.getTimestamp("created_at"),
            rs.getTimestamp("updated_at")
        );
    }
}
