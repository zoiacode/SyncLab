package service.building;

import java.sql.Connection;
import java.util.Date;
import java.util.UUID;

import dao.BuildingDao;
import model.Building;

public class CreateBuildingService {
    private final BuildingDao buildingDao;

    public CreateBuildingService(Connection connection) {
        this.buildingDao = new BuildingDao(connection);
    }

    public Building execute(
        String buildCode, 
        int floor, 
        String campus
    ) throws Exception {

        UUID id = UUID.randomUUID();
        Date now = new Date();

        Building building = new Building(
            id, 
            buildCode, 
            floor, 
            campus, 
            now, 
            now
        );

        try {
            buildingDao.create(building);
            return building;
        } catch (Exception e) {
            throw new Exception("Falha ao criar Building: " + e.getMessage());
        }
    }
}