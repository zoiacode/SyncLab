package service.notification;

import java.sql.Connection;
import java.util.List;
import java.util.UUID;

import dao.NotificationDao;
import model.Notification;

public class FetchVisibleNotificationsService {

    private final NotificationDao notificationDao;

    public FetchVisibleNotificationsService(Connection connection) {
        this.notificationDao = new NotificationDao(connection);
    }

    public List<Notification> execute(UUID userId) throws Exception {
        try {
            if (userId == null) {
                throw new Exception("User ID não pode ser nulo");
            }

            // Pega notificações privadas + públicas
            List<Notification> notifications = notificationDao.getAllVisibleForPerson(userId);
            return notifications;
        } catch (Exception e) {
            System.err.println("Erro em FetchVisibleNotificationsService: " + e.getMessage());
            throw e;
        }
    }
}
