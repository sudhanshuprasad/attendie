package com.regain.attendie;

public class Semester {
    String styno;

    public Semester() {
    }

    public Semester(String styno) {
        this.styno = styno;
    }

    public String getSem() {
        return styno;
    }

    public void setSem(String sem) {
        this.styno = sem;
    }
}
