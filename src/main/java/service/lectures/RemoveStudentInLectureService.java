package service.lectures;

import java.sql.Connection;
import java.util.UUID;

import dao.LectureDao;
import dao.PersonDao;
import dao.ProfessorDao;
import dao.StudentDao;
import model.Lecture;
import model.Person;
import model.Professor;
import model.Student;
import service.notification.CreateNotificationService;

public class RemoveStudentInLectureService {
    final LectureDao lectureDao;
    final StudentDao studentDao;
    final ProfessorDao professorDao;
    final PersonDao personDao;
    final Connection connection;

    public RemoveStudentInLectureService(Connection connection) {
        this.lectureDao = new LectureDao(connection);
        studentDao = new StudentDao(connection);
        personDao = new PersonDao(connection);
        professorDao = new ProfessorDao(connection);
        this.connection = connection;
    }

    public void execute(
        UUID personId,
        UUID lectureId
    ) throws Exception {
        try {
            Person person = this.personDao.getById(personId);
            if(person == null) {
                throw new Exception("Usuário não encontrado");
            }

            Student student = this.studentDao.getByPersonId(person.getId());
            if(student == null) {
                  throw new Exception("Estudante não encontrado!");
            }

            Lecture lecture = this.lectureDao.getById(lectureId);

            if(lecture == null) {
                throw new Exception("Aula não cadastrada!");
            }

            Professor professor = this.professorDao.getById(lecture.getProfessorId());

            this.lectureDao.removeStudentInClass(student.getId(), lectureId);        
        

            CreateNotificationService notification = new CreateNotificationService(this.connection);
            String NotificationText = "Estudante: " + person.getName() + " se desistiu da aula: " + lecture.getSubjectName(); 
            notification.execute(professor.getPersonId(), "Aluno não mais registrado", NotificationText);

        } catch (Exception e) {
            System.err.println("Erro em RegisterLectureService: " + e.getMessage());
            throw e;
        } 
    }
}
