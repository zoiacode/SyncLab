package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import model.Checkin;

public class CheckinDao {
    private final Connection connection;

    public CheckinDao(Connection connection) {
        this.connection = connection;
    }

    public void create(Checkin checkin) throws SQLException {
        String sql = "INSERT INTO checkin (id, student_id, is_confirmed, lecture_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, checkin.getId());
            ps.setObject(2, checkin.getstudentId());
            ps.setBoolean(3, checkin.isConfirmed());
            ps.setObject(4, checkin.getLectureId());
            ps.setTimestamp(5, new java.sql.Timestamp(checkin.getCreatedAt().getTime()));
            ps.setTimestamp(6, new java.sql.Timestamp(checkin.getUpdatedAt().getTime()));
            ps.executeUpdate();
        }
    }

    public Checkin getById(UUID id) throws SQLException {
        String sql = "SELECT * FROM checkin WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapCheckin(rs);
            }
        }
        return null;
    }

    public List<Checkin> getAll() throws SQLException {
        List<Checkin> checkins = new ArrayList<>();
        String sql = "SELECT * FROM checkin";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                checkins.add(mapCheckin(rs));
            }
        }
        return checkins;
    }

    public void save(Checkin checkin) throws SQLException {
        String sql = "UPDATE checkin SET student_id = ?, timestamp = ?, is_confirmed = ?, lecture_id = ?, created_at = ?, updated_at = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, checkin.getstudentId());
            ps.setTimestamp(2, new java.sql.Timestamp(checkin.getTimestamp().getTime()));
            ps.setBoolean(3, checkin.isConfirmed());
            ps.setObject(4, checkin.getLectureId());
            ps.setTimestamp(5, new java.sql.Timestamp(checkin.getCreatedAt().getTime()));
            ps.setTimestamp(6, new java.sql.Timestamp(checkin.getUpdatedAt().getTime()));
            ps.setObject(7, checkin.getId());
            ps.executeUpdate();
        }
    }

    public void deleteById(UUID id) throws SQLException {
        String sql = "DELETE FROM checkin WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, id);
            ps.executeUpdate();
        }
    }

    private Checkin mapCheckin(ResultSet rs) throws SQLException {
        return new Checkin(
            (UUID) rs.getObject("id"),
            (UUID) rs.getObject("student_id"),
            rs.getTimestamp("timestamp"),
            rs.getBoolean("is_confirmed"),
            (UUID) rs.getObject("lecture_id"),
            rs.getTimestamp("created_at"),
            rs.getTimestamp("updated_at")
        );
    }
}
