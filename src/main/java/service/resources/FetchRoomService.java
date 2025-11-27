package service.resources;

import java.sql.Connection;
import java.util.List;
import java.util.UUID;

import dao.RoomDao;
import model.Room;

public class FetchRoomService {
    private final RoomDao roomDao;
    public FetchRoomService(Connection connection) {
        this.roomDao = new RoomDao(connection);
    }

    public List<Room> execute(
        UUID id
    ) throws Exception {

        try {
            List<Room> roomRef = roomDao.getAll();
            return roomRef;
        } catch (Exception e) {
            System.err.println("Erro em FetchRoomService: " + e.getMessage());
            throw e;
        }
    }
}
