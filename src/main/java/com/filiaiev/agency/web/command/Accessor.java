package com.filiaiev.agency.web.command;

import com.filiaiev.agency.entity.Entity;

import javax.servlet.http.HttpServletRequest;

public interface Accessor {
    boolean isAccessible(HttpServletRequest req, Entity o);
}
