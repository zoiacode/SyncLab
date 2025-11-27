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

import model.Credential;

public class CredentialDao {
    private final Connection connection;

    public CredentialDao(Connection connection) {
        this.connection = connection;
    }

    public void create(Credential credential) throws SQLException {
        String sql = "INSERT INTO credential (id, email, password, person_id, refresh_token, refresh_token_expiration, created_at, updated_at) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, credential.getId());
            ps.setString(2, credential.getEmail());
            ps.setString(3, credential.getPassword());
            ps.setObject(4, credential.getPersonId());
            ps.setString(5, credential.getRefreshToken());
            ps.setTimestamp(6, credential.getRefreshTokenExpiration() != null ? new Timestamp(credential.getRefreshTokenExpiration().getTime()) : null);
            ps.setTimestamp(7, new Timestamp(credential.getCreatedAt().getTime()));
            ps.setTimestamp(8, new Timestamp(credential.getUpdatedAt().getTime()));
            ps.executeUpdate();
        }
    }

    public Credential getById(UUID id) throws SQLException {
        String sql = "SELECT * FROM credential WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapCredential(rs);
            }
        }
        return null;
    }

     public Credential getByRefreshToken(String refreshToken) throws SQLException {
        String sql = "SELECT * FROM credential WHERE refresh_token = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, refreshToken);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapCredential(rs);
                }
            }
        }
        return null;
    }

    public Credential getByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM credential WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapCredential(rs);
            }
        }
        return null;
    }

    public Credential getByPersonId(UUID personId) throws SQLException {
        String sql = "SELECT * FROM credential WHERE person_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, personId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapCredential(rs);
            }
        }
        return null;
    }

    public List<Credential> getAll() throws SQLException {
        List<Credential> credentials = new ArrayList<>();
        String sql = "SELECT * FROM credential";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                credentials.add(mapCredential(rs));
            }
        }
        return credentials;
    }

    public void save(Credential credential) throws SQLException {
        String sql = "UPDATE credential SET email = ?, password = ?, person_id = ?, refresh_token = ?, refresh_token_expiration = ?, updated_at = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, credential.getEmail());
            ps.setString(2, credential.getPassword());
            ps.setObject(3, credential.getPersonId());
            ps.setString(4, credential.getRefreshToken());
            ps.setTimestamp(5, credential.getRefreshTokenExpiration() != null ? new Timestamp(credential.getRefreshTokenExpiration().getTime()) : null);
            ps.setTimestamp(6, new Timestamp(credential.getUpdatedAt().getTime()));
            ps.setObject(7, credential.getId());
            ps.executeUpdate();
        }
    }

    public void updateRefreshToken(UUID credentialId, String refreshToken, Timestamp expiration) throws SQLException {
        String sql = "UPDATE credential SET refresh_token = ?, refresh_token_expiration = ?, updated_at = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, refreshToken);
            ps.setTimestamp(2, expiration);
            ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            ps.setObject(4, credentialId);
            ps.executeUpdate();
        }
    }

    public void deleteById(UUID id) throws SQLException {
        String sql = "DELETE FROM credential WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, id);
            ps.executeUpdate();
        }
    }

    private Credential mapCredential(ResultSet rs) throws SQLException {
        return new Credential(
            (UUID) rs.getObject("id"),
            rs.getString("email"),
            rs.getString("password"),
            (UUID) rs.getObject("person_id"),
            rs.getString("refresh_token"),
            rs.getTimestamp("refresh_token_expiration"),
            rs.getTimestamp("created_at"),
            rs.getTimestamp("updated_at")
        );
    }
}
