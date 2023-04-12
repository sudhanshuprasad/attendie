package com.regain.attendie;

public class DetailedResultModel {
    String sub_name;
    String subject_code;
    String grade;
    String credit_earned;

    public DetailedResultModel() {
    }

    public DetailedResultModel(String sub_name, String subject_code, String grade, String credit_earned) {
        this.sub_name = sub_name;
        this.subject_code = subject_code;
        this.grade = grade;
        this.credit_earned = credit_earned;
    }

    public String getSub_name() {
        return sub_name;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }

    public String getSubject_code() {
        return subject_code;
    }

    public void setSubject_code(String subject_code) {
        this.subject_code = subject_code;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getCredit_earned() {
        return credit_earned;
    }

    public void setCredit_earned(String credit_earned) {
        this.credit_earned = credit_earned;
    }
}
