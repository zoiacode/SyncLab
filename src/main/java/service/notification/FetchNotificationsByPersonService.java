package service.notification;

import java.sql.Connection;
import java.util.List;
import java.util.UUID;

import dao.NotificationDao;
import model.Notification;

public class FetchNotificationsByPersonService {

    private final NotificationDao notificationDao;

    public FetchNotificationsByPersonService(Connection connection) {
        this.notificationDao = new NotificationDao(connection);
    }

    public List<Notification> execute(UUID personId) throws Exception {
        try {
            if (personId == null) throw new Exception("PersonId não pode ser nulo");
            return notificationDao.getAllByPersonId(personId);
        } catch (Exception e) {
            System.err.println("Erro em FetchNotificationsByPersonService: " + e.getMessage());
            throw e;
        }
    }
}
