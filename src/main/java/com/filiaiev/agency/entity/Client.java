package com.filiaiev.agency.entity;

import java.math.BigDecimal;

public class Client extends Entity{

    private int person_id;
    private BigDecimal cash;

    public int getPersonId() {
        return person_id;
    }

    public void setPersonId(int person_id) {
        this.person_id = person_id;
    }

    public BigDecimal getCash() {
        return cash;
    }

    public void setCash(BigDecimal cash) {
        this.cash = cash;
    }

    @Override
    public String toString() {
        return "Client #" + id + "\nperson #" + person_id + "\ncash: " + cash;
    }
}
