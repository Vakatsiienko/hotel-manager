package com.vaka.hotel_manager.core.context;

import com.vaka.hotel_manager.core.context.config.ApplicationContextConfig;
import com.vaka.hotel_manager.core.context.config.PersistenceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Created by iaroslav on 07.12.16.
 */
@WebListener
public class ContextInitializer implements ServletContextListener {
    private static final Logger LOG = LoggerFactory.getLogger(ContextInitializer.class);
    @Override
    public void contextInitialized(ServletContextEvent sce) {

        new ApplicationContextHolder().setContext(ApplicationContext
                .init(new ApplicationContextConfig(), new PersistenceConfig()));
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
