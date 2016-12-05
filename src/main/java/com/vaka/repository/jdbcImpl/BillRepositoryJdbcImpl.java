package com.vaka.repository.jdbcImpl;

import com.vaka.context.ApplicationContext;
import com.vaka.domain.Bill;
import com.vaka.domain.Reservation;
import com.vaka.domain.User;
import com.vaka.repository.BillRepository;
import com.vaka.util.DomainExtractor;
import com.vaka.util.NamedPreparedStatement;
import com.vaka.util.StatementExtractor;
import com.vaka.util.exception.RepositoryException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

/**
 * Created by Iaroslav on 12/3/2016.
 */
public class BillRepositoryJdbcImpl implements BillRepository {
    private DataSource dataSource;

    @Override
    public Bill create(Bill entity) {
        String strQuery = "INSERT INTO bill (created_datetime, reservation_id, total_cost, paid) VALUES (:createdDatetime, :reservationId, :totalCost, :paid)";
        try (Connection connection = getDataSource().getConnection();//TODO move to JdbcUtil
             NamedPreparedStatement statement = createAndExecuteCreateStatement(connection, strQuery, entity, Statement.RETURN_GENERATED_KEYS);
             ResultSet resultSet = statement.getGenerationKeys()) {
            if (resultSet.next()) {
                entity.setId(resultSet.getInt(1));
                return entity;
            } else throw new SQLException("ID wasn't returned");
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }
    private NamedPreparedStatement createAndExecuteCreateStatement(Connection connection, String strQuery, Bill entity, int statementCode) throws SQLException {
        NamedPreparedStatement statement = new NamedPreparedStatement(connection, strQuery, statementCode).init();
        StatementExtractor.extract(entity, statement);
        statement.execute();
        return statement;
    }
    @Override
    public Optional<Bill> getByReservationId(Integer id) {
        try {
            Connection connection = getDataSource().getConnection();
            String strQuery = "SELECT * FROM bill b INNER JOIN reservation res ON b.reservation_id = res.id LEFT JOIN room r ON res.room_id = r.id INNER JOIN user u ON res.user_id = u.id WHERE b.reservation_id = :reservationId";
            NamedPreparedStatement statement = new NamedPreparedStatement(connection, strQuery).init();
            statement.setStatement("reservationId", id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next())
                return Optional.of(DomainExtractor.extractBill(resultSet));
            else return Optional.empty();
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Optional<Bill> getById(Integer id) {
        String strQuery = "SELECT * FROM bill b INNER JOIN reservation res ON b.reservation_id = res.id LEFT JOIN room r ON res.room_id = r.id INNER JOIN user u ON res.user_id = u.id WHERE b.id = :id";
        try (Connection connection = getDataSource().getConnection();
             NamedPreparedStatement statement = createGetByIdStatement(connection, strQuery, id);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next())
                return Optional.of(DomainExtractor.extractBill(resultSet));
            else return Optional.empty();
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }
    private NamedPreparedStatement createGetByIdStatement(Connection connection, String strQuery, Integer id) throws SQLException {
        NamedPreparedStatement statement = new NamedPreparedStatement(connection, strQuery).init();
        statement.setStatement("id", id);
        return statement;
    }
    @Override
    public boolean delete(Integer id) {
        String strQuery = "DELETE FROM bill WHERE id = :id";
        try (Connection connection = getDataSource().getConnection();
             NamedPreparedStatement statement = createDeleteStatement(connection, strQuery, id)) {
            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    private NamedPreparedStatement createDeleteStatement(Connection connection, String strQuery, Integer id) throws SQLException {
        NamedPreparedStatement statement = new NamedPreparedStatement(connection, strQuery).init();
        statement.setStatement("id", id);
        return statement;
    }

    @Override
    public boolean update(Integer id, Bill entity) {
        entity.setId(id);
        String strQuery = "UPDATE bill SET reservation_id = :reservationId, total_cost = :totalCost, paid = :paid WHERE id = :id";
        try (Connection connection = getDataSource().getConnection();
             NamedPreparedStatement statement = createUpdateStatement(connection, strQuery, entity)) {
            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    private NamedPreparedStatement createUpdateStatement(Connection connection, String strQuery, Bill entity) throws SQLException {
        NamedPreparedStatement statement = new NamedPreparedStatement(connection, strQuery).init();
        StatementExtractor.extract(entity, statement);
        return statement;
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
}
