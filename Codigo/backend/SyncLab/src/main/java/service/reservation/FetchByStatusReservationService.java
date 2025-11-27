package service.reservation;

import java.sql.Connection;
import java.util.List;

import dao.ReservationDao;
import model.Reservation;

public class FetchByStatusReservationService {
    private final ReservationDao reservationDao;
    public FetchByStatusReservationService(Connection connection) {
        this.reservationDao = new ReservationDao(connection);
    }

    public List<Reservation> execute(
        String status
    ) throws Exception {

        try {
            List<Reservation> reservationRef = reservationDao.getAllByStatus(status);
            return reservationRef;
        } catch (Exception e) {
            System.err.println("Erro em FetchReservationService: " + e.getMessage());
            throw e;
        }
    }
}
