package util;

import java.util.UUID;

public class StudentInClass {

    UUID personId;
    UUID studentId;
    UUID lectureId;
    String name;
    String studentCode;

    public StudentInClass(
            UUID personId,
            UUID studentId,
            UUID lectureId,
            String name,
            String studentCode
    ) {
        this.studentId = studentId;
        this.lectureId = lectureId;
        this.name = name;
        this.studentCode = studentCode;
        this.personId = personId;
    }
}