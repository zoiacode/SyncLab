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
import model.Person;
import model.Professor;

public class ProfessorDao {

    private final Connection connection;

    public ProfessorDao(Connection connection) {
        this.connection = connection;
    }

    public boolean create(Professor professor) throws SQLException {
        String sql = "INSERT INTO professor (id, person_id, academic_degree, expertise_area, employment_status, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, professor.getId());
            ps.setObject(2, professor.getPersonId());
            ps.setString(3, professor.getAcademicDegree());
            ps.setString(4, professor.getExpertiseArea());
            ps.setString(5, professor.getEmploymentStatus());
            ps.setTimestamp(6, new Timestamp(professor.getCreatedAt().getTime()));
            ps.setTimestamp(7, new Timestamp(professor.getUpdatedAt().getTime()));
            int affectedLines = ps.executeUpdate();
            return affectedLines > 0;
        }
    }

    public Professor getById(UUID id) throws SQLException {
        String sql = "SELECT * FROM professor WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapProfessor(rs);
            }
        }
        return null;
    }

    public UUID createProfessorTransactional(Person personObj, Credential credential, Professor professor) throws Exception {

    try {
        connection.setAutoCommit(false); 

        String sqlPerson = "INSERT INTO person (id, name, phone_number, cpf, birth_date, profile_url, description, desactivated_at, person_code, role, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, NULL, ?, 'PROFESSOR', NOW(), NOW());";

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

        String sqlProfessor = "INSERT INTO professor (id, person_id, academic_degree, expertise_area, employment_status, created_at, updated_at) VALUES (?, ?, ?, ?, ?, NOW(), NOW());";

        try (PreparedStatement ps = connection.prepareStatement(sqlProfessor)) {
            ps.setObject(1, professor.getId());
            ps.setObject(2, professor.getPersonId());
            ps.setString(3, professor.getAcademicDegree());
            ps.setString(4, professor.getExpertiseArea());
            ps.setString(5, professor.getEmploymentStatus());
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
        return professor.getId();

    } catch (Exception e) {
        connection.rollback();
        throw e;

    } finally {
        connection.setAutoCommit(true);
    }
}


   public Professor getByPersonId(UUID personId) throws SQLException {
    String sql = "SELECT * FROM professor WHERE person_id = ?";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setObject(1, personId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return mapProfessor(rs);
        }
    }
    return null;
}


    public List<Professor> getAll() throws SQLException {
        List<Professor> list = new ArrayList<>();
        String sql = "SELECT * FROM professor";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapProfessor(rs));
            }
        }
        return list;
    }

     public List<Professor> getAndNameAll() throws SQLException {
        List<Professor> list = new ArrayList<>();
        String sql = "SELECT p.*, pe.name AS professor_name FROM public.professor p JOIN public.person pe ON p.person_id = pe.id;";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapProfessorWName(rs));
            }
        }
        return list;
    }


    public boolean save(Professor professor) throws SQLException {
        String sql = "UPDATE professor SET person_id = ?, academic_degree = ?, expertise_area = ?, employment_status = ?, updated_at = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, professor.getPersonId());
            ps.setString(2, professor.getAcademicDegree());
            ps.setString(3, professor.getExpertiseArea());
            ps.setString(4, professor.getEmploymentStatus());
            ps.setTimestamp(5, new Timestamp(professor.getUpdatedAt().getTime()));
            ps.setObject(6, professor.getId());
            int linesTouched = ps.executeUpdate();
            return linesTouched > 0;
        }
    }

    public void deleteById(UUID id) throws SQLException {
        String sql = "DELETE FROM professor WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, id);
            ps.executeUpdate();
        }
    }

    private Professor mapProfessorWName(ResultSet rs) throws SQLException {
        Professor professor = new Professor(
            (UUID) rs.getObject("id"),
            (UUID) rs.getObject("person_id"),
            rs.getString("academic_degree"),
            rs.getString("expertise_area"),
            rs.getString("employment_status"),
            rs.getTimestamp("created_at"),
            rs.getTimestamp("updated_at")
        );

        // O nome vem da coluna com alias 'professor_name' na sua query SQL.
        professor.setName(rs.getString("professor_name"));

        return professor;
    }

    private Professor mapProfessor(ResultSet rs) throws SQLException {
        return new Professor(
            (UUID) rs.getObject("id"),
            (UUID) rs.getObject("person_id"),
            rs.getString("academic_degree"),
            rs.getString("expertise_area"),
            rs.getString("employment_status"),
            rs.getTimestamp("created_at"),
            rs.getTimestamp("updated_at")
        );
    }
}
