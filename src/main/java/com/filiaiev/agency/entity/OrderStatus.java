package com.filiaiev.agency.entity;

public enum OrderStatus {
    CREATED, WAITING_FOR_PAYMENT, PAID, CANCELED, IN_WORK, COMPLETED;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }

}
