package com.filiaiev.agency.web.command;

import com.filiaiev.agency.entity.Entity;

import javax.servlet.http.HttpServletRequest;

// Interface that provide method could be used for checking access to Entity object
public interface Accessor {
    boolean isAccessible(HttpServletRequest req, Entity o);
}
