package service.notification;

import java.sql.Connection;
import java.util.UUID;

import dao.NotificationDao;
import model.Notification;

public class CreateNotificationService {

    private final NotificationDao notificationDao;

    public CreateNotificationService(Connection connection) {
        this.notificationDao = new NotificationDao(connection);
    }

    public Notification execute(UUID personId, String title, String message) throws Exception {
        try {
            if(title == null || message == null) {
                throw new Exception("Informação faltando");
            }
    
            Notification notification = new Notification(personId, title, message);
            notificationDao.create(notification);
            return notification;
        } catch (Exception e) {
            System.err.println("Erro em CreateNotificationService: " + e.getMessage());
            throw e;
        }
    }
}
