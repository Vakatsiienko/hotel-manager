package com.vaka.repository.jdbcImpl;

import com.vaka.context.ApplicationContext;
import com.vaka.domain.Reservation;
import com.vaka.domain.ReservationStatus;
import com.vaka.repository.ReservationRepository;
import com.vaka.util.DomainExtractor;
import com.vaka.util.NamedPreparedStatement;
import com.vaka.util.StatementExtractor;
import com.vaka.util.exception.RepositoryException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Iaroslav on 12/4/2016.
 */
public class ReservationRepositoryJdbcImpl implements ReservationRepository {
    private DataSource dataSource;

    @Override
    public List<Reservation> findByRoomIdAndStatus(Integer roomId, ReservationStatus status) {
        String strCountRowsQuery = "SELECT COUNT(*) FROM reservation WHERE room_id = :roomId AND status = :status";
        String strQuery = "SELECT * FROM reservation res INNER JOIN user u ON res.user_id = u.id INNER JOIN room r ON res.room_id = r.id WHERE res.room_id = :roomId AND res.status = :status";
        try (Connection connection = getDataSource().getConnection();
             NamedPreparedStatement statement = getFindByRoomIdAndStatusStatement(connection, strQuery, roomId, status);
             ResultSet resultSet = statement.executeQuery();
             NamedPreparedStatement countStatement = getFindByRoomIdAndStatusStatement(connection, strCountRowsQuery, roomId, status);
             ResultSet countSet = countStatement.executeQuery();) {

            return fetchListFromResultSet(resultSet, countSet);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    private NamedPreparedStatement getFindByRoomIdAndStatusStatement(Connection connection, String query, Integer roomId, ReservationStatus status) throws SQLException {
        NamedPreparedStatement statement = new NamedPreparedStatement(connection, query).init();
        statement.setStatement("roomId", 1);
        statement.setStatement("status", status.name());
        return statement;
    }

    private List<Reservation> fetchListFromResultSet(ResultSet resultSet, ResultSet resultCountSet) throws SQLException {
        int count = 0;
        if (resultCountSet.next())
            count = resultCountSet.getInt(1);

        List<Reservation> reservations = new ArrayList<>(count);
        while (resultSet.next()) {
            reservations.add(DomainExtractor.extractReservation(resultSet));
        }
        return reservations;
    }

//    @Override
//    public List<Reservation> findByStatus() {
//        try {
//            Connection connection = getDataSource().getConnection();
//            String strCountRowsQuery = "SELECT COUNT(*) FROM reservation WHERE status = :status";
//            NamedPreparedStatement countStatement = new NamedPreparedStatement(connection, strCountRowsQuery).init();
//            countStatement.setStatement("status", "CONFIRMED");
//            ResultSet countSet = countStatement.executeQuery();
//            int count = 10;
//            if (countSet.next())
//                count = countSet.getInt(1);
//
//            String strQuery = "SELECT * FROM reservation res INNER JOIN user u ON res.user_id = u.id LEFT JOIN room r ON res.room_id = r.id WHERE res.status = :status";
//            NamedPreparedStatement statement = new NamedPreparedStatement(connection, strQuery).init();
//            statement.setStatement("status", "CONFIRMED");
//            ResultSet resultSet = statement.executeQuery();
//            List<Reservation> reservations = new ArrayList<>(count);
//            while (resultSet.next()) {
//                reservations.add(DomainExtractor.extractReservation(resultSet));
//            }
//            return reservations;
//        } catch (SQLException e) {
//            throw new RepositoryException(e);
//        }
//    }
//
//    @Override
//    public List<Reservation> findRequested() {
//        String strCountRowsQuery = "SELECT COUNT(*) FROM reservation WHERE status = :status";
//        String strFindQuery = "SELECT * FROM reservation res INNER JOIN user u ON res.user_id = u.id LEFT JOIN room r ON res.room_id = r.id WHERE res.status = :status";
//
//        try (Connection connection = getDataSource().getConnection();
//             NamedPreparedStatement countStatement = getRequestedCountStatement(connection, strCountRowsQuery);
//             ResultSet countSet = countStatement.executeQuery();
//             NamedPreparedStatement findStatement = getFindRequestedStatement(connection, strFindQuery);
//             ResultSet resultSet = findStatement.executeQuery();) {
//            int count = 0;
//            if (countSet.next())
//                count = countSet.getInt(1);
//
//            List<Reservation> reservations = new ArrayList<>(count);
//            while (resultSet.next()) {
//                reservations.add(DomainExtractor.extractReservation(resultSet));
//            }
//            return reservations;
//        } catch (SQLException e) {
//            throw new RepositoryException(e);
//        }
//    }
//
//    private NamedPreparedStatement getRequestedCountStatement(Connection connection, String query) throws SQLException {
//        NamedPreparedStatement statement = new NamedPreparedStatement(connection, query).init();
//        statement.setStatement("status", "REQUESTED");
//        return statement;
//    }
//
//    private NamedPreparedStatement getFindRequestedStatement(Connection connection, String query) throws SQLException {
//        NamedPreparedStatement statement = new NamedPreparedStatement(connection, query).init();
//        statement.setStatement("status", "REQUESTED");
//        return statement;
//    }
//
//    @Override
//    public List<Reservation> findRejected() {
//        String strCountRowsQuery = "SELECT COUNT(*) FROM reservation WHERE status = :status";
//        String strQuery = "SELECT * FROM reservation res INNER JOIN user u ON res.user_id = u.id LEFT JOIN room r ON res.room_id = r.id WHERE res.status = :status";
//
//        try (Connection connection = getDataSource().getConnection();
//             NamedPreparedStatement countStatement = getRejectedCountStatement(connection, strCountRowsQuery);
//             ResultSet countSet = countStatement.executeQuery();
//             NamedPreparedStatement statement = getFindRejectedStatement(connection, strQuery);
//             ResultSet resultSet = statement.executeQuery()) {
//            int count = 0;
//            if (countSet.next())
//                count = countSet.getInt(1);
//
//            List<Reservation> reservations = new ArrayList<>(count);
//            while (resultSet.next()) {
//                reservations.add(DomainExtractor.extractReservation(resultSet));
//            }
//            return reservations;
//        } catch (SQLException e) {
//            throw new RepositoryException(e);
//        }
//    }

    @Override
    public List<Reservation> findByStatus(ReservationStatus status) {
        String strCountRowsQuery = "SELECT COUNT(*) FROM reservation WHERE status = :status";
        String strQuery = "SELECT * FROM reservation res INNER JOIN user u ON res.user_id = u.id LEFT JOIN room r ON res.room_id = r.id WHERE res.status = :status";
        try (Connection connection = getDataSource().getConnection();
             NamedPreparedStatement countStatement = getStatusStatement(connection, strCountRowsQuery, status);
             ResultSet countSet = countStatement.executeQuery();
             NamedPreparedStatement statement = getStatusStatement(connection, strQuery, status);
             ResultSet resultSet = statement.executeQuery()) {

            return fetchListFromResultSet(resultSet, countSet);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    private NamedPreparedStatement getStatusStatement(Connection connection, String query, ReservationStatus status) throws SQLException {
        NamedPreparedStatement statement = new NamedPreparedStatement(connection, query).init();
        statement.setStatement("status", status.name());
        return statement;
    }

    @Override
    public Reservation create(Reservation reservation) {
        String strQueryStrategy1 = "INSERT INTO reservation (created_datetime, user_id, guests, requested_room_class, status, arrival_date, departure_date) VALUES (:createdDatetime, :userId, :guests, :requestedRoomClass, :status, :arrivalDate, :departureDate)";
        try (Connection connection = getDataSource().getConnection();
             NamedPreparedStatement statement = createAndExecuteCreateStatement(connection, strQueryStrategy1, reservation, Statement.RETURN_GENERATED_KEYS);
             ResultSet resultSet = statement.getGenerationKeys()) {
            if (resultSet.next())
                reservation.setId(resultSet.getInt(1));
            return reservation;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    private NamedPreparedStatement createAndExecuteCreateStatement(Connection connection, String strQuery, Reservation entity, int statementCode) throws SQLException {
        NamedPreparedStatement statement = new NamedPreparedStatement(connection, strQuery, statementCode).init();
        StatementExtractor.extract(entity, statement);
        statement.execute();
        return statement;
    }

    @Override
    public Optional<Reservation> getById(Integer id) {
        String strQuery = "SELECT * FROM reservation res INNER JOIN user u ON res.user_id = u.id LEFT JOIN room r ON res.room_id = r.id WHERE res.id = :id";
        try (Connection connection = getDataSource().getConnection();
             NamedPreparedStatement statement = createGetByIdStatement(connection, strQuery, id);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next())
                return Optional.of(DomainExtractor.extractReservation(resultSet));
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
        String strQuery = "DELETE FROM reservation WHERE id = :id";
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
    public boolean update(Integer id, Reservation entity) {
        entity.setId(id);
        String strQuery;
        if (entity.getRoom() != null)
            strQuery = "UPDATE reservation SET user_id = :userId, room_id = :roomId, guests = :guests, requested_room_class = :requestedRoomClass, status = :status, arrival_date = :arrivalDate, departure_date = :departureDate WHERE id = :id";
        else
            strQuery = "UPDATE reservation SET user_id = :userId, guests = :guests, requested_room_class = :requestedRoomClass, status = :status, arrival_date = :arrivalDate, departure_date = :departureDate WHERE id = :id";
        //TODO add strategies
        try (Connection connection = getDataSource().getConnection();
             NamedPreparedStatement statement = createUpdateStatement(connection, strQuery, entity)) {
            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    private NamedPreparedStatement createUpdateStatement(Connection connection, String strQuery, Reservation entity) throws SQLException {
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
