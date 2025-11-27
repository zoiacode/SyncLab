package service.building;

import java.sql.Connection;
import java.util.Date;
import java.util.UUID;

import dao.BuildingDao;
import model.Building;

public class UpdateBuildingService {
    private final BuildingDao buildingDao;

    public UpdateBuildingService(Connection connection) {
        this.buildingDao = new BuildingDao(connection);
    }

    public Building execute(
        UUID id,
        String buildCode, 
        int floor, 
        String campus
    ) throws Exception {
        
        Building existingBuilding = buildingDao.getById(id);
        if (existingBuilding == null) {
            throw new Exception("Building com ID " + id + " não encontrado para atualização.");
        }

        Date now = new Date();

        Building updatedBuilding = new Building(
            existingBuilding.getId(), 
            buildCode, 
            floor, 
            campus, 
            existingBuilding.getCreatedAt(), 
            now
        );

        try {
            buildingDao.save(updatedBuilding);
            return updatedBuilding;
        } catch (Exception e) {
            throw new Exception("Falha ao atualizar Building: " + e.getMessage());
        }
    }
}