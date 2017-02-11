package com.vaka.hotel_manager.repository.util;

import com.vaka.hotel_manager.repository.exception.ConstraintViolationException;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * Created by Iaroslav on 2/6/2017.
 */
public interface SQLExceptionParser {
    ConstraintViolationException parse(SQLIntegrityConstraintViolationException e);
}
