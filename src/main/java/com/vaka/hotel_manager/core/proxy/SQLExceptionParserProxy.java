package com.vaka.hotel_manager.core.proxy;

import com.vaka.hotel_manager.core.context.ApplicationContextHolder;
import com.vaka.hotel_manager.repository.util.SQLExceptionParser;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * Created by Iaroslav on 2/6/2017.
 */
public class SQLExceptionParserProxy<T> implements InvocationHandler {
    private final T underlying;
    private SQLExceptionParser parser;

    public SQLExceptionParserProxy(T underlying) {
        this.underlying = underlying;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            return method.invoke(underlying, args);
        } catch (InvocationTargetException e) {
            if (e.getTargetException().getCause() instanceof SQLIntegrityConstraintViolationException)
                throw getParser().parse((SQLIntegrityConstraintViolationException) e.getTargetException().getCause());
            else throw e;
        }
    }

    public SQLExceptionParser getParser() {
        if (parser == null) {
            parser = ApplicationContextHolder.getContext().getBean(SQLExceptionParser.class);
        }
        return parser;
    }
}
