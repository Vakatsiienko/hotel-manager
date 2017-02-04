package com.vaka.hotel_manager.core.context.config;

import java.util.Map;

/**
 * Created by Iaroslav on 2/3/2017.
 */
public interface BeanConfig {

    Map<Object, Class<?>> getBeanImplClassByBeanName();

    Map<Object, Object> getBeanByBeanName();
}
