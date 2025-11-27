package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import model.Equipment;
import model.Person;
import model.Reservation;
import model.Room;
import model.UserEquipmentHistory;
import model.UserRoomHistory;

public class ReservationDao {

    private final Connection connection;

    public ReservationDao(Connection connection) {
        this.connection = connection;
    }

    public boolean create(Reservation reservation) throws SQLException {
        String sql = "INSERT INTO reservation (id, person_id, equipment_id, room_id, resource_type, purpose, start_time, end_time, status, created_at, updated_at, course_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, reservation.getId());
            ps.setObject(2, reservation.getPersonId());

            // equipment_id pode ser nulo
            if (reservation.getEquipmentId() != null) {
                ps.setObject(3, reservation.getEquipmentId()); 
            }else {
                ps.setNull(3, java.sql.Types.OTHER);
            }

            // room_id pode ser nulo
            if (reservation.getRoomId() != null) {
                ps.setObject(4, reservation.getRoomId()); 
            }else {
                ps.setNull(4, java.sql.Types.OTHER);
            }

            ps.setString(5, reservation.getResourceType());
            ps.setString(6, reservation.getPurpose());
            ps.setTimestamp(7, new Timestamp(reservation.getStartTime().getTime()));
            ps.setTimestamp(8, new Timestamp(reservation.getEndTime().getTime()));
            ps.setString(9, reservation.getStatus());
            ps.setTimestamp(10, new Timestamp(reservation.getCreatedAt().getTime()));
            ps.setTimestamp(11, new Timestamp(reservation.getUpdatedAt().getTime()));
            ps.setObject(12, reservation.getCourseId());

            return ps.executeUpdate() > 0;
        }
    }

    public List<Reservation> getAllByPersonId(UUID personId) throws SQLException {
        String sql = "SELECT * FROM reservation WHERE person_id = ? ORDER BY created_at DESC";
        List<Reservation> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, personId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapReservation(rs));
            }
        }
        return list;
    }

    public Reservation getById(UUID id) throws SQLException {
        String sql = "SELECT * FROM reservation WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapReservation(rs);
            }
        }
        return null;
    }

    public List<Reservation> getApprovedByRoomAndTime(UUID roomId, Date startTime, Date endTime) throws SQLException {
        String sql = "SELECT * FROM reservation WHERE room_id = ? AND status = 'Approved' AND start_time < ? AND end_time > ? ORDER BY created_at DESC";

        List<Reservation> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, roomId);
            ps.setTimestamp(2, new java.sql.Timestamp(endTime.getTime())); // endTime do intervalo
            ps.setTimestamp(3, new java.sql.Timestamp(startTime.getTime())); // startTime do intervalo

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapReservation(rs));
                }
            }
        }
        return list;
    }

    public List<Reservation> getApprovedByEquipmentAndTime(UUID equipmentId, Date startTime, Date endTime) throws SQLException {
        String sql = "SELECT * FROM reservation WHERE equipment_id = ? AND status = 'Approved' AND start_time < ? AND end_time > ? ORDER BY created_at DESC";

        List<Reservation> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, equipmentId);
            ps.setTimestamp(2, new java.sql.Timestamp(endTime.getTime())); // endTime do intervalo
            ps.setTimestamp(3, new java.sql.Timestamp(startTime.getTime())); // startTime do intervalo

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapReservation(rs));
                }
            }
        }
        return list;
    }

    public List<Reservation> getAllByEquipmentId(UUID equipmentId) throws SQLException {
        String sql = "SELECT * FROM reservation WHERE equipment_id = ? ORDER BY created_at DESC";
        List<Reservation> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, equipmentId);
            ResultSet rs = ps.executeQuery(sql);
            while (rs.next()) {
                list.add(mapReservation(rs));
            }
        }
        return list;
    }

    public List<Reservation> getAllByRoomId(UUID roomId) throws SQLException {
        String sql = "SELECT * FROM reservation WHERE room_id = ? ORDER BY created_at DESC";
        List<Reservation> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, roomId);
            ResultSet rs = ps.executeQuery(sql);
            while (rs.next()) {
                list.add(mapReservation(rs));
            }
        }
        return list;
    }

   public List<Reservation> getAllByStatus(String status) throws SQLException {
String sql = "SELECT " +
    // Colunas da Reserva (r)
    "r.id, r.person_id, r.equipment_id, r.room_id, r.resource_type, r.purpose, r.start_time, r.end_time, r.status, r.created_at, r.updated_at, r.course_id, " +
    // Colunas da Pessoa (p)
    "p.id AS person_id_alias, p.name AS person_name, p.phone_number AS person_phone_number, p.cpf AS person_cpf, " +
    "p.birth_date AS person_birth_date, p.profile_url AS person_profile_url, p.description AS person_description, " +
    "p.desactivated_at AS person_desactivated_at, p.person_code AS person_code, p.created_at AS person_created_at, " +
    "p.updated_at AS person_updated_at, p.role AS person_role, " +
    // Colunas do Equipamento (e)
    "e.id AS equipment_id_alias, e.name AS equipment_name, e.description AS equipment_description, e.quantity AS equipment_quantity, " +
    "e.status AS equipment_status, e.max_loan_duration AS equipment_max_loan_duration, e.image_url AS equipment_image_url, " +
    "e.created_at AS equipment_created_at, e.updated_at AS equipment_updated_at, " +
    // Colunas da Sala (ro)
    "ro.id AS room_id_alias, ro.capacity AS room_capacity, ro.room_type AS room_room_type, ro.room_code AS room_code, " +
    "ro.status AS room_status, ro.image_url AS room_image_url, ro.building_id AS room_building_id, ro.floor AS room_floor, " +
    "ro.created_at AS room_created_at, ro.updated_at AS room_updated_at " +
    // Estrutura de JOINs e Condições
    "FROM reservation r " +
    "INNER JOIN person p ON r.person_id = p.id " +
    "LEFT JOIN equipment e ON r.equipment_id = e.id " +
    "LEFT JOIN room ro ON r.room_id = ro.id " +
    "WHERE r.status = ? " +
    "ORDER BY r.created_at DESC";

    List<Reservation> list = new ArrayList<>();
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setString(1, status);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            list.add(mapReservationWithDetails(rs));
        }
    }
    return list;
}

