package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import model.Person;

public class PersonDao {

    private final Connection connection;

    public PersonDao(Connection connection) {
        this.connection = connection;
    }

    public Person getById(UUID id) {
        String sql = "SELECT * FROM person WHERE id = ?";
        return querySinglePerson(sql, id);
    }

    public Person getByCpf(String cpf) {
        String sql = "SELECT * FROM person WHERE cpf = ?";
        return querySinglePerson(sql, cpf);
    }

    public Person getByNumber(String phoneNumber) {
        String sql = "SELECT * FROM person WHERE phone_number = ?";
        return querySinglePerson(sql, phoneNumber);
    }

    public List<Person> getAll() {
        List<Person> lista = new ArrayList<>();
        String sql = "SELECT * FROM person";
        try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapResultSetToPerson(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar todas as pessoas: " + e.getMessage());
        }
        return lista;
    }

    public boolean create(Person person) {
        String sql = "INSERT INTO person ( id, created_at, updated_at, name, phone_number, cpf, birth_date, profile_url, description, desactivated_at, person_code, role) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, person.getId());
            ps.setTimestamp(2, new java.sql.Timestamp(person.getCreatedAt().getTime()));
            ps.setTimestamp(3, new java.sql.Timestamp(person.getUpdatedAt().getTime()));
            ps.setString(4, person.getName());
            ps.setString(5, person.getPhoneNumber());
            ps.setString(6, person.getCpf());

            if (person.getBirthDate() != null) {
                ps.setDate(7, new java.sql.Date(person.getBirthDate().getTime())); 
            }else {
                ps.setNull(7, java.sql.Types.DATE);
            }

            ps.setString(8, person.getProfileUrl());
            ps.setString(9, person.getDescription());

            if (person.getDesactivatedAt() != null) {
                ps.setTimestamp(10, new java.sql.Timestamp(person.getDesactivatedAt().getTime())); 
            }else {
                ps.setNull(10, java.sql.Types.TIMESTAMP);
            }

            ps.setString(11, person.getPersonCode());
            ps.setString(12, person.getRole());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir pessoa: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteById(UUID id) {
        String sql = "DELETE FROM person WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao deletar pessoa: " + e.getMessage());
            return false;
        }
    }

    public boolean save(Person person) {
        String sql = "UPDATE person SET updated_at = ?, name = ?, phone_number = ?, cpf = ?, birth_date = ?, profile_url = ?, description = ?, desactivated_at = ?, person_code = ?, role = ? WHERE id = ?;";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(11, person.getId());
            ps.setTimestamp(1, new java.sql.Timestamp(person.getUpdatedAt().getTime()));
            ps.setString(2, person.getName());
            ps.setString(3, person.getPhoneNumber());
            ps.setString(4, person.getCpf());

            if (person.getBirthDate() != null) {
                ps.setDate(5, new java.sql.Date(person.getBirthDate().getTime())); 
            }else {
                ps.setNull(5, java.sql.Types.DATE);
            }

            ps.setString(6, person.getProfileUrl());
            ps.setString(7, person.getDescription());

            if (person.getDesactivatedAt() != null) {
                ps.setTimestamp(8, new java.sql.Timestamp(person.getDesactivatedAt().getTime())); 
            }else {
                ps.setNull(8, java.sql.Types.TIMESTAMP);
            }

            ps.setString(9, person.getPersonCode());

            ps.setString(10, person.getRole());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir pessoa: " + e.getMessage());
            return false;
        }
    }

    private Person querySinglePerson(String sql, Object param) {
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, param);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToPerson(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erro na consulta: " + e.getMessage());
        }
        return null;
    }

    private Person mapResultSetToPerson(ResultSet rs) throws SQLException {
        return new Person(
                (UUID) rs.getObject("id"),
                rs.getTimestamp("created_at"),
                rs.getTimestamp("updated_at"),
                rs.getString("name"),
                rs.getString("phone_number"),
                rs.getString("cpf"),
                rs.getDate("birth_date"),
                rs.getString("profile_url"),
                rs.getString("description"),
                rs.getTimestamp("desactivated_at"),
                rs.getString("person_code"),
                rs.getString("role")
        );
    }
}
