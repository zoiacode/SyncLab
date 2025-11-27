package service.resources;

import java.sql.Connection;
import java.util.UUID;

import dao.EquipmentDao;
import model.Equipment;

public class DeleteEquipmentService {
    private final EquipmentDao equipmentDao;
    public DeleteEquipmentService(Connection connection) {
        this.equipmentDao = new EquipmentDao(connection);
    }

    public Equipment execute(
        UUID id
    ) throws Exception {
        try {
            Equipment equipmentRef = equipmentDao.getById(id);

            if(equipmentRef == null) {
                throw new Exception("Equipamento não cadastrado");
            }

            boolean removed = equipmentDao.deleteById(id);
            if (!removed) {
                throw new Exception("Falha ao deletar equipamento no banco.");
            }

            return equipmentRef;
        } catch (Exception e) {
            System.err.println("Erro em RemovedEquipmentService: " + e.getMessage());
            throw e;
        }
    }
}
