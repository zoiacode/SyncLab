package service.notification;

import java.sql.Connection;
import java.util.UUID;

import dao.NotificationDao;
import model.Notification;

public class UpdateNotificationService {

    private final NotificationDao notificationDao;

    public UpdateNotificationService(Connection connection) {
        this.notificationDao = new NotificationDao(connection);
    }

    public Notification execute(UUID notificationId, String newTitle, String newMessage) throws Exception {
        try {
            Notification notification = notificationDao.getById(notificationId);
            if (notification == null) throw new Exception("Notificação não encontrada");

            if (newTitle != null) {
                notification.setTitle(newTitle.trim());
            }
            if (newMessage != null) {
                notification.setMessage(newMessage.trim());
            }

            notificationDao.save(notification);
            return notification;
        } catch (Exception e) {
            System.err.println("Erro em UpdateNotificationService: " + e.getMessage());
            throw e;
        }
    }
}
