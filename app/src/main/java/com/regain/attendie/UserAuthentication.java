package com.regain.attendie;

public class UserAuthentication {
    String username;
    String password;
String MemberType;

    public UserAuthentication() {
    }

    public UserAuthentication(String username, String password, String memberType) {
        this.username = username;
        this.password = password;
        MemberType = memberType;
    }

    public String getMemberType() {
        return MemberType;
    }

    public void setMemberType(String memberType) {
        MemberType = memberType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
