package service.notification;

import java.sql.Connection;
import java.util.UUID;

import dao.NotificationDao;
import model.Notification;

public class DeleteNotificationService {

    private final NotificationDao notificationDao;

    public DeleteNotificationService(Connection connection) {
        this.notificationDao = new NotificationDao(connection);
    }

    public Notification execute(UUID notificationId) throws Exception {
        try {
            Notification notification = notificationDao.getById(notificationId);
            if ( notification == null)
                throw new Exception("Notificação não encontrada");

            notificationDao.deleteById(notificationId);
            return notification;
        } catch (Exception e) {
            System.err.println("Erro em DeleteNotificationService: " + e.getMessage());
            throw e;
        }
    }
}
