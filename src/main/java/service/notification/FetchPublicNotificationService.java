package service.notification;

import java.sql.Connection;
import java.util.List;

import dao.NotificationDao;
import model.Notification;

public class FetchPublicNotificationService {

    private final NotificationDao notificationDao;

    public FetchPublicNotificationService(Connection connection) {
        this.notificationDao = new NotificationDao(connection);
    }

    public List<Notification> execute() throws Exception {
        try {
            List<Notification> notifications = notificationDao.getAllPublic();
            return notifications;
        } catch (Exception e) {
            System.err.println("Erro em FetchPublicNotificationService: " + e.getMessage());
            throw e;
        }
    }
}
