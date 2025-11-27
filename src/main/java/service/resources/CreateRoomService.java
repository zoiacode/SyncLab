package service.resources;

import java.sql.Connection;
import java.util.UUID;

import dao.BuildingDao;
import dao.RoomDao;
import model.Building;
import model.Room;
import service.notification.CreateNotificationService;

public class CreateRoomService {
    private final RoomDao roomDao;
    private final BuildingDao buildingDao;
    private final Connection connection;
    public CreateRoomService(Connection connection) {
        this.roomDao = new RoomDao(connection);
        this.buildingDao = new BuildingDao(connection);
        this.connection = connection;

    }

    public Room execute(
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
            
            Room room = new Room(
                capacity, 
                roomType, 
                code,
                status, 
                imageUrl, 
                buildingId, 
                floor
            );

            boolean created = roomDao.create(room);
            if (!created) {
                throw new Exception("Falha ao cadastrar sala no banco.");
            }

            
            CreateNotificationService notification = new CreateNotificationService(connection);

            String notificationText = "Sala: " + room.getCode() + " cadastrado no sistema.";

            notification.execute(null, "Nova sala cadastrada", notificationText);

            return room;
        } catch (Exception e) {
            System.err.println("Erro em CreateRoomService: " + e.getMessage());
            throw e;
        }
    }
}
