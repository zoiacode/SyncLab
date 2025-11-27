package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import model.Course;

public class CourseDao {
    private final Connection connection;

    public CourseDao(Connection connection) {
        this.connection = connection;
    }

    public void create(Course course) throws SQLException {
        String sql = "INSERT INTO course (id, name, acg, schedule, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, course.getId());
            ps.setString(2, course.getName());
            ps.setString(3, course.getAcg());
            ps.setString(4, course.getSchedule());
            ps.setTimestamp(5, new java.sql.Timestamp(course.getCreatedAt().getTime()));
            ps.setTimestamp(6, new java.sql.Timestamp(course.getUpdatedAt().getTime()));
            ps.executeUpdate();
        }
    }
    

public void linkCoursesAndProfessor(UUID professorId, List<UUID> coursesId) throws SQLException {
    String sql = "INSERT INTO course_professor (professor_id, course_id) SELECT ? AS professor_id, UNNEST(?::uuid[]) AS course_id ON CONFLICT (professor_id, course_id) DO NOTHING";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setObject(1, professorId);
        UUID[] array = coursesId.toArray(new UUID[0]);
        java.sql.Array sqlArray = connection.createArrayOf("uuid", array);
        ps.setArray(2, sqlArray);

        ps.executeUpdate();
    }
}

    public Course getById(UUID id) throws SQLException {
        String sql = "SELECT * FROM course WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapCourse(rs);
            }
        }
        return null;
    }

    public List<Course> getAll() throws SQLException {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM course";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                courses.add(mapCourse(rs));
            }
        }
        return courses;
    }

    public void save(Course course) throws SQLException {
        String sql = "UPDATE course SET  name = ?, acg = ?, schedule = ?, created_at = ?, updated_at = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, course.getName());
            ps.setString(2, course.getAcg());
            ps.setString(3, course.getSchedule());
            ps.setTimestamp(4, new java.sql.Timestamp(course.getCreatedAt().getTime()));
            ps.setTimestamp(5, new java.sql.Timestamp(course.getUpdatedAt().getTime()));
            ps.setObject(6, course.getId());
            ps.executeUpdate();
        }
    }

    public void deleteById(UUID id) throws SQLException {
        String sql = "DELETE FROM course WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, id);
            ps.executeUpdate();
        }
    }

    private Course mapCourse(ResultSet rs) throws SQLException {
        return new Course(
            (UUID) rs.getObject("id"),
            rs.getString("name"),
            rs.getString("acg"),
            rs.getString("schedule"),
            rs.getTimestamp("created_at"),
            rs.getTimestamp("updated_at")
        );
    }
}
