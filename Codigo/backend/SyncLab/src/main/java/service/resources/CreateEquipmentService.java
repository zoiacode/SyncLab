package service.resources;

import java.sql.Connection;

import dao.EquipmentDao;
import model.Equipment;
import service.notification.CreateNotificationService;

public class CreateEquipmentService {
    private final EquipmentDao equipmentDao;
    private final Connection connection;
    public CreateEquipmentService(Connection connection) {
        this.equipmentDao = new EquipmentDao(connection);
        this.connection = connection;
    }

    public Equipment execute(
        String name, 
        String description, 
        int quantity, 
        String status, 
        int maxLoanDuration, 
        String imageUrl
    ) throws Exception {

        try {
            Equipment equipment = new Equipment(
                name, 
                description, 
                quantity, 
                status, 
                maxLoanDuration, 
                imageUrl
            );

            boolean created = equipmentDao.create(equipment);
            if (!created) {
                throw new Exception("Falha ao cadastrar equipamento no banco.");
            }

            CreateNotificationService notification = new CreateNotificationService(connection);

            String notificationText = "Equipamento: " + equipment.getName() + " cadastrado no sistema.";

            notification.execute(null, "Novo item cadastrado", notificationText);

            return equipment;
        } catch (Exception e) {
            System.err.println("Erro em CreateEquipmentService: " + e.getMessage());
            e.getStackTrace();
            throw e;
        }
    }
}
