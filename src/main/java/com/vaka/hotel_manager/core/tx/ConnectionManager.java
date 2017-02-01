package com.vaka.hotel_manager.core.tx;

import com.vaka.hotel_manager.repository.util.SQLFunction;

import java.sql.Connection;

/**
 * Created by Iaroslav on 12/30/2016.
 */
public interface ConnectionManager {

    <T> T withConnection(SQLFunction<Connection, T> withCon);
}