private Reservation mapReservationWithDetails(ResultSet rs) throws SQLException {
    Reservation reservation = new Reservation();
    
    reservation.setId(rs.getObject("id", java.util.UUID.class));
    reservation.setPersonId(rs.getObject("person_id", java.util.UUID.class));
    reservation.setEquipmentId(rs.getObject("equipment_id", java.util.UUID.class));
    reservation.setRoomId(rs.getObject("room_id", java.util.UUID.class));
    reservation.setResourceType(rs.getString("resource_type"));
    reservation.setPurpose(rs.getString("purpose"));
    reservation.setStartTime(rs.getTimestamp("start_time"));
    reservation.setEndTime(rs.getTimestamp("end_time"));
    reservation.setStatus(rs.getString("status"));
    reservation.setCreatedAt(rs.getTimestamp("created_at"));
    reservation.setUpdatedAt(rs.getTimestamp("updated_at"));
    reservation.setCourseId(rs.getObject("course_id", java.util.UUID.class));

    Person person = new Person();
    person.setId(rs.getObject("person_id_alias", java.util.UUID.class));
    person.setName(rs.getString("person_name"));
    person.setPhoneNumber(rs.getString("person_phone_number"));
    person.setCpf(rs.getString("person_cpf"));
    person.setBirthDate(rs.getDate("person_birth_date"));
    person.setProfileUrl(rs.getString("person_profile_url"));
    person.setDescription(rs.getString("person_description"));
    person.setDesactivatedAt(rs.getDate("person_desactivated_at"));
    person.setPersonCode(rs.getString("person_code"));
    person.setUpdatedAt(rs.getTimestamp("person_updated_at"));
    person.setRole(rs.getString("person_role"));
    

    reservation.setPersonRef(person);

    UUID equipmentId = reservation.getEquipmentId();
    if (equipmentId != null) {
        Equipment equipment = new Equipment();
        equipment.setId(rs.getObject("equipment_id_alias", java.util.UUID.class));
        equipment.setName(rs.getString("equipment_name"));
        equipment.setDescription(rs.getString("equipment_description"));
        equipment.setQuantity(rs.getInt("equipment_quantity"));
        equipment.setStatus(rs.getString("equipment_status"));
        equipment.setMaxLoanDuration(rs.getInt("equipment_max_loan_duration"));
        equipment.setImageUrl(rs.getString("equipment_image_url"));
        // A data de criação é final na classe Equipment.
        // equipment.setCreatedAt(rs.getTimestamp("equipment_created_at")); 
        equipment.setUpdatedAt(rs.getTimestamp("equipment_updated_at"));
        reservation.setEquipmentRef(equipment);
    } 

    UUID roomId = reservation.getRoomId();
    if (roomId != null) {
        Room room = new Room();
        room.setId(rs.getObject("room_id_alias", java.util.UUID.class));
        room.setCapacity(rs.getInt("room_capacity"));
        room.setRoomType(rs.getString("room_room_type"));
        room.setCode(rs.getString("room_code"));
        room.setStatus(rs.getString("room_status"));
        room.setImageUrl(rs.getString("room_image_url"));
        room.setBuildingId(rs.getObject("room_building_id", java.util.UUID.class));
        room.setFloor(rs.getInt("room_floor"));
        room.setUpdatedAt(rs.getTimestamp("room_updated_at"));
        reservation.setRoom(room);
    }
    
    return reservation;
}

    public List<Reservation> getAll() throws SQLException {
        List<Reservation> list = new ArrayList<>();
        String sql = "SELECT * FROM reservation ORDER BY created_at DESC";
        try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapReservation(rs));
            }
        }
        return list;
    }

    public boolean save(Reservation reservation) throws SQLException {
        String sql = "UPDATE reservation SET person_id = ?, equipment_id = ?, room_id = ?, resource_type = ?, purpose = ?, start_time = ?, end_time = ?, status = ?, updated_at = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, reservation.getPersonId());
            ps.setObject(2, reservation.getEquipmentId());
            ps.setObject(3, reservation.getRoomId());
            ps.setString(4, reservation.getResourceType());
            ps.setString(5, reservation.getPurpose());
            ps.setTimestamp(6, new Timestamp(reservation.getStartTime().getTime()));
            ps.setTimestamp(7, new Timestamp(reservation.getEndTime().getTime()));
            ps.setString(8, reservation.getStatus());
            ps.setTimestamp(9, new Timestamp(reservation.getUpdatedAt().getTime()));
            ps.setObject(10, reservation.getId());
            int lines = ps.executeUpdate();
            return lines > 0;
        }
    }

    public void deleteById(UUID id) throws SQLException {
        String sql = "DELETE FROM reservation WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, id);
            ps.executeUpdate();
        }
    }

    private Reservation mapReservation(ResultSet rs) throws SQLException {
        Reservation newReservation = new Reservation(
                (UUID) rs.getObject("id"),
                (UUID) rs.getObject("person_id"),
                (UUID) rs.getObject("equipment_id"),
                (UUID) rs.getObject("room_id"),
                rs.getString("resource_type"),
                rs.getString("purpose"),
                rs.getTimestamp("start_time"),
                rs.getTimestamp("end_time"),
                rs.getString("status"),
                rs.getTimestamp("created_at"),
                rs.getTimestamp("updated_at")
        );

        newReservation.setCourseId((UUID) rs.getObject("course_id"));

        return newReservation;
    }

    public List<UserRoomHistory> getUserRoomHistory(UUID userId) throws SQLException {
        String sql = "SELECT room_id, COUNT(*) AS usage_count "
                + "FROM reservation "
                + "WHERE person_id = ? AND status = 'Approved' AND room_id IS NOT NULL "
                + "GROUP BY room_id";
        List<UserRoomHistory> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    UUID roomId = (UUID) rs.getObject("room_id");
                    int usageCount = rs.getInt("usage_count");
                    list.add(new UserRoomHistory(userId, roomId, usageCount));
                }
            }
        }
        return list;
    }

    public List<UserEquipmentHistory> getUserEquipmentHistory(UUID userId) throws SQLException {
        String sql = "SELECT equipment_id, COUNT(*) AS usage_count "
                + "FROM reservation "
                + "WHERE person_id = ? AND status = 'Approved' AND equipment_id IS NOT NULL "
                + "GROUP BY equipment_id";
        List<UserEquipmentHistory> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    UUID equipmentId = (UUID) rs.getObject("equipment_id");
                    int usageCount = rs.getInt("usage_count");
                    list.add(new UserEquipmentHistory(userId, equipmentId, usageCount));
                }
            }
        }
        return list;
    }

    public List<Date> getPossibleStartTimes() throws SQLException {
        String sql = "SELECT DISTINCT start_time FROM reservation WHERE start_time >= ? ORDER BY start_time";
        List<Date> list = new ArrayList<>();
        Date now = new Date();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setTimestamp(1, new Timestamp(now.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Timestamp ts = rs.getTimestamp("start_time");
                    if (ts != null) {
                        list.add(new Date(ts.getTime()));
                    }
                }
            }
        }

        if (list.isEmpty()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(now);
            for (int i = 0; i < 8; i++) {
                list.add(cal.getTime());
                cal.add(Calendar.HOUR_OF_DAY, 1);
            }
        }

        return list;
    }

}
