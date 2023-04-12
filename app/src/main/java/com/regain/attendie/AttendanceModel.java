package com.regain.attendie;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//{"stynumber":6,"TotalAttandence":93.55,"Latt":"22 / 24","Patt":"7 / 7",
//        "subject":"Programming Languages and Compilers",
//        "Tatt":"0 / 0","subjectcode":"CSE4021","lastupdatedon":"25/06/2022 04:56 PM"}
public class AttendanceModel {
    Pattern pattern = Pattern.compile("^(\\d+)/(\\d+)$");
    ArrayList<String> need;
    ArrayList<String> bunk;
    String subject_name;
    String subject_code;
    int labPresent;
    int labTotal;
    int theoryPresent;
    int theoryTotal;
    boolean expanded=false;

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public AttendanceModel() {
        this.need = new ArrayList<String>();
        this.bunk = new ArrayList<String>();
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public String getSubject_code() {
        return subject_code;
    }

    public void setSubject_code(String subject_code) {
        this.subject_code = subject_code;
    }

    public int getLabPresent() {
        return labPresent;
    }

    public void setLabPresent(int labPresent) {
        this.labPresent = labPresent;
    }

    public int getLabTotal() {
        return labTotal;
    }

    public void setLabTotal(int labTotal) {
        this.labTotal = labTotal;
    }

    public int getTheoryPresent() {
        return theoryPresent;
    }

    public void setTheoryPresent(int theoryPresent) {
        this.theoryPresent = theoryPresent;
    }

    public int getTheoryTotal() {
        return theoryTotal;
    }

    public void setTheoryTotal(int theoryTotal) {
        this.theoryTotal = theoryTotal;
    }

    public void setLab(String lab) {
//        if (!lab.equals("Not Applicable")) {
            Matcher labMatcher = pattern.matcher(lab);
            if (labMatcher.find()) {
                this.labPresent = Integer.parseInt(labMatcher.group(1));
                this.labTotal = Integer.parseInt(labMatcher.group(2));
            }


//        }

    }

    public void setTheory(String theory) {
        if (!theory.equals("Not Applicable")) {
            Matcher theoryMatcher = pattern.matcher(theory);
            if (theoryMatcher.find()) {
                this.theoryPresent = Integer.parseInt(theoryMatcher.group(1));
                this.theoryTotal = Integer.parseInt(theoryMatcher.group(2));
            }
        }
    }

    public int presentClass() {
        return this.labPresent + this.theoryPresent;
    }

    public int totalClass() {
        return this.labTotal + this.theoryTotal;
    }

    public int absentClass() {
        return totalClass() - presentClass();
    }

    public int totalPercent() {
        return (int) Math.round(((double) presentClass() / totalClass()) * 100);
    }

    public void setAttendanceProcedure() {
        //need for more classes
        needMore();
        bunkMore();
    }

    public void needMore() {
        int total = totalPercent();
        int pres = presentClass();
        int totalclass=totalClass();
        int rem = total % 5;
        int minimum_attendance = 5 - ((rem == 0) ? 0 : rem) + total;
        for (int i = minimum_attendance; i <= 95; i += 5) {
            double per = (double)i / 100;
            double nd = ((per * totalclass) - pres) / (1 - per);
            need.add("Need "+(int) Math.round(nd)+" more classes for "+i+"% attendance");
        }

    }

    public void bunkMore() {
        int total = totalPercent();
        int totalclass=totalClass();
        int pres = presentClass();
        int rem = total % 5;
        int minimum_attendance = total - ((rem == 0) ? 5 : rem);
        for (int i = minimum_attendance; i >= 55; i -= 5) {
            double per =(double) i / 100;
            double nd = (pres - per * totalclass) / per;
            bunk.add("Bunk "+(int) Math.round(nd)+" more classes for "+i+"% attendance");
        }

    }
}
