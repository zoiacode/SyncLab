package service.building;

import java.sql.Connection;
import java.util.UUID;

import dao.BuildingDao;
import model.Building;

public class GetBuildingByIdService {
    private final BuildingDao buildingDao;

    public GetBuildingByIdService(Connection connection) {
        this.buildingDao = new BuildingDao(connection);
    }

    public Building execute(UUID id) throws Exception {
        try {
            Building building = buildingDao.getById(id);
            if (building == null) {
                throw new Exception("Building com ID " + id + " não encontrado.");
            }
            return building;
        } catch (Exception e) {
            throw new Exception("Falha ao buscar Building por ID: " + e.getMessage());
        }
    }
}