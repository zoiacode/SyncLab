package service.resources;

import java.sql.Connection;
import java.util.UUID;

import dao.RoomDao;
import model.Room;

public class DeleteRoomService {
    private final RoomDao roomDao;
    public DeleteRoomService(Connection connection) {
        this.roomDao = new RoomDao(connection);
    }

    public Room execute(
        UUID id
    ) throws Exception {
        try {
            Room roomRef = roomDao.getById(id);

            if(roomRef == null) {
                throw new Exception("Sala não cadastrado");
            }

            boolean removed = roomDao.deleteById(id);
            if (!removed) {
                throw new Exception("Falha ao deletar sala no banco.");
            }

            return roomRef;
        } catch (Exception e) {
            System.err.println("Erro em RemovedRoomService: " + e.getMessage());
            throw e;
        }
    }
}
