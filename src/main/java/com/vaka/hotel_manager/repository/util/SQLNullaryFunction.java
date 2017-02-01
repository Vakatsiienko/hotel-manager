package com.vaka.hotel_manager.repository.util;

import java.sql.SQLException;

/**
 * Created by Iaroslav on 1/22/2017.
 */
public interface SQLNullaryFunction<T> {
    T apply() throws SQLException;
}
