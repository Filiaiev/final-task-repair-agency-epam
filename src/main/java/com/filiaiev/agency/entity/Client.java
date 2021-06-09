package com.filiaiev.agency.entity;

import java.math.BigDecimal;
import java.util.Objects;

public class Client extends Entity{

    private int personId;
    private BigDecimal cash;
    private String preferredLocale;

    public String getPreferredLocale() {
        return preferredLocale;
    }

    public void setPreferredLocale(String preferredLocale) {
        this.preferredLocale = preferredLocale;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int person_id) {
        this.personId = person_id;
    }

    public BigDecimal getCash() {
        return cash;
    }

    public void setCash(BigDecimal cash) {
        this.cash = cash;
    }

    @Override
    public String toString() {
        return "Client #" + id + "\nperson #" + personId + "\ncash: " + cash +
                "\npreffered locale: " + preferredLocale;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return personId == client.personId && Objects.equals(cash, client.cash) && Objects.equals(preferredLocale, client.preferredLocale);
    }
}
