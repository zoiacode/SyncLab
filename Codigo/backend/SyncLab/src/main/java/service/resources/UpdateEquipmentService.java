package service.resources;

import java.sql.Connection;
import java.util.UUID;

import dao.EquipmentDao;
import model.Equipment;

public class UpdateEquipmentService {
    private final EquipmentDao dao;

    public UpdateEquipmentService(Connection connection) {
        this.dao = new EquipmentDao(connection);
    }

    public Equipment execute(
        UUID id,
        String name, 
        String description, 
        int quantity, 
        String status, 
        int maxLoanDuration, 
        String imageUrl
    ) throws Exception {

        try {
            Equipment equipment = dao.getById(id);
            if (equipment == null) {
                throw new Exception("Equipamento não encontrado");
            }  

            equipment.setName(name);
            equipment.setDescription(description);
            equipment.setQuantity(quantity);
            equipment.setStatus(status);
            equipment.setMaxLoanDuration(maxLoanDuration);
            equipment.setImageUrl(imageUrl);
            
            boolean updated = dao.save(equipment);
            if (!updated) {
                throw new Exception("Falha ao editar equipamento no banco.");
            }

            return equipment;
        } catch (Exception e) {
            System.err.println("Erro em UpdateEquipmentService: " + e.getMessage());
            throw e;
        }
    }
}
