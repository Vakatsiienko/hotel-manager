package com.vaka.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Initializing in UserServlet
 * Created by Iaroslav on 11/24/2016.
 */
public class ApplicationContext {

    private static Map<Class<?>, Object> beanByInterface;
    
    private static ApplicationContext instance;

    private ApplicationContext() {
        beanByInterface = new ConcurrentHashMap<>();

    }

    public static ApplicationContext getInstance(){
        if (instance == null){
            synchronized (ApplicationContext.class) {
                if (instance == null)
                    instance = new ApplicationContext();
            }
        }
        return instance;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<?> clazz) {
        return (T) beanByInterface.get(clazz);
    }
}
