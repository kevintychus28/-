package com.example.cms.student.schedule;

public class Cource {
    private String cName;
    private String cClassroom;
    private String cTeacher;

    public Cource() {
    }

    public Cource(String cName, String cClassroom, String cTeacher){
        this.cName = cName;
        this.cClassroom = cClassroom;
        this.cTeacher = cTeacher;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getcClassroom() {
        return cClassroom;
    }

    public void setcClassroom(String cClassroom) {
        this.cClassroom = cClassroom;
    }

    public String getcTeacher() {
        return cTeacher;
    }

    public void setcTeacher(String cTeacher) {
        this.cTeacher = cTeacher;
    }
}
