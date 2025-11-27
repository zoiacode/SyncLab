package service.building;

import java.sql.Connection;
import java.util.List;

import dao.BuildingDao;
import model.Building;

public class GetAllBuildingsService {
    private final BuildingDao buildingDao;

    public GetAllBuildingsService(Connection connection) {
        this.buildingDao = new BuildingDao(connection);
    }

    public List<Building> execute() throws Exception {
        try {
            return buildingDao.getAll();
        } catch (Exception e) {
            throw new Exception("Falha ao buscar todos os Buildings: " + e.getMessage());
        }
    }
}