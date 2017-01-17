package com.vaka.hotel_manager.core.tx;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Iaroslav on 12/30/2016.
 */
public interface ConnectionProvider {

    Connection getConnection() throws SQLException;
}
