package service.lectures;

import java.sql.Connection;
import java.util.UUID;

import dao.LectureDao;
import model.Lecture;

public class DeleteLectureService {

    private final LectureDao lectureDao;

    public DeleteLectureService(Connection connection) {
        this.lectureDao = new LectureDao(connection);
    }

    public void execute(UUID lectureId, UUID currentUserId, boolean isAdmin) throws Exception {
        try {
            Lecture lecture = lectureDao.getById(lectureId);
            if (lecture == null) {
                throw new Exception("Aula não encontrada");
            }

            if (!isAdmin && !lecture.getProfessorId().equals(currentUserId)) {
                throw new Exception("Não permitido deletar esta aula");
            }

            lectureDao.deleteById(lectureId);

        } catch (Exception e) {
            System.err.println("Erro em DeleteLectureService: " + e.getMessage());
            throw e;
        }
    }
}
