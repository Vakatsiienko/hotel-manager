package com.vaka.hotel_manager.util.repository;

import java.sql.SQLException;

/**
 * Created by Iaroslav on 12/16/2016.
 */
public interface SQLBiFunction<T, U, R> {

    R apply(T t, U u) throws SQLException;
}
