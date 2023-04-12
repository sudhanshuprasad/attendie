package com.regain.attendie;

public class Result_Model {
    String semester_name;
    String sgpa;
    String totalearnedcredit;

    public Result_Model(String semester_name, String sgpa, String totalearnedcredit) {
        this.semester_name = semester_name;
        this.sgpa = sgpa;
        this.totalearnedcredit = totalearnedcredit;
    }

    public Result_Model() {
    }

    public String getSemester_name() {
        return semester_name;
    }

    public void setSemester_name(String semester_name) {
        this.semester_name = semester_name;
    }

    public String getSgpa() {
        return sgpa;
    }

    public void setSgpa(String sgpa) {
        this.sgpa = sgpa;
    }

    public String getTotalearnedcredit() {
        return totalearnedcredit;
    }

    public void setTotalearnedcredit(String totalearnedcredit) {
        this.totalearnedcredit = totalearnedcredit;
    }
}
