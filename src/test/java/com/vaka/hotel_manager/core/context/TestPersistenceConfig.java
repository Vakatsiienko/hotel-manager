package com.vaka.hotel_manager.core.context;

import com.vaka.hotel_manager.core.context.config.PersistenceConfig;
import com.vaka.hotel_manager.core.tx.ConnectionManager;

/**
 * Created by Iaroslav on 2/9/2017.
 */
public class TestPersistenceConfig extends PersistenceConfig {
    {
        getBeanByBeanName().remove(ConnectionManager.class);
    }
}
