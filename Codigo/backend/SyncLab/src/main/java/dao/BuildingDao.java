package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import model.Building;

public class BuildingDao {
    private final Connection connection;

    public BuildingDao(Connection connection) {
        this.connection = connection;
    }

    public void create(Building building) throws SQLException {
        String sql = "INSERT INTO building (id, build_code, floor, campus, created_at, updated_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, building.getId());
            ps.setString(2, building.getBuildCode());
            ps.setInt(3, building.getFloor());
            ps.setString(4, building.getCampus());
            ps.setTimestamp(5, new java.sql.Timestamp(building.getCreatedAt().getTime()));
            ps.setTimestamp(6, new java.sql.Timestamp(building.getUpdatedAt().getTime()));
            ps.executeUpdate();
        }
    }

    public Building getById(UUID id) throws SQLException {
        String sql = "SELECT * FROM building WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapBuilding(rs);
            }
        }
        return null;
    }

    public List<Building> getAll() throws SQLException {
        List<Building> buildings = new ArrayList<>();
        String sql = "SELECT * FROM building";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                buildings.add(mapBuilding(rs));
            }
        }
        return buildings;
    }

    public void save(Building building) throws SQLException {
        String sql = "UPDATE building SET build_code = ?, floor = ?, campus = ?, " +
                     "created_at = ?, updated_at = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, building.getBuildCode());
            ps.setInt(2, building.getFloor());
            ps.setString(3, building.getCampus());
            ps.setTimestamp(4, new java.sql.Timestamp(building.getCreatedAt().getTime()));
            ps.setTimestamp(5, new java.sql.Timestamp(building.getUpdatedAt().getTime()));
            ps.setObject(6, building.getId());
            ps.executeUpdate();
        }
    }

    public void deleteById(UUID id) throws SQLException {
        String sql = "DELETE FROM building WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, id);
            ps.executeUpdate();
        }
    }

    private Building mapBuilding(ResultSet rs) throws SQLException {
        return new Building(
            (UUID) rs.getObject("id"),
            rs.getString("build_code"),
            rs.getInt("floor"),
            rs.getString("campus"),
            rs.getTimestamp("created_at"),
            rs.getTimestamp("updated_at")
        );
    }
}
