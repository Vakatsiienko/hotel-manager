package com.vaka.hotel_manager.core.context;


/**
 * Created by Iaroslav on 2/3/2017.
 */
public class ApplicationContextHolder {
    private static ApplicationContext context;

    public static ApplicationContext getContext() {
        return context;
    }

    public void setContext(ApplicationContext context) {
        ApplicationContextHolder.context = context;
    }
}
