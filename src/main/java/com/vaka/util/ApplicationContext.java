package com.vaka.util;

import com.vaka.config.ApplicationContextConfig;
import com.vaka.domain.Manager;
import com.vaka.util.exception.ApplicationContextInitException;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Initializing in UserServlet
 * Created by Iaroslav on 11/24/2016.
 */
public class ApplicationContext {

    private static Map<Class<?>, Object> beanByInterface;

//    private static ApplicationContext instance;

    @Getter
    private static AtomicInteger idCounter;
    private static ApplicationContextConfig contextConfig;

    static {
        beanByInterface = new ConcurrentHashMap<>();
        contextConfig = new ApplicationContextConfig();
        idCounter = new AtomicInteger(0);
    }

    private ApplicationContext() {

    }

    public static void init() {
        Map<Class<?>, Class<?>> classByBeanName = contextConfig.getImplClassByBeanName();
        classByBeanName.keySet().stream().forEach(k -> {
            try {
                Object bean = classByBeanName.get(k).getConstructor().newInstance();
                beanByInterface.put(k, bean);
            } catch (Exception e) {
                throw new ApplicationContextInitException(e);
            }
        });
    }

//    public static ApplicationContext getInstance() {
//        if (instance == null) {
//            synchronized (ApplicationContext.class) {
//                if (instance == null)
//                    instance = new ApplicationContext();
//            }
//        }
//        return instance;
//    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<?> clazz) {
        return (T) beanByInterface.get(clazz);
    }
}
