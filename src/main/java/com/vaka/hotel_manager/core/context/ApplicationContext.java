package com.vaka.hotel_manager.core.context;

import com.vaka.hotel_manager.core.context.config.ApplicationContextConfig;
import com.vaka.hotel_manager.core.context.config.PersistenceConfig;
import com.vaka.hotel_manager.util.exception.ApplicationContextInitException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationContext {
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationContext.class);

    private Map<Class<?>, Object> beanByName;

    private static ApplicationContext instance;

    @Getter
    private Map<String, String> queryByClassAndMethodName;

    @Getter
    private AtomicInteger idCounter = new AtomicInteger();

    public ApplicationContext init(ApplicationContextConfig contextConfig, PersistenceConfig persistenceConfig) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        beanByName = new ConcurrentHashMap<>();
        Map<Class<?>, Class<?>> classByBeanName = contextConfig.getImplClassByBeanName();
        LOG.debug("Initializing beans.");
        for (Map.Entry<Class<?>, Class<?>> entry : classByBeanName.entrySet()) {
            if (contextConfig.getImplBeanByBeanName().containsKey(entry.getKey()))
                throw new ApplicationContextInitException("Config bean names collision, not support multiple bean realization for one name");
            Object bean = entry.getValue().getConstructor().newInstance();
            beanByName.put(entry.getKey(), bean);
        }
        contextConfig.getImplBeanByBeanName().forEach(beanByName::put);
        beanByName.put(DataSource.class, persistenceConfig.dataSource());
        queryByClassAndMethodName = persistenceConfig.queryByClassAndMethodName();
        return this;
    }

    public static ApplicationContext getInstance() {
        if (instance == null) {
            synchronized (ApplicationContext.class) {
                if (instance == null)
                    instance = new ApplicationContext();
            }
        }
        return instance;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<?> clazz) {
        return (T) beanByName.get(clazz);
    }
}
