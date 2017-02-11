package com.vaka.hotel_manager.core.context.config;

import com.vaka.hotel_manager.core.tx.ConnectionManager;
import com.vaka.hotel_manager.core.tx.JdbcTransactionManagerImpl;
import com.vaka.hotel_manager.core.tx.TransactionManager;
import com.vaka.hotel_manager.repository.util.SQLExceptionParser;
import com.vaka.hotel_manager.repository.util.JdbcCrudHelper;
import com.vaka.hotel_manager.repository.util.mysql.MysqlExceptionParser;
import com.vaka.hotel_manager.util.SqlParser;
import com.vaka.hotel_manager.util.exception.ApplicationContextInitException;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.util.*;

/**
 * Created by Iaroslav on 12/3/2016.
 */
public class PersistenceConfig implements BeanConfig {
    private String[] sqlPaths;
    private String connectionPropertiesPath;
    private Map<Object, Object> beanByBeanName = new HashMap<>();
    {
        connectionPropertiesPath = getClass().getClassLoader().getResource("persistence.properties").getPath();
        URL repository = getClass().getClassLoader().getResource("repository");
        File[] files = new File(repository.getFile()).listFiles();
        sqlPaths = Arrays.stream(files)
                .map(File::getPath)
                .toArray(String[]::new);

        javax.sql.DataSource dataSource = dataSource();

        beanByBeanName.put(javax.sql.DataSource.class, dataSource);

        TransactionManager transactionManager = new JdbcTransactionManagerImpl(dataSource::getConnection, TransactionManager.TRANSACTION_READ_COMMITTED);
        beanByBeanName.put(TransactionManager.class, transactionManager);

        ConnectionManager connectionManager = (ConnectionManager) transactionManager;
        beanByBeanName.put(ConnectionManager.class, connectionManager);

        JdbcCrudHelper crudHelper = new JdbcCrudHelper(connectionManager);
        beanByBeanName.put(JdbcCrudHelper.class, crudHelper);

        beanByBeanName.put("queryByClassAndMethodName", queryByClassAndMethodName());

        beanByBeanName.put(SQLExceptionParser.class, new MysqlExceptionParser());
        beanByBeanName.put("domainFieldNameByTableFieldName", Collections.unmodifiableMap(domainFieldNameByTableFieldName()));
    }

    private void setSystemProperties() throws IOException {
        try {
            Properties props = new Properties();
            props.load(new FileInputStream(connectionPropertiesPath));
            TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of(props.getProperty("dataSource.timeZone"))));
        } catch (IOException e) {
            throw new ApplicationContextInitException(e);
        }
    }

    private Map<String, String> queryByClassAndMethodName() {
        SqlParser parser = new SqlParser();
        parser.parseFiles(sqlPaths);
        return parser.getQueryByClassAndMethodName();
    }

    private javax.sql.DataSource dataSource() {
        try {
            setSystemProperties();
            DataSource datasource = new DataSource();
            datasource.setPoolProperties(poolProperties());
            return datasource;
        } catch (IOException e) {
            throw new ApplicationContextInitException(e);
        }
    }

    private PoolProperties poolProperties() throws IOException {
        try {
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
        } catch (IOException e) {
            throw new ApplicationContextInitException(e);
        }
    }

    private Map<String, String> domainFieldNameByTableFieldName(){
        Map<String, String> domainFieldNameByTableFieldName = new HashMap<>();
        //TODO make field "column" annotation and collector
        domainFieldNameByTableFieldName.put("id", "id");
        domainFieldNameByTableFieldName.put("created_datetime", "createdDatetime");
        domainFieldNameByTableFieldName.put("email", "email");
        domainFieldNameByTableFieldName.put("password", "password");
        domainFieldNameByTableFieldName.put("name", "name");
        domainFieldNameByTableFieldName.put("role", "role");
        domainFieldNameByTableFieldName.put("phone_number", "phoneNumber");
        domainFieldNameByTableFieldName.put("vk_id", "vkId");
        domainFieldNameByTableFieldName.put("reservation_id", "reservationId");
        domainFieldNameByTableFieldName.put("total_cost", "totalCost");
        domainFieldNameByTableFieldName.put("paid", "paid");
        domainFieldNameByTableFieldName.put("number", "number");
        domainFieldNameByTableFieldName.put("cost_per_day", "costPerDay");
        domainFieldNameByTableFieldName.put("room_class_id", "roomClassId");
        domainFieldNameByTableFieldName.put("user_id", "userId");
        domainFieldNameByTableFieldName.put("room_id", "roomId");
        domainFieldNameByTableFieldName.put("guests", "guests");
        domainFieldNameByTableFieldName.put("requested_room_class_id", "roomClassId");//TODO change to proper value
        domainFieldNameByTableFieldName.put("status", "status");
        domainFieldNameByTableFieldName.put("arrival_date", "arrivalDate");
        domainFieldNameByTableFieldName.put("departure_date", "departureDate");
        return domainFieldNameByTableFieldName;
    }


    @Override
    public Map<Object, Class<?>> getBeanImplClassByBeanName() {
        return Collections.emptyMap();
    }

    @Override
    public Map<Object, Object> getBeanByBeanName() {
        return beanByBeanName;
    }
}
