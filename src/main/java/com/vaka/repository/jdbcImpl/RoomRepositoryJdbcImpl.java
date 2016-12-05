package com.vaka.repository.jdbcImpl;

import com.vaka.context.ApplicationContext;
import com.vaka.domain.Room;
import com.vaka.domain.RoomClass;
import com.vaka.domain.User;
import com.vaka.repository.RoomRepository;
import com.vaka.util.DomainExtractor;
import com.vaka.util.NamedPreparedStatement;
import com.vaka.util.StatementExtractor;
import com.vaka.util.exception.RepositoryException;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Iaroslav on 12/3/2016.
 */
public class RoomRepositoryJdbcImpl implements RoomRepository {
    private DataSource dataSource;

    @Override
    public List<Room> findAvailableForReservation(RoomClass roomClass, LocalDate arrivalDate, LocalDate departureDate) {
        String strQuery = "SELECT * FROM room r WHERE room_class = :roomClass AND NOT EXISTS (SELECT * FROM reservation WHERE room_id = r.id AND arrival_date < :departureDate AND departure_date > :arrivalDate)";

        try (Connection connection = getDataSource().getConnection();
             NamedPreparedStatement statement = getFindAvailableForReservationStatement(connection, strQuery, roomClass, arrivalDate, departureDate);
             ResultSet resultSet = statement.executeQuery()) {

            List<Room> rooms = new ArrayList<>(30);//TODO get size
            while (resultSet.next()) {
                rooms.add(DomainExtractor.extractRoom(resultSet));
            }
            return rooms;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }
    private NamedPreparedStatement getFindAvailableForReservationStatement(Connection connection, String strQuery, RoomClass roomClass, LocalDate arrivalDate, LocalDate departureDate) throws SQLException {
        NamedPreparedStatement statement = new NamedPreparedStatement(connection, strQuery).init();
        statement.setStatement("roomClass", roomClass.name());
        statement.setStatement("arrivalDate", Date.valueOf(arrivalDate));
        statement.setStatement("departureDate", Date.valueOf(departureDate));
        return statement;
    }

    @Override
    public Room create(Room entity) {
        String strQuery = "INSERT INTO room (created_datetime, number, capacity, cost_per_day, room_class, description) VALUES (:createdDatetime, :number, :capacity, :costPerDay, :roomClass, :description)";
        try (Connection connection = getDataSource().getConnection();
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

    private NamedPreparedStatement createAndExecuteCreateStatement(Connection connection, String strQuery, Room entity, int statementCode) throws SQLException {
        NamedPreparedStatement statement = new NamedPreparedStatement(connection, strQuery, statementCode).init();
        StatementExtractor.extract(entity, statement);
        statement.execute();
        return statement;
    }

    @Override
    public Optional<Room> getById(Integer id) {
        String strQuery = "SELECT * FROM room WHERE id = :id";
        try (Connection connection = getDataSource().getConnection();
             NamedPreparedStatement statement = createGetByIdStatement(connection, strQuery, id);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next())
                return Optional.of(DomainExtractor.extractRoom(resultSet));
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
        String strQuery = "DELETE FROM room WHERE id = :id";
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
    public boolean update(Integer id, Room entity) {
        entity.setId(id);
        String strQuery = "UPDATE room SET number = :number, capacity = :capacity, cost_per_day = :costPerDay, room_class = :roomClass, description = :description WHERE id = :id";
        try (Connection connection = getDataSource().getConnection();
             NamedPreparedStatement statement = createUpdateStatement(connection, strQuery, entity)) {
            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    private NamedPreparedStatement createUpdateStatement(Connection connection, String strQuery, Room entity) throws SQLException {
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
