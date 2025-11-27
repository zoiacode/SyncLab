package service.reservation;

import java.sql.Connection;
import java.util.List;
import java.util.UUID;

import dao.ReservationDao;
import model.Reservation;

public class FetchReservationByPersonIdService {
    private final ReservationDao reservationDao;
    
    public FetchReservationByPersonIdService(Connection connection) {
        this.reservationDao = new ReservationDao(connection);
    }
    public List<Reservation> execute(
        UUID personId
    ) throws Exception {

        try {
            List<Reservation> reservationRef = reservationDao.getAllByPersonId(personId);
            return reservationRef;
        } catch (Exception e) {
            System.err.println("Erro em FetchReservationByPersonIdService: " + e.getMessage());
            throw e;
        }
    }
}