package com.vaka.hotel_manager.core.context;

import com.vaka.hotel_manager.core.context.config.BeanConfig;
import com.vaka.hotel_manager.util.exception.ApplicationContextInitException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationContext {
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationContext.class);

    private Map<Object, Object> beanByName = new ConcurrentHashMap<>();


    public static ApplicationContext init(BeanConfig... beanConfig) {
        ApplicationContext context = new ApplicationContext();
        BiConsumer<Object, Object> addBean = (name, value) -> {
            if (context.beanByName.containsKey(name)) {
                throw new ApplicationContextInitException("Config bean names collision, context doesn't support " +
                        "multiple bean realization for one name");
            }
            context.beanByName.put(name, value);
        };
        try {
            for (BeanConfig contextConfig : beanConfig) {
                contextConfig.getBeanByBeanName().forEach(addBean::accept);

                Map<Object, Class<?>> classByBeanName = contextConfig.getBeanImplClassByBeanName();
                LOG.debug("Initializing beans.");
                for (Map.Entry<Object, Class<?>> entry : classByBeanName.entrySet()) {
                    Object bean = entry.getValue().getConstructor().newInstance();

                    addBean.accept(entry.getKey(), bean);
                }
            }
            return context;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new ApplicationContextInitException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Object name) {
        return (T) beanByName.get(name);
    }
}
