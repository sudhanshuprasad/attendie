package com.regain.attendie;

public class RegistrationAttendance {
//    payload = str({"registerationid": self.registerationid})
    String registerationid;

    public RegistrationAttendance(String registerationid) {
        this.registerationid = registerationid;
    }

    public String getRegisterationid() {
        return registerationid;
    }

    public void setRegisterationid(String registerationid) {
        this.registerationid = registerationid;
    }

    public RegistrationAttendance() {
    }
}
