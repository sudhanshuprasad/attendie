package com.regain.attendie;

public class Update_Change_Password {
    String newpassword;
    String confirmpassword;

    public Update_Change_Password(String newpassword, String confirmpassword) {
        this.newpassword = newpassword;
        this.confirmpassword = confirmpassword;
    }

    public Update_Change_Password() {
    }

    public String getNewpassword() {
        return newpassword;
    }

    public void setNewpassword(String newpassword) {
        this.newpassword = newpassword;
    }

    public String getConfirmpassword() {
        return confirmpassword;
    }

    public void setConfirmpassword(String confirmpassword) {
        this.confirmpassword = confirmpassword;
    }
}
