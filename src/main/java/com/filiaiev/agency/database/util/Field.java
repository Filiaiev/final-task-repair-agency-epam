package com.filiaiev.agency.database.util;

public final class Field {

    private Field(){}

    public static final String ID = "id";
    public static final String PERSONS__FNAME = "first_name";
    public static final String PERSONS__MNAME = "middle_name";
    public static final String PERSONS__LNAME = "last_name";
    public static final String PERSONS__BIRTH_DATE = "birthdate";
    public static final String PERSONS__USER_ID = "user_id";

    public static final String CLIENTS__PERSON_ID = "person_id";
    public static final String CLIENTS__CASH = "cash";

    public static final String USERS__EMAIL = "email";
    public static final String USERS__LOGIN = "login";
    public static final String USERS__PASS = "pass";
    public static final String USERS__ROLE_ID = "role_id";

    public static final String ORDERS__CLIENT_ID = "client_id";
    public static final String ORDERS__WORKER_ID = "worker_id";
    public static final String ORDERS__ORDER_DATE = "order_date";
    public static final String ORDERS__COMPLETE_DATE = "complete_date";
    public static final String ORDERS__COST = "cost";
    public static final String ORDERS__COMMENT = "comment";
    public static final String ORDERS__DESCRIPTION = "description";
    public static final String ORDERS__STATUS_ID = "status_id";

}
