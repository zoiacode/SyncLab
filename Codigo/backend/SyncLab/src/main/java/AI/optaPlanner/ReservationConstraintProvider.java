package AI.optaPlanner;

import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;
import org.optaplanner.core.api.score.stream.Joiners;

import model.Lecture;
import model.Reservation;
import model.UserEquipmentHistory;
import model.UserRoomHistory;
import util.valueObject.DateHour;

public class ReservationConstraintProvider implements ConstraintProvider {

    @Override
    public Constraint[] defineConstraints(ConstraintFactory factory) {
        return new Constraint[]{
            noRoomConflict(factory),
            preferFamiliarRooms(factory),
            rewardByCapacity(factory),
            rewardByCorrectType(factory),
            roomConflict(factory)
        };
    }

    private Constraint noRoomConflict(ConstraintFactory factory) {
        return factory
            .forEachUniquePair(Reservation.class,
                Joiners.equal(Reservation::getRoomId),
                Joiners.overlapping(Reservation::getStartTime, Reservation::getEndTime))
            .penalize(HardSoftScore.ONE_HARD)
            .asConstraint("Room conflict 2");
    }

    private Constraint rewardByCapacity(ConstraintFactory factory) {
        return factory.forEach(Lecture.class).filter(lecture -> lecture.getRoom() != null).reward("Capacidade adequada", HardSoftScore.ONE_SOFT, lecture -> {
            int capacidade = lecture.getRoom().getCapacity();
            int alunos = lecture.getStudentQuantity();

           // recompensa maior quando laboratório tem capacidade próxima ao necessário
            if (capacidade >= alunos) {
                return 10 - Math.abs(capacidade - alunos); 
            }
            return -20; // castigo se não tem cadeiras suficientes
        });
    }

    private Constraint rewardByCorrectType(ConstraintFactory factory)  {
        return factory.forEach(Lecture.class).filter(lecture -> lecture.getRoom() != null).impact("Tipo compatível", HardSoftScore.ONE_SOFT, lecture -> lecture.getRoom().getRoomType().equals(lecture.getLectureType()) ? 15 : -10); 
    }

    private Constraint noEquipmentConflict(ConstraintFactory factory) {
        return factory
            .forEachUniquePair(Reservation.class,
                Joiners.equal(Reservation::getEquipmentId),
                Joiners.overlapping(Reservation::getStartTime, Reservation::getEndTime))
            .penalize(HardSoftScore.ONE_HARD)
            .asConstraint("Equipment conflict");
    }

    private Constraint requireRoomAndEquipment(ConstraintFactory factory) {
    return factory
        .forEach(Reservation.class)
        .filter(reservation -> reservation.getRoomId() == null || reservation.getEquipmentId() == null)
        .penalize(HardSoftScore.ONE_HARD)
        .asConstraint("Reservation must have room and equipment");
}


    private Constraint rewardEfficientUsage(ConstraintFactory factory) {
        return factory
            .forEach(Reservation.class)
            .reward(HardSoftScore.ONE_SOFT)
            .asConstraint("Efficient resource usage");
    }

    private Constraint preferFamiliarRooms(ConstraintFactory factory) {
        return factory
            .forEach(Reservation.class)
            .join(UserRoomHistory.class,
                Joiners.equal(Reservation::getPersonId, UserRoomHistory::getUserId),
                Joiners.equal(Reservation::getRoomId, UserRoomHistory::getRoomId))
            .reward(HardSoftScore.ofSoft(5),
                (reservation, history) -> history.getUsageCount())
            .asConstraint("Prefer familiar rooms");
    }


    private Constraint preferFamiliarEquipments(ConstraintFactory factory) {
        return factory
            .forEach(Reservation.class)
            .join(UserEquipmentHistory.class,
                Joiners.equal(Reservation::getPersonId, UserEquipmentHistory::getUserId),
                Joiners.equal(Reservation::getEquipmentId, UserEquipmentHistory::getEquipmentId))
            .reward(HardSoftScore.ofSoft(5),
                (reservation, history) -> history.getUsageCount())
            .asConstraint("Prefer familiar equipments");
    }

    private Constraint roomConflict(ConstraintFactory factory) {
        return factory.forEachUniquePair(Lecture.class,
                        Joiners.equal(Lecture::getRoom))
                .filter(this::hasOverlap)
                .penalize("Room conflict", HardSoftScore.ONE_HARD);
    }

    private boolean hasOverlap(Lecture lecture1, Lecture lecture2) {
        if (lecture1.getDate() == null || lecture2.getDate() == null) {
            return false;
        }

        String days1 = lecture1.getDate().getDays();
        String days2 = lecture2.getDate().getDays();
        boolean isSameDay = false;

        for (char day : days1.toCharArray()) {
            if (days2.indexOf(day) != -1) {
                isSameDay = true;
                break;
            }
        }

        if (!isSameDay) {
            return false;
        }

        int start1 = convertToMinutes(lecture1.getDate());
        int start2 = convertToMinutes(lecture2.getDate());
        int duration = 50; 

        int end1 = start1 + duration;
        int end2 = start2 + duration;

        return start1 < end2 && start2 < end1;
    }

    private int convertToMinutes(DateHour dateHour) {
        try {
            int hours = Integer.parseInt(dateHour.getHour());
            int minutes = Integer.parseInt(dateHour.getMinutes());
            return (hours * 60) + minutes;
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
