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

import model.Notification;

public class NotificationDao {

    private final Connection connection;

    public NotificationDao(Connection connection) {
        this.connection = connection;
    }

    public void create(Notification notification) throws SQLException {
        String sql = "INSERT INTO notification (id, person_id, title, message, sent_at, read_at, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, notification.getId());
            ps.setObject(2, notification.getPersonId());
            ps.setString(3, notification.getTitle());
            ps.setString(4, notification.getMessage());
            ps.setTimestamp(5, new Timestamp(notification.getSentAt().getTime()));
            if (notification.getReadAt() != null) {
                ps.setTimestamp(6, new Timestamp(notification.getReadAt().getTime()));
            } else {
                ps.setTimestamp(6, null);
            }
            ps.setTimestamp(7, new Timestamp(notification.getCreatedAt().getTime()));
            ps.setTimestamp(8, new Timestamp(notification.getUpdatedAt().getTime()));
            ps.executeUpdate();
        }
    }

    public Notification getById(UUID id) throws SQLException {
        String sql = "SELECT * FROM notification WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapNotification(rs);
            }
        }
        return null;
    }

    public List<Notification> getAll() throws SQLException {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM notification";
        try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                notifications.add(mapNotification(rs));
            }
        }
        return notifications;
    }

    public void save(Notification notification) throws SQLException {
        String sql = "UPDATE notification SET person_id = ?, title = ?, message = ?, sent_at = ?, read_at = ?, updated_at = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, notification.getPersonId());
            ps.setString(2, notification.getTitle());
            ps.setString(3, notification.getMessage());
            ps.setTimestamp(4, new Timestamp(notification.getSentAt().getTime()));
            if (notification.getReadAt() != null) {
                ps.setTimestamp(5, new Timestamp(notification.getReadAt().getTime()));
            } else {
                ps.setTimestamp(5, null);
            }
            ps.setTimestamp(6, new Timestamp(notification.getUpdatedAt().getTime()));
            ps.setObject(7, notification.getId());
            ps.executeUpdate();
        }
    }

    public void deleteById(UUID id) throws SQLException {
        String sql = "DELETE FROM notification WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, id);
            ps.executeUpdate();
        }
    }

    public List<Notification> getAllByPersonId(UUID personId) throws SQLException {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM notification WHERE person_id = ? ORDER BY created_at DESC";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, personId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    notifications.add(mapNotification(rs));
                }
            }
        }
        return notifications;
    }

    public List<Notification> getAllPublic() throws SQLException {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM notification WHERE person_id IS NULL ORDER BY created_at DESC";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                notifications.add(mapNotification(rs));
            }
        }
        return notifications;
    }

    public List<Notification> getAllVisibleForPerson(UUID personId) throws SQLException {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM notification WHERE person_id IS NULL OR person_id = ? ORDER BY created_at DESC";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, personId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    notifications.add(mapNotification(rs));
                }
            }
        }
        return notifications;
    }

    private Notification mapNotification(ResultSet rs) throws SQLException {
        return new Notification(
                (UUID) rs.getObject("id"),
                (UUID) rs.getObject("person_id"),
                rs.getString("title"),
                rs.getString("message"),
                rs.getTimestamp("sent_at"),
                rs.getTimestamp("read_at"),
                rs.getTimestamp("created_at"),
                rs.getTimestamp("updated_at")
        );
    }
}
