package service.resources;

import java.sql.Connection;
import java.util.UUID;

import dao.RoomDao;
import model.Room;

public class GetRoomByIdService {
    private final RoomDao roomDao;
    public GetRoomByIdService(Connection connection) {
        this.roomDao = new RoomDao(connection);
    }

    public Room execute(
        UUID id
    ) throws Exception {

        try {
        
            Room roomRef = roomDao.getById(id);
            if (roomRef == null) {
                throw new Exception("Sala não cadastrado");
            }

            return roomRef;
        } catch (Exception e) {
            System.err.println("Erro em GetRoomByIdService: " + e.getMessage());
            throw e;
        }
    }
}
