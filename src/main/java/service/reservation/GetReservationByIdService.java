package service.reservation;

import java.sql.Connection;
import java.util.UUID;

import dao.ReservationDao;
import model.Reservation;

public class GetReservationByIdService {
    private final ReservationDao reservationDao;
    public GetReservationByIdService(Connection connection) {
        this.reservationDao = new ReservationDao(connection);
    }

    public Reservation execute(
        UUID id
    ) throws Exception {

        try {
        
            Reservation reservationRef = reservationDao.getById(id);
            if (reservationRef == null) {
                throw new Exception("Equipamento não cadastrado");
            }

            return reservationRef;
        } catch (Exception e) {
            System.err.println("Erro em GetReservationByIdService: " + e.getMessage());
            throw e;
        }
    }
}
