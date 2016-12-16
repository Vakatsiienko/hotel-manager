package com.vaka.hotel_manager.util.repository;

import java.sql.SQLException;

/**
 * Created by Iaroslav on 12/16/2016.
 */
public interface SQLFunction<T, R> {

    R apply(T t) throws SQLException;
}
