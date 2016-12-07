package com.vaka.hotel_manager.context.config;

import com.vaka.hotel_manager.util.SqlParser;
import com.vaka.hotel_manager.util.exception.CreatingException;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import java.io.*;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Iaroslav on 12/3/2016.
 */
public class PersistenceConfig {

    private String[] sqlFileURLs;

    public PersistenceConfig(String... sqlFileURLs) {
        this.sqlFileURLs = sqlFileURLs;
    }

    public javax.sql.DataSource dataSource() throws IOException {
        DataSource datasource = new DataSource();
        datasource.setPoolProperties(poolProperties());
        return datasource;
    }

    public Map<String, String> queryByClassAndMethodName() throws IOException {
        return new SqlParser(sqlFileURLs).createAndGetQueryByClassAndMethodName();
    }

    public PoolProperties poolProperties() throws IOException {
        Properties props = new Properties();
        props.load(getClass().getClassLoader().getResourceAsStream("persistence.properties"));

        PoolProperties p = new PoolProperties();
        p.setUrl(props.getProperty("dataSource.url"));
        p.setDriverClassName(props.getProperty("dataSource.driver"));
        p.setUsername(props.getProperty("dataSource.username"));
        p.setPassword(props.getProperty("dataSource.password"));
        p.setMaxActive(Integer.valueOf(props.getProperty("pool.maxActive")));
        p.setMaxWait(Integer.valueOf(props.getProperty("pool.maxWait")));
        p.setTestWhileIdle(Boolean.valueOf(props.getProperty("pool.testWhileIdle")));
        p.setTestOnBorrow(Boolean.valueOf("pool.testOnBorrow"));
        p.setValidationQuery(props.getProperty("pool.validationQuery"));
        p.setTestOnReturn(Boolean.valueOf(props.getProperty("pool.testOnReturn")));
        p.setValidationInterval(Integer.valueOf(props.getProperty("pool.validationInterval")));
        p.setTimeBetweenEvictionRunsMillis(Integer.valueOf(props.getProperty("pool.timeBetweenEvictionRunsMillis")));
        p.setInitialSize(Integer.valueOf(props.getProperty("pool.initialSize")));
        p.setJmxEnabled(Boolean.valueOf(props.getProperty("pool.jmxEnabled")));
        p.setRemoveAbandonedTimeout(Integer.valueOf(props.getProperty("pool.removeAbandonedTimeout")));
        p.setMinEvictableIdleTimeMillis(Integer.valueOf(props.getProperty("pool.minEvictableIdleTimeMillis")));
        p.setMinIdle(Integer.valueOf(props.getProperty("pool.minIdle")));
        p.setLogAbandoned(Boolean.valueOf(props.getProperty("pool.logAbandoned")));
        p.setRemoveAbandoned(Boolean.valueOf(props.getProperty("pool.removeAbandoned")));
        p.setJdbcInterceptors(props.getProperty("pool.jdbcInterceptors"));

        return p;
    }
}
