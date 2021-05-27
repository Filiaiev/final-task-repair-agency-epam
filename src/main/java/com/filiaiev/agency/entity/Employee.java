package com.filiaiev.agency.entity;

public class Employee extends Entity{

    private int person_id;

    public int getPersonId() {
        return person_id;
    }

    public void setPersonId(int person_id) {
        this.person_id = person_id;
    }

    @Override
    public String toString() {
        return "Employee #" + id + "\nperson #" + person_id;
    }
}
