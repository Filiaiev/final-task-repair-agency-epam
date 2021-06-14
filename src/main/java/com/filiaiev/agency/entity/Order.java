package com.filiaiev.agency.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

public class Order extends Entity implements Comparable<Order>{

    private int clientId;
    private Integer workerId;
    private Timestamp orderDate;
    private Timestamp completeDate;
    private BigDecimal cost;
    private String comment;
    private String description;
    private int statusId;
    private String statusName;

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public Integer getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Integer workerId) {
        this.workerId = workerId;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public Timestamp getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(Timestamp completeDate) {
        this.completeDate = completeDate;
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
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    @Override
    public String toString() {
        return "Order #" + id + "\nclient #: " + clientId + "\nworker #: " + workerId
                + "\norder date: " + orderDate + "\ncomplete date: " + completeDate
                + "\ncost: " + cost + "\ncomment: " + comment +
                "\ndescription: " + description + "\nstatus #" + statusId;
    }

    @Override
    public int compareTo(Order o) {
        return statusId-o.statusId;
    }

    /**
     * Equals compares all fields excluding statusName,
     * because it does not appears at order_headers table in DB
    */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return clientId == order.clientId && statusId == order.statusId && Objects.equals(workerId, order.workerId) && Objects.equals(orderDate, order.orderDate) && Objects.equals(completeDate, order.completeDate) && Objects.equals(cost, order.cost) && Objects.equals(comment, order.comment) && Objects.equals(description, order.description);
    }
}
