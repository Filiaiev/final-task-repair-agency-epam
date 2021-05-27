package com.filiaiev.agency.entity;

public class User extends Entity{

    private String email;
    private String login;
    private String pass;
    private int roleId;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int role_id) {
        this.roleId = role_id;
    }

    @Override
    public String toString() {
        return "User #" + id + "\nemail: " + email + "\nlogin: " + login + "\npass: " +
                pass + "\nrole_id: " + roleId;
    }
}
