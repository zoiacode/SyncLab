package service.resources;

import java.sql.Connection;
import java.util.List;
import java.util.UUID;

import dao.EquipmentDao;
import model.Equipment;

public class FetchEquipmentService {
    private final EquipmentDao equipmentDao;
    public FetchEquipmentService(Connection connection) {
        this.equipmentDao = new EquipmentDao(connection);
    }

    public List<Equipment> execute(
        UUID id
    ) throws Exception {

        try {
            List<Equipment> equipmentRef = equipmentDao.getAll();
            return equipmentRef;
        } catch (Exception e) {
            System.err.println("Erro em FetchEquipmentService: " + e.getMessage());
            throw e;
        }
    }
}
