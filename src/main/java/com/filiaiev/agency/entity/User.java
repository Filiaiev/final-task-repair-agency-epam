package com.filiaiev.agency.entity;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return roleId == user.roleId && Objects.equals(email, user.email) && Objects.equals(login, user.login) && Objects.equals(pass, user.pass);
    }

}
