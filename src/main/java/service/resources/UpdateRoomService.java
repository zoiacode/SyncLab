package service.resources;

import java.sql.Connection;
import java.util.UUID;

import dao.BuildingDao;
import dao.RoomDao;
import model.Building;
import model.Room;

public class UpdateRoomService {
    private final RoomDao dao;
    private final BuildingDao buildingDao;

    public UpdateRoomService(Connection connection) {
        this.dao = new RoomDao(connection);
        this.buildingDao = new BuildingDao(connection);
    }

    public Room execute(
        UUID id,
        int capacity, 
        String roomType, 
        String code,
        String status, 
        String imageUrl, 
        UUID buildingId, 
        int floor
    ) throws Exception {

        try {
            Building buildingRef = buildingDao.getById(buildingId);

            if(buildingRef == null) {
                throw new Exception("Prédio não encontrado"); 
            }

            Room room = dao.getById(id);
            if (room == null) {
                throw new Exception("Sala não encontrado");
            }  

            room.setCapacity(capacity);
            room.setRoomType(roomType);
            room.setCode(code);
            room.setStatus(status);
            room.setImageUrl(imageUrl);
            room.setBuildingId(buildingId);
            room.setFloor(floor);

            boolean updated = dao.save(room);
            if (!updated) {
                throw new Exception("Falha ao editar Sala no banco.");
            }

            return room;
        } catch (Exception e) {
            System.err.println("Erro em UpdateRoomService: " + e.getMessage());
            throw e;
        }
    }
}
