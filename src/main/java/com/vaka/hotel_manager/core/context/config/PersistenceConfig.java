package com.vaka.hotel_manager.core.context.config;

import com.vaka.hotel_manager.util.SqlParser;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.time.ZoneId;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;

/**
 * Created by Iaroslav on 12/3/2016.
 */
public class PersistenceConfig {
    private static final Logger LOG = LoggerFactory.getLogger(PersistenceConfig.class);

    private String[] sqlPaths;
    private String connectionPropertiesPath;

    public PersistenceConfig(String connectionPropertiesPath, String... sqlPath) {
        this.connectionPropertiesPath = connectionPropertiesPath;
        this.sqlPaths = sqlPath;
    }

    private void setSystemProperties() throws IOException {
        Properties props = new Properties();
        props.load(new FileInputStream(connectionPropertiesPath));
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of(props.getProperty("dataSource.timeZone"))));
    }

    public javax.sql.DataSource dataSource() throws IOException {
        setSystemProperties();
        DataSource datasource = new DataSource();
        datasource.setPoolProperties(poolProperties());
        return datasource;
    }

    public Map<String, String> queryByClassAndMethodName() throws IOException {
        return new SqlParser(sqlPaths).createAndGetQueryByClassAndMethodName();
    }

    public PoolProperties poolProperties() throws IOException {
        Properties props = new Properties();
        props.load(new FileInputStream(connectionPropertiesPath));
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
