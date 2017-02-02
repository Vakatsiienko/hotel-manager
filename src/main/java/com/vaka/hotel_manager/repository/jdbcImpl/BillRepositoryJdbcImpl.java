package com.vaka.hotel_manager.repository.jdbcImpl;

import com.vaka.hotel_manager.core.context.ApplicationContext;
import com.vaka.hotel_manager.core.tx.ConnectionManager;
import com.vaka.hotel_manager.domain.entities.Bill;
import com.vaka.hotel_manager.repository.BillRepository;
import com.vaka.hotel_manager.repository.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Iaroslav on 12/3/2016.
 */
public class BillRepositoryJdbcImpl implements BillRepository {
    private static final Logger LOG = LoggerFactory.getLogger(BillRepositoryJdbcImpl.class);
    private Map<String, String> queryByClassAndMethodName;
    private ConnectionManager connectionManager;
    private JdbcCrudHelper crudHelper;

    @Override
    public Optional<Bill> getByReservationId(Integer id) {
        String strQuery = getQueryByClassAndMethodName().get("bill.getByReservationId");
        RepositoryUtils.logQuery(LOG, strQuery, id);
        return getConnectionManager().withConnection(connection -> {
            try (NamedPreparedStatement statement = getGetByReservationIdStatement(connection, strQuery, id);
                 ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next())
                    return Optional.of(StatementToDomainExtractor.extractBill(resultSet));
                else return Optional.empty();
            }
        });
    }

    private NamedPreparedStatement getGetByReservationIdStatement(Connection connection, String strQuery, Integer reservationId) throws SQLException {
        NamedPreparedStatement statement = NamedPreparedStatement.create(connection, strQuery);
        statement.setStatement("billReservationId", reservationId);
        return statement;
    }

    @Override
    public Bill create(Bill entity) {
        String strQuery = getQueryByClassAndMethodName().get("bill.create");
        RepositoryUtils.logQuery(LOG, strQuery, entity);
        return getCrudHelper().create(
                DomainToStatementExtractor::extract,
                strQuery, entity);
    }

    @Override
    public Optional<Bill> getById(Integer id) {
        String strQuery = getQueryByClassAndMethodName().get("bill.getById");
        RepositoryUtils.logQuery(LOG, strQuery, id);
        return getCrudHelper().getById(StatementToDomainExtractor::extractBill, strQuery, id);
    }

    @Override
    public boolean delete(Integer id) {
        String strQuery = getQueryByClassAndMethodName().get("bill.delete");
        RepositoryUtils.logQuery(LOG, strQuery, id);
        return getCrudHelper().delete(strQuery, id);

    }

    @Override
    public boolean update(Integer id, Bill entity) {
        String strQuery = getQueryByClassAndMethodName().get("bill.update");
        RepositoryUtils.logQuery(LOG, strQuery, id, entity);
        return getCrudHelper().update(DomainToStatementExtractor::extract, strQuery, entity, id);

    }


    public ConnectionManager getConnectionManager() {
        if (connectionManager == null) {
            synchronized (this) {
                if (connectionManager == null) {
                    connectionManager = ApplicationContext.getInstance().getBean(ConnectionManager.class);
                }
            }
        }
        return connectionManager;
    }

    public JdbcCrudHelper getCrudHelper() {
        if (crudHelper == null) {
            synchronized (this) {
                if (crudHelper == null) {
                    crudHelper = ApplicationContext.getInstance().getBean(JdbcCrudHelper.class);
                }
            }
        }
        return crudHelper;
    }

    public Map<String, String> getQueryByClassAndMethodName() {
        if (queryByClassAndMethodName == null) {
            synchronized (this) {
                if (queryByClassAndMethodName == null) {
                    queryByClassAndMethodName = ApplicationContext.getInstance().getQueryByClassAndMethodName();
                }
            }
        }
        return queryByClassAndMethodName;
    }
}
