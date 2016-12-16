package com.vaka.hotel_manager.context;

import com.vaka.hotel_manager.context.config.ApplicationContextConfig;
import com.vaka.hotel_manager.context.config.PersistenceConfig;
import com.vaka.hotel_manager.util.exception.ApplicationContextInitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Arrays;

/**
 * Created by iaroslav on 07.12.16.
 */
@WebListener
public class ContextInitializer implements ServletContextListener {
    private static final Logger LOG = LoggerFactory.getLogger(ContextInitializer.class);
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            String connectionProp = getClass().getClassLoader().getResource("persistence.properties").getPath();
            URL repository = getClass().getClassLoader().getResource("repository");
            File[] files = new File(repository.getFile()).listFiles();
            String[] filesPath = Arrays.stream(files)
                    .map(File::getPath)
                    .toArray(String[]::new);
            ApplicationContext.getInstance()
                    .init(new ApplicationContextConfig(), new PersistenceConfig(connectionProp, filesPath));
        } catch (NullPointerException | IOException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
            LOG.debug("ApplicationContextInitException", ex);
            throw new ApplicationContextInitException(ex.getMessage(), ex);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
