package com.filiaiev.agency.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Order extends Entity implements Comparable<Order>{

    private int client_id;
    private Integer worker_id;
    private Timestamp order_date;
    private Timestamp complete_date;
    private BigDecimal cost;
    private String comment;
    private String description;
    private int status_id;

    public int getClientId() {
        return client_id;
    }

    public void setClientId(int client_id) {
        this.client_id = client_id;
    }

    public Integer getWorkerId() {
        return worker_id;
    }

    public void setWorkerId(Integer worker_id) {
        this.worker_id = worker_id;
    }

    public Timestamp getOrderDate() {
        return order_date;
    }

    public void setOrderDate(Timestamp order_date) {
        this.order_date = order_date;
    }

    public Timestamp getCompleteDate() {
        return complete_date;
    }

    public void setCompleteDate(Timestamp complete_date) {
        this.complete_date = complete_date;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatusId() {
        return status_id;
    }

    public void setStatusId(int status_id) {
        this.status_id = status_id;
    }

    @Override
    public String toString() {
        return "Order #" + id + "\nclient #: " + client_id + "\nworker #: " + worker_id
                + "\norder date: " + order_date + "\ncomplete date: " + complete_date
                + "\ncost: " + cost + "\ncomment: " + comment +
                "\ndescription: " + description + "\nstatus #" + status_id;
    }

    @Override
    public int compareTo(Order o) {
        return status_id-o.status_id;
    }
}
