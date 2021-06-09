package com.filiaiev.agency.database.exception;

public class InsertingDuplicateException extends Exception{

    public InsertingDuplicateException(String message) {
        super(message);
    }

    public InsertingDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }
}
