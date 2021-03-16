package com.example.cms.entity;

public class Cource {
    private String Cou_name;
    private String Cou_teacher;
    private String Cou_classroom;
    private String Cou_weekday;
    private String Cou_period;
    private String grade;

    public Cource() {
    }

    public String getCou_name() {
        return Cou_name;
    }

    public void setCou_name(String cou_name) {
        Cou_name = cou_name;
    }

    public String getCou_teacher() {
        return Cou_teacher;
    }

    public void setCou_teacher(String cou_teacher) {
        Cou_teacher = cou_teacher;
    }

    public String getCou_classroom() {
        return Cou_classroom;
    }

    public void setCou_classroom(String cou_classroom) {
        Cou_classroom = cou_classroom;
    }

    public String getCou_weekday() {
        return Cou_weekday;
    }

    public void setCou_weekday(String cou_weekday) {
        Cou_weekday = cou_weekday;
    }

    public String getCou_period() {
        return Cou_period;
    }

    public void setCou_period(String cou_period) {
        Cou_period = cou_period;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "Cource{" +
                "Cou_name='" + Cou_name + '\'' +
                ", Cou_teacher='" + Cou_teacher + '\'' +
                ", Cou_classroom='" + Cou_classroom + '\'' +
                ", Cou_weekday='" + Cou_weekday + '\'' +
                ", Cou_period='" + Cou_period + '\'' +
                ", grade='" + grade + '\'' +
                '}';
    }
}
