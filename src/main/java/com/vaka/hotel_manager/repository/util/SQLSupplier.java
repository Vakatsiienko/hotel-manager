package com.vaka.hotel_manager.repository.util;

import java.sql.SQLException;

/**
 * Created by Iaroslav on 2/3/2017.
 */
@FunctionalInterface
public interface SQLSupplier<T> {
    T get() throws SQLException;
}
