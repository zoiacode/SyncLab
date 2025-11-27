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

import model.Lecture;
import util.StudentInClass;
import util.valueObject.DateHour;
import util.valueObject.TypeObj;

public class LectureDao {

    private final Connection connection;

    public LectureDao(Connection connection) {
        this.connection = connection;
    }

    public void create(Lecture lecture) throws SQLException {
        String sql = "INSERT INTO lecture (id, subject_name, professor_id, room_id, end_date, date, created_at, updated_at, student_quantity, lecture_type, course_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, lecture.getId());
            ps.setString(2, lecture.getSubjectName());
            ps.setObject(3, lecture.getProfessorId());
            ps.setObject(4, lecture.getRoomId());
            ps.setTimestamp(5, lecture.getEndDate() == null ? null : new Timestamp(lecture.getEndDate().getTime()));
            ps.setString(6, lecture.getDate().getValue());
            ps.setTimestamp(7, new Timestamp(lecture.getCreatedAt().getTime()));
            ps.setTimestamp(8, new Timestamp(lecture.getUpdatedAt().getTime()));
            ps.setInt(9, lecture.getStudentQuantity());
            ps.setString(10, lecture.getLectureType().getValue());
            ps.setObject(11, lecture.getCourseId());
            ps.executeUpdate();
        }
    }

    public Lecture getById(UUID id) throws SQLException {
        String sql = "SELECT * FROM lecture WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapLecture(rs);
            }
        }
        return null;
    }

    public List<Lecture> getAll() throws SQLException {
        List<Lecture> lectures = new ArrayList<>();
        String sql = "SELECT * FROM lecture";
        try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lectures.add(mapLecture(rs));
            }
        }
        return lectures;
    }

    public List<Lecture> getAllByProfessorId(UUID professorId) throws SQLException {
        List<Lecture> lectures = new ArrayList<>();
        String sql = "SELECT * FROM lecture WHERE professor_id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setObject(1, professorId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                lectures.add(mapLecture(rs));
            }
        }
        return lectures;
    }

    public void registerStudentInClass(UUID studentId, UUID lectureId) throws SQLException {
        String sql = "INSERT INTO lecture_students (lecture_id, student_id) VALUES (?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, lectureId);
            ps.setObject(2, studentId);
            ps.executeUpdate();
        }
    }

    public void removeStudentInClass(UUID studentId, UUID lectureId) throws SQLException {
        String sql = "DELETE FROM lecture_students WHERE lecture_id = ? AND student_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, lectureId);
            ps.setObject(2, studentId);
            ps.executeUpdate();
        }
    }

    public List<StudentInClass> getPersonsInClass(UUID classId) {
        List<StudentInClass> students = new ArrayList<>();

        String sql = "SELECT p.id AS person_id, s.id AS student_id, ls.lecture_id AS lecture_id, p.name AS name, p.person_code AS student_code FROM lecture_students ls JOIN student s ON s.id = ls.student_id JOIN person p ON p.id = s.person_id WHERE ls.lecture_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, classId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    students.add(new StudentInClass(
                            (UUID) rs.getObject("person_id"),
                            (UUID) rs.getObject("student_id"),
                            (UUID) rs.getObject("lecture_id"),
                            rs.getString("name"),
                            rs.getString("student_code")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar estudantes da turma", e);
        }

        return students;
    }

    public List<Lecture> getAllFromNow() throws SQLException {
        List<Lecture> lectures = new ArrayList<>();
        String sql = "SELECT * FROM lecture WHERE (end_date IS NULL OR end_date > NOW()) ORDER BY created_at ASC";

        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lectures.add(mapLecture(rs));
            }
        }
        return lectures;
    }

    public void save(Lecture lecture) throws SQLException {
        String sql = "UPDATE lecture SET subject_name = ?, professor_id = ?, room_id = ?, end_date = ?, date = ?, created_at = ?, updated_at = ?, student_quantity = ?, lecture_type = ?, course_id = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, lecture.getSubjectName());
            ps.setObject(2, lecture.getProfessorId());
            ps.setObject(3, lecture.getRoomId());
            ps.setTimestamp(4, new Timestamp(lecture.getEndDate().getTime()));
            ps.setString(5, lecture.getDate().getValue());
            ps.setTimestamp(6, new Timestamp(lecture.getCreatedAt().getTime()));
            ps.setTimestamp(7, new Timestamp(lecture.getUpdatedAt().getTime()));
            ps.setInt(8, lecture.getStudentQuantity());
            ps.setString(9, lecture.getLectureType().getValue());
            ps.setObject(10, lecture.getCourseId());
            ps.setObject(11, lecture.getId());
            ps.executeUpdate();
        }
    }

    public void deleteById(UUID id) throws SQLException {
        String sql = "DELETE FROM lecture WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, id);
            ps.executeUpdate();
        }
    }

    private Lecture mapLecture(ResultSet rs) throws SQLException {
        return new Lecture(
                (UUID) rs.getObject("id"),
                rs.getString("subject_name"),
                (UUID) rs.getObject("professor_id"),
                (UUID) rs.getObject("room_id"),
                new DateHour(rs.getString("date")),
                rs.getTimestamp("created_at"),
                rs.getTimestamp("updated_at"),
                rs.getInt("student_quantity"),
                rs.getTimestamp("end_date"),
                (UUID) rs.getObject("course_id"),
                new TypeObj(rs.getString("lecture_type"))
        );
    }
}


