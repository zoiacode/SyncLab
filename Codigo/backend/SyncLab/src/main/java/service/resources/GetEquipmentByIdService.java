package service.resources;

import java.sql.Connection;
import java.util.UUID;

import dao.EquipmentDao;
import model.Equipment;

public class GetEquipmentByIdService {
    private final EquipmentDao equipmentDao;
    public GetEquipmentByIdService(Connection connection) {
        this.equipmentDao = new EquipmentDao(connection);
    }

    public Equipment execute(
        UUID id
    ) throws Exception {

        try {
        
            Equipment equipmentRef = equipmentDao.getById(id);
            if (equipmentRef == null) {
                throw new Exception("Equipamento não cadastrado");
            }

            return equipmentRef;
        } catch (Exception e) {
            System.err.println("Erro em GetEquipmentByIdService: " + e.getMessage());
            throw e;
        }
    }
}
