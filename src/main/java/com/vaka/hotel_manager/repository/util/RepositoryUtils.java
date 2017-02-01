package com.vaka.hotel_manager.repository.util;

import org.slf4j.Logger;

import java.util.Arrays;

/**
 * Created by Iaroslav on 2/1/2017.
 */
public class RepositoryUtils {
    public static void logQuery(Logger logger, String strQuery, Object...params) {
        if (logger.isInfoEnabled()) {
            logger.info("Sql query: {},\n Params: {}", strQuery, Arrays.toString(params));
        }
    }
}
