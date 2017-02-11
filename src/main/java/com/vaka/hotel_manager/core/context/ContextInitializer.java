package com.vaka.hotel_manager.core.context;

import com.vaka.hotel_manager.core.context.config.ApplicationContextConfig;
import com.vaka.hotel_manager.core.context.config.PersistenceConfig;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Created by iaroslav on 07.12.16.
 */
@WebListener
public class ContextInitializer implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {

        new ApplicationContextHolder().setContext(ApplicationContext
                .init(new ApplicationContextConfig(), new PersistenceConfig()));
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
