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
import model.Student;

public class StudentDao {

    private final Connection connection;

    public StudentDao(Connection connection) {
        this.connection = connection;
    }

    public boolean create(Student student) throws SQLException {
        String sql = "INSERT INTO student (id, person_id, registration_number, semester, shift, scholarship_type, academic_status, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, student.getId());
            ps.setObject(2, student.getPersonId());
            ps.setString(3, student.getRegistrationNumber());
            ps.setString(4, student.getSemester());
            ps.setString(5, student.getShift());
            ps.setString(6, student.getScholarshipType());
            ps.setString(7, student.getAcademicStatus());
            ps.setTimestamp(8, new Timestamp(student.getCreatedAt().getTime()));
            ps.setTimestamp(9, new Timestamp(student.getUpdatedAt().getTime()));

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }

       public Student getByPersonId(UUID personId) throws SQLException {
        String sql = "SELECT * FROM student WHERE person_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, personId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapStudent(rs);
            }
        }
        return null;
    }

    public Student getById(UUID id) throws SQLException {
        String sql = "SELECT * FROM student WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapStudent(rs);
            }
        }
        return null;
    }

    public List<Student> getAll() throws SQLException {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM student";
        try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapStudent(rs));
            }
        }
        return list;
    }
    
    public UUID createStudentTransactional(Person personObj, Credential credential, Student student) throws Exception {

    try {
        connection.setAutoCommit(false);

        String sqlPerson = "INSERT INTO person (id, name, phone_number, cpf, birth_date, profile_url, description, desactivated_at, person_code, role, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, NULL, ?, 'STUDENT', NOW(), NOW());";

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

        String sqlStudent = "INSERT INTO student (id, person_id, registration_number, course, semester, shift, scholarship_type, academic_status, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW());";

        try (PreparedStatement ps = connection.prepareStatement(sqlStudent)) {
            ps.setObject(1, student.getId());
            ps.setObject(2, student.getPersonId());
            ps.setString(3, student.getRegistrationNumber());
            ps.setString(4, student.getCourse());
            ps.setString(5, student.getSemester());
            ps.setString(6, student.getShift());
            ps.setString(7, student.getScholarshipType());
            ps.setString(8, student.getAcademicStatus());
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
        return student.getId();

    } catch (Exception e) {
        connection.rollback();
        throw e;
    } finally {
        connection.setAutoCommit(true);
    }
    }


    public boolean save(Student student) throws SQLException {
        String sql = "UPDATE student SET person_id = ?, registration_number = ?, semester = ?, shift = ?, scholarship_type = ?, academic_status = ?, updated_at = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, student.getPersonId());
            ps.setString(2, student.getRegistrationNumber());
            ps.setString(3, student.getSemester());
            ps.setString(4, student.getShift());
            ps.setString(5, student.getScholarshipType());
            ps.setString(6, student.getAcademicStatus());
            ps.setTimestamp(7, new Timestamp(student.getUpdatedAt().getTime()));
            ps.setObject(8, student.getId());
            int linesTouched = ps.executeUpdate();
            return linesTouched > 0;
        }
    }

    public void deleteById(UUID id) throws SQLException {
        String sql = "DELETE FROM student WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, id);
            ps.executeUpdate();
        }
    }

    private Student mapStudent(ResultSet rs) throws SQLException {
        return new Student(
                (UUID) rs.getObject("id"),
                (UUID) rs.getObject("person_id"),
                rs.getString("registration_number"),
                null,
                rs.getString("semester"),
                rs.getString("shift"),
                rs.getString("scholarship_type"),
                rs.getString("academic_status"),
                rs.getTimestamp("created_at"),
                rs.getTimestamp("updated_at")
        );
    }
}
