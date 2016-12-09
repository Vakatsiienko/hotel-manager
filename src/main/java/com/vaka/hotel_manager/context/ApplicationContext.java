package com.vaka.hotel_manager.context;

import com.vaka.hotel_manager.context.config.ApplicationContextConfig;
import com.vaka.hotel_manager.context.config.PersistenceConfig;
import com.vaka.hotel_manager.util.exception.ApplicationContextInitException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationContext {
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationContext.class);

    private Map<Class<?>, Object> beanByInterface;

    private static ApplicationContext instance;

    @Getter
    private Map<String, String> queryByClassAndMethodName;

    @Getter
    private AtomicInteger idCounter = new AtomicInteger();

    public ApplicationContext init(ApplicationContextConfig contextConfig, PersistenceConfig persistenceConfig) throws IOException {
        beanByInterface = new ConcurrentHashMap<>();
        Map<Class<?>, Class<?>> classByBeanName = contextConfig.getImplClassByBeanName();
        classByBeanName.keySet().forEach(k -> {
            try {
                Object bean = classByBeanName.get(k).getConstructor().newInstance();
                beanByInterface.put(k, bean);
            } catch (InstantiationException  | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                LOG.error(e.getMessage(), e);
                throw new ApplicationContextInitException(e);
            }
        });
        beanByInterface.put(DataSource.class, persistenceConfig.dataSource());
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
        return (T) beanByInterface.get(clazz);
    }
}
