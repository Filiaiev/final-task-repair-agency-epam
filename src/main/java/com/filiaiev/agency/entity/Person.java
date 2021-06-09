package com.filiaiev.agency.entity;

import java.sql.Date;
import java.util.Objects;

public class Person extends Entity{

    private String firstName;
    private String middleName;
    private String lastName;
    private Date birthDate;
    private int userId;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String first_name) {
        this.firstName = first_name;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middle_name) {
        this.middleName = middle_name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String last_name) {
        this.lastName = last_name;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthdate) {
        this.birthDate = birthdate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int user_id) {
        this.userId = user_id;
    }

    @Override
    public String toString() {
        return "Person #" + id + "\nfirst name: " + firstName +
                "\nmiddle name: " + middleName + "\nlast name: " + lastName +
                "\nbirth date: " + birthDate + "\nuser_id: " + userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return userId == person.userId && Objects.equals(firstName, person.firstName) && Objects.equals(middleName, person.middleName) && Objects.equals(lastName, person.lastName) && Objects.equals(birthDate, person.birthDate);
    }
}
