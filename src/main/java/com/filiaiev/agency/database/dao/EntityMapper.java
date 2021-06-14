package com.filiaiev.agency.database.dao;

import com.filiaiev.agency.entity.Entity;

import java.sql.ResultSet;

public interface EntityMapper<T extends Entity> {

    /**
     * Mapping ResultSet to T instance whose base class in Entity
     *
     * @param rs result set of selecting client from DB
     * @return T class instance
     */
    T mapRow(ResultSet rs);
}
