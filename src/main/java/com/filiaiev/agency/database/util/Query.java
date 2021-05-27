package com.filiaiev.agency.database.util;

public class Query {

    private Query(){}







    public static final String SQL__INSERT_ORDER = "INSERT INTO order_headers" +
            "(client_id, description) VALUES(?, ?)";

}
