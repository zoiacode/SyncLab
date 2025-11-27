package service.lectures;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import dao.CheckinDao;
import dao.LectureDao;
import dao.StudentDao;
import model.Checkin;
import model.Lecture;
import model.Student;

public class CheckinManyService {

    private final CheckinDao checkinDao;
    private final StudentDao studentDao;
    private final LectureDao lectureDao;

    public CheckinManyService(Connection connection) {
        this.checkinDao = new CheckinDao(connection);
        this.studentDao = new StudentDao(connection);
        this.lectureDao = new LectureDao(connection);
    }

    
    public List<Checkin> execute(UUID lectureId, List<UUID> studentIds) throws Exception {
        try {
            Lecture lecture = lectureDao.getById(lectureId);
            if (lecture == null) {
                throw new Exception("Aula não encontrada");
            }

            List<Checkin> checkins = new ArrayList<>();

            for (UUID studentId : studentIds) {
                
                Student student = studentDao.getByPersonId(studentId);
                if (student == null) {
                    throw new Exception("Aluno não encontrado: " + studentId);
                }

                
                Checkin checkin = new Checkin(studentId, lectureId);
                checkinDao.create(checkin);
                checkins.add(checkin);
            }

            return checkins;

        } catch (Exception e) {
            System.err.println("Erro em CheckinManyService: " + e.getMessage());
            throw e;
        }
    }
}
