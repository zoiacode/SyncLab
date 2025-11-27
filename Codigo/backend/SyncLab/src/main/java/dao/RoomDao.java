package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import model.Room;

public class RoomDao {

    private final Connection connection;

    public RoomDao(Connection connection) {
        this.connection = connection;
    }

    public boolean create(Room room) throws SQLException {
        String sql = "INSERT INTO room (id, capacity, room_type, status, image_url, building_id, floor, room_code, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, room.getId());
            
            ps.setInt(2, room.getCapacity());
            ps.setString(3, room.getRoomType());
            ps.setString(4, room.getStatus());
            ps.setString(5, room.getImageUrl());
            ps.setObject(6, room.getBuildingId());
            ps.setInt(7, room.getFloor());
            ps.setString(8, room.getCode());
            ps.setTimestamp(9, new Timestamp(room.getCreatedAt().getTime()));
            ps.setTimestamp(10, new Timestamp(room.getUpdatedAt().getTime()));
            int lines = ps.executeUpdate();
            return lines > 0;
        }
    }

    public Room getById(UUID id) throws SQLException {
        String sql = "SELECT * FROM room WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRoom(rs);
            }
        }
        return null;
    }

    public List<Room> getAll() throws SQLException {
        List<Room> list = new ArrayList<>();
        String sql = "SELECT * FROM room";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRoom(rs));
            }
        }
        return list;
    }

    public boolean save(Room room) throws SQLException {
        int lines = 0;
        
        String sql = "UPDATE room SET capacity = ?, room_type = ?, status = ?, image_url = ?, building_id = ?, floor = ?, updated_at = ?, room_code = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, room.getCapacity());
            ps.setString(2, room.getRoomType());
            ps.setString(3, room.getStatus());
            ps.setString(4, room.getImageUrl());
            ps.setObject(5, room.getBuildingId());
            ps.setInt(6, room.getFloor());
            ps.setTimestamp(7, new Timestamp(room.getUpdatedAt().getTime()));
            ps.setString(8, room.getCode());
            ps.setObject(9, room.getId());
            lines = ps.executeUpdate();
        } catch(Exception e) {
            e.printStackTrace();
        }
         return lines > 0;
    }

    public boolean deleteById(UUID id) throws SQLException {
        String sql = "DELETE FROM room WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, id);
            int lines = ps.executeUpdate();
            return lines > 0;
        }
    }

    private Room mapRoom(ResultSet rs) throws SQLException {
        return new Room(
            (UUID) rs.getObject("id"),
            rs.getInt("capacity"),
            rs.getString("room_type"),
            rs.getString("room_code"),
            rs.getString("status"),
            rs.getString("image_url"),
            (UUID) rs.getObject("building_id"),
            rs.getInt("floor"),
            rs.getTimestamp("created_at"),
            rs.getTimestamp("updated_at")
        );
    }
}
