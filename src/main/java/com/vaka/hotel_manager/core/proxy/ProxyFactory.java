package com.vaka.hotel_manager.core.proxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

/**
 * Created by Iaroslav on 2/6/2017.
 */
public class ProxyFactory {
    /**
     * @param target proxying implementation target class
     * @param iface  interface of proxy
     * @param proxyClass invocation handler class
     * @param <T>    the target of proxy
     * @param <R>    invocation handler
     * @return proxied instance
     */
    @SuppressWarnings("unchecked")
    public static <I, T extends I, R extends InvocationHandler> T getProxiedInstance(T target, Class<I> iface, Class<R> proxyClass) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        Constructor<R> constructorR = proxyClass.getConstructor(Object.class);
        R handler = constructorR.newInstance(target);
        return (T) Proxy.newProxyInstance(
                iface.getClassLoader(),
                new Class<?>[]{iface},
                handler);
    }
}
