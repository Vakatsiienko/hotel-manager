package com.vaka.context;

import com.vaka.context.config.ApplicationContextConfig;
import com.vaka.context.config.PersistenceConfig;
import com.vaka.util.exception.ApplicationContextInitException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Initializing in MainServlet
 * Created by Iaroslav on 11/24/2016.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationContext {

    private Map<Class<?>, Object> beanByInterface;

    private static ApplicationContext instance;

    @Getter
    private Map<String, String> queryByClassAndMethodName;

    @Getter
    private AtomicInteger idCounter = new AtomicInteger();

    public ApplicationContext init(ApplicationContextConfig contextConfig, PersistenceConfig persistenceConfig) {
        beanByInterface = new ConcurrentHashMap<>();
        Map<Class<?>, Class<?>> classByBeanName = contextConfig.getImplClassByBeanName();
        classByBeanName.keySet().stream().forEach(k -> {
            try {
                Object bean = classByBeanName.get(k).getConstructor().newInstance();
                beanByInterface.put(k, bean);
            } catch (Exception e) {
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
