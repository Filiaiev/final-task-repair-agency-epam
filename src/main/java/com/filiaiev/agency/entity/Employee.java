package com.filiaiev.agency.entity;

public class Employee extends Entity{

    private int personId;

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int person_id) {
        this.personId = person_id;
    }

    @Override
    public String toString() {
        return "Employee #" + id + "\nperson #" + personId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return personId == employee.personId;
    }
}
