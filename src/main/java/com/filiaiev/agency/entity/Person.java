package com.filiaiev.agency.entity;

import java.sql.Date;

public class Person extends Entity{

    private String first_name;
    private String middle_name;
    private String last_name;
    private Date birthdate;
    private int user_id;

    public String getFirstName() {
        return first_name;
    }

    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }

    public String getMiddleName() {
        return middle_name;
    }

    public void setMiddleName(String middle_name) {
        this.middle_name = middle_name;
    }

    public String getLastName() {
        return last_name;
    }

    public void setLastName(String last_name) {
        this.last_name = last_name;
    }

    public Date getBirthDate() {
        return birthdate;
    }

    public void setBirthDate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public int getUserId() {
        return user_id;
    }

    public void setUserId(int user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "Person #" + id + "\nfirst name: " + first_name +
                "\nmiddle name: " + middle_name + "\nlast name: " + last_name +
                "\nbirth date: " + birthdate + "\nuser_id: " + user_id;
    }
}
