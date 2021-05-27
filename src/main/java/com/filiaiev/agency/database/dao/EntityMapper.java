package com.filiaiev.agency.database.dao;

import java.sql.ResultSet;

public interface EntityMapper<T> {

    T mapRow(ResultSet rs);
}
