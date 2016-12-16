package com.vaka.hotel_manager.repository.jdbcImpl;

import com.vaka.hotel_manager.context.ApplicationContext;
import com.vaka.hotel_manager.domain.Bill;
import com.vaka.hotel_manager.repository.BillRepository;
import com.vaka.hotel_manager.util.repository.StatementToDomainExtractor;
import com.vaka.hotel_manager.util.exception.RepositoryException;
import com.vaka.hotel_manager.util.repository.CrudRepositoryUtil;
import com.vaka.hotel_manager.util.repository.NamedPreparedStatement;
import com.vaka.hotel_manager.util.repository.DomainToStatementExtractor;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Iaroslav on 12/3/2016.
 */
public class BillRepositoryJdbcImpl implements BillRepository {
    private static final Logger LOG = LoggerFactory.getLogger(BillRepositoryJdbcImpl.class);
    private DataSource dataSource;
    private Map<String, String> queryByClassAndMethodName;

    @Override
    public Bill create(Bill entity) {
        String strQuery = getQueryByClassAndMethodName().get("bill.create");
        try {
            return CrudRepositoryUtil.create(
                    DomainToStatementExtractor::extract,
                    getDataSource(), strQuery, entity);
        } catch (SQLException e) {
            LOG.info(e.getMessage());
            throw new RepositoryException(e);
        }
    }

    @Override
    public Optional<Bill> getByReservationId(Integer id) {
        String strQuery = getQueryByClassAndMethodName().get("bill.getByReservationId");
        try (Connection connection = getDataSource().getConnection();
             NamedPreparedStatement statement = getGetByReservationIdStatement(connection, strQuery, id);
             ResultSet resultSet = statement.executeQuery()){
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
            return CrudRepositoryUtil.getById(StatementToDomainExtractor::extractBill, getDataSource(), strQuery, id);
        } catch (SQLException e) {
            LOG.info(e.getMessage());
            throw new RepositoryException(e);
        }
    }

    @Override
    public boolean delete(Integer id) {
        String strQuery = getQueryByClassAndMethodName().get("bill.delete");
        return CrudRepositoryUtil.delete(getDataSource(), strQuery, id);
    }

    @Override
    public boolean update(Integer id, Bill entity) {
        String strQuery = getQueryByClassAndMethodName().get("bill.update");
        try {
            return CrudRepositoryUtil.update(DomainToStatementExtractor::extract, getDataSource(), strQuery, entity, id);
        } catch (SQLException e) {
            LOG.info(e.getMessage());
            throw new RepositoryException(e);
        }
    }


    public DataSource getDataSource() {
        if (dataSource == null) {
            synchronized (this) {
                if (dataSource == null) {
                    dataSource = ApplicationContext.getInstance().getBean(DataSource.class);
                }
            }
        }
        return dataSource;
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
