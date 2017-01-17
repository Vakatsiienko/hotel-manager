package com.vaka.hotel_manager.repository.jdbcImpl;

import com.vaka.hotel_manager.core.context.ApplicationContext;
import com.vaka.hotel_manager.core.tx.ConnectionProvider;
import com.vaka.hotel_manager.domain.Bill;
import com.vaka.hotel_manager.repository.BillRepository;
import com.vaka.hotel_manager.repository.util.JdbcCrudHelper;
import com.vaka.hotel_manager.repository.util.StatementToDomainExtractor;
import com.vaka.hotel_manager.util.exception.RepositoryException;
import com.vaka.hotel_manager.repository.util.NamedPreparedStatement;
import com.vaka.hotel_manager.repository.util.DomainToStatementExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
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
    private ConnectionProvider connectionProvider;
    private JdbcCrudHelper crudHelper;

    @Override
    public Bill create(Bill entity) {
        String strQuery = getQueryByClassAndMethodName().get("bill.create");
        try {
            return getCrudHelper().create(
                    DomainToStatementExtractor::extract,
                    strQuery, entity);
        } catch (SQLException e) {
            LOG.info(e.getMessage());
            throw new RepositoryException(e);
        }
    }

    @Override
    public Optional<Bill> getByReservationId(Integer id) {
        String strQuery = getQueryByClassAndMethodName().get("bill.getByReservationId");
        try (NamedPreparedStatement statement = getGetByReservationIdStatement(getConnectionProvider().getConnection(), strQuery, id);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next())
                return Optional.of(StatementToDomainExtractor.extractBill(resultSet));
            else return Optional.empty();
        } catch (SQLException e) {
            LOG.info(e.getMessage());
            throw new RepositoryException(e);
        }
    }

    private NamedPreparedStatement getGetByReservationIdStatement(Connection connection, String strQuery, Integer reservationId) throws SQLException {
        NamedPreparedStatement statement = new NamedPreparedStatement(connection, strQuery).init();
        statement.setStatement("reservationId", reservationId);
        return statement;
    }

    @Override
    public Optional<Bill> getById(Integer id) {
        String strQuery = getQueryByClassAndMethodName().get("bill.getById");
        try {
            return getCrudHelper().getById(StatementToDomainExtractor::extractBill, strQuery, id);
        } catch (SQLException e) {
            LOG.info(e.getMessage());
            throw new RepositoryException(e);
        }
    }

    @Override
    public boolean delete(Integer id) {
        String strQuery = getQueryByClassAndMethodName().get("bill.delete");
        try {
            return getCrudHelper().delete(strQuery, id);
        } catch (SQLException e) {
            LOG.info(e.getMessage());
            throw new RepositoryException(e);
        }
    }

    @Override
    public boolean update(Integer id, Bill entity) {
        String strQuery = getQueryByClassAndMethodName().get("bill.update");
        try {
            return getCrudHelper().update(DomainToStatementExtractor::extract, strQuery, entity, id);
        } catch (SQLException e) {
            LOG.info(e.getMessage());
            throw new RepositoryException(e);
        }
    }



    public ConnectionProvider getConnectionProvider() {
        if (connectionProvider == null) {
            synchronized (this) {
                if (connectionProvider == null) {
                    connectionProvider = ApplicationContext.getInstance().getBean(ConnectionProvider.class);
                }
            }
        }
        return connectionProvider;
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
