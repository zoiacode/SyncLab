package service.building;

import java.sql.Connection;
import java.util.UUID;

import dao.BuildingDao;
import model.Building;

public class DeleteBuildingService {
    private final BuildingDao buildingDao;

    public DeleteBuildingService(Connection connection) {
        this.buildingDao = new BuildingDao(connection);
    }

    public void execute(UUID id) throws Exception {
        try {
            Building existingBuilding = buildingDao.getById(id);
            if (existingBuilding == null) {
                throw new Exception("Building com ID " + id + " não encontrado para exclusão.");
            }
            buildingDao.deleteById(id);
        } catch (Exception e) {
            throw new Exception("Falha ao deletar Building: " + e.getMessage());
        }
    }
}