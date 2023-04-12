package com.regain.attendie;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

//{"stynumber":6,"TotalAttandence":93.55,"Latt":"22 / 24","Patt":"7 / 7",
//        "subject":"Programming Languages and Compilers",
//        "Tatt":"0 / 0","subjectcode":"CSE4021","lastupdatedon":"25/06/2022 04:56 PM"}
public class AttendanceModel2 {
    public AttendanceModel2() {
        this.need = new ArrayList<String>();
        this.bunk = new ArrayList<String>();
    }

    public AttendanceModel2(String stynumber, String totalAttandence, String latt, String patt, String subject, String subjectcode, String lastupdation) {
        this.stynumber = stynumber;
        TotalAttandence = totalAttandence;
        Latt = latt;
        Patt = patt;
        this.subject = subject;
        this.subjectcode = subjectcode;
        this.lastupdation = lastupdation;
    }
    boolean expanded=false;

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }


    public String getStynumber() {
        return stynumber;
    }

    public void setStynumber(String stynumber) {
        this.stynumber = stynumber;
    }

    public String getTotalAttandence() {
        return TotalAttandence;
    }

    public void setTotalAttandence(String totalAttandence) {
        TotalAttandence = totalAttandence;
    }

    public String getLatt() {
        return Latt;
    }

    public void setLatt(String latt) {
        Latt = latt;
    }

    public String getPatt() {
        return Patt;
    }

    public void setPatt(String patt) {
        Patt = patt;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubjectcode() {
        return subjectcode;
    }

    public void setSubjectcode(String subjectcode) {
        this.subjectcode = subjectcode;
    }

    public String getLastupdation() {
        return lastupdation;
    }

    public void setLastupdation(String lastupdation) {
        this.lastupdation = lastupdation;
    }
    public int getTotalclasses(){
        String s_lab[]=new String[2];
        if(Patt.equals("Not Applicable")){
             s_lab[0]="0";
             s_lab[1]="0";
        }else{
             s_lab=Patt.split("/");
        }
        String s_theory[]=new String[2];
        if(Latt.equals("Not Applicable")){
            s_theory[0]="0";
            s_theory[1]="0";
        }else{
            s_theory=Latt.split("/");
        }

//        System.out.println("erro_patt"+Patt);
        try {
            total_class = (Integer.parseInt(s_lab[1].trim()) + Integer.parseInt(s_theory[1].trim())) + "";
            attend_class = (Integer.parseInt(s_lab[0].trim()) + Integer.parseInt(s_theory[0].trim())) + "";
        }catch (Exception e){

        }
        return 0;
    }

    String stynumber;
    String TotalAttandence;
    String Latt;
    String Patt;
    String subject;
    String subjectcode;
    String lastupdation;
    ArrayList<String> need;
    ArrayList<String> bunk;

    public String getTotal_class() {
        return total_class;
    }

    public void setTotal_class(String total_class) {
        this.total_class = total_class;
    }

    public String getAttend_class() {
        return attend_class;
    }

    public void setAttend_class(String attend_class) {
        this.attend_class = attend_class;
    }

    String total_class;
    String attend_class;
    public void setAttendanceProcedure() {
        //need for more classes
        needMore();
        bunkMore();
    }

    public void needMore() {
        int total = totalPercent();
        int totalclass=Integer.parseInt(total_class);
        int pres = Integer.parseInt(attend_class);
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
        int totalclass=Integer.parseInt(total_class);
        int pres = Integer.parseInt(attend_class);
        int rem = total % 5;
        int minimum_attendance = total - ((rem == 0) ? 5 : rem);
        for (int i = minimum_attendance; i >= 55; i -= 5) {
            double per =(double) i / 100;
            double nd = (pres - per * totalclass) / per;
            bunk.add("Bunk "+(int) Math.round(nd)+" more classes for "+i+"% attendance");
        }

    }
    public int totalPercent() {
        return (int) Math.round((Double.parseDouble(attend_class) / Double.parseDouble(total_class)) * 100);
    }

}
