package model;

import java.util.Date;
import java.util.UUID;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import util.valueObject.DateHour;
import util.valueObject.TypeObj;

@PlanningEntity
public class Lecture {

    @PlanningId 
    private UUID id;
    private String subjectName;
    private UUID professorId;
    private DateHour date;
    private int studentQuantity;
    private UUID roomId;
    private UUID courseId;
    private Date endDate;
    private TypeObj lectureType;
    private Room room;

    private final Date createdAt;
    private Date updatedAt;

    public Lecture(String subjectName, UUID professorId, UUID roomId, DateHour date, int studentQuantity, Date endDate, UUID courseId, TypeObj lectureType) {
        this.id = UUID.randomUUID();
        this.subjectName = subjectName;
        this.professorId = professorId;
        this.roomId = roomId;
        this.date = date;
        this.studentQuantity = studentQuantity;
        this.endDate = endDate;
        this.courseId = courseId;
        this.lectureType = lectureType;
        
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    public Lecture(UUID id, String subjectName, UUID professorId, UUID roomId, DateHour date, Date createdAt, Date updatedAt, int studentQuantity, Date endDate, UUID courseId, TypeObj lectureType) {
        this.id = id;
        this.subjectName = subjectName;
        this.professorId = professorId;
        this.roomId = roomId;
        this.date = date;
        this.studentQuantity = studentQuantity;
        this.endDate = endDate;
        this.courseId = courseId;
        this.lectureType = lectureType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
        touch();
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
        touch();
    }

    public UUID getProfessorId() {
        return professorId;
    }

    public void setProfessorId(UUID professorId) {
        this.professorId = professorId;
        touch();
    }

    public UUID getRoomId() {
        return roomId;
    }
    public void setRoomId(UUID value) {
        this.roomId = value;
        this.touch();
    }

    public DateHour getDate() {
        return this.date;
    }

    public void setDate(DateHour date) {
        this.date = date;
        this.touch();
    }
    
    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    
    public int getStudentQuantity() {
        return studentQuantity;
    }
    
    public void setStudentQuantity(int studentQuantity) {
        this.studentQuantity = studentQuantity;
        touch();
    }

      public UUID getCourseId() {
        return courseId;
    }

    public void setCourse(UUID ourseId) {
        this.courseId = ourseId;
        touch();

    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
        touch();

    }

    public TypeObj getLectureType() {
        return lectureType;
    }

    public void setLectureType(TypeObj lectureType) {
        this.lectureType = lectureType;
        touch();

    }

    protected void touch() {
        this.updatedAt = new Date();
    }
}
