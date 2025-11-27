package service.notification;

import java.sql.Connection;
import java.util.Date;
import java.util.UUID;

import dao.NotificationDao;
import model.Notification;

public class ReadNotificationService {

    private final NotificationDao notificationDao;

    public ReadNotificationService(Connection connection) {
        this.notificationDao = new NotificationDao(connection);
    }

    public Notification execute(UUID notificationId, UUID userId) throws Exception {
        try {
            if (notificationId == null || userId == null) {
                throw new Exception("Parâmetros inválidos");
            }

            Notification notification = notificationDao.getById(notificationId);
            if (notification == null) {
                throw new Exception("Notificação não encontrada");
            }

            if (notification.getPersonId() != null && !notification.getPersonId().equals(userId)) {
                throw new Exception("Não permitido marcar esta notificação como lida");
            }

            notification.setReadAt(new Date());
            notificationDao.save(notification);

            return notification;
        } catch (Exception e) {
            System.err.println("Erro em ReadNotificationService: " + e.getMessage());
            throw e;
        }
    }
}
