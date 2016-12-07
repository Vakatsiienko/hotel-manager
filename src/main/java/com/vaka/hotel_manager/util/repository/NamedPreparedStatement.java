package com.vaka.hotel_manager.util.repository;

import lombok.Getter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Iaroslav on 12/3/2016.
 */
public class NamedPreparedStatement implements AutoCloseable {
    @Getter
    private PreparedStatement preparedStatement;
    private List<String> fields = new ArrayList<>();
    private String strQuery;
    private Connection connection;
    private boolean statementCodeIsSet;
    private int statementCode;

    public NamedPreparedStatement(Connection connection, String strQuery) throws SQLException {
        this.strQuery = strQuery;
        this.connection = connection;
    }

    public NamedPreparedStatement(Connection connection, String strQuery, int statementCode) throws SQLException {
        this.strQuery = strQuery;
        this.statementCode = statementCode;
        this.connection = connection;
        statementCodeIsSet = true;
    }

    public NamedPreparedStatement init() throws SQLException {
        parseStringToStatement();
        if (statementCodeIsSet)
            preparedStatement = connection.prepareStatement(strQuery, statementCode);
        else preparedStatement = connection.prepareStatement(strQuery);
        return this;
    }

    private void parseStringToStatement(){
        int pos;
        List<Integer> punctuation = new ArrayList<>();
        while ((pos = strQuery.indexOf(":")) != -1) {
//            int endComma = strQuery.substring(pos).indexOf(',');
//            int endSpace = strQuery.substring(pos).indexOf(' ');
//            int endBrace = strQuery.substring(pos).indexOf(')');
            int end;
//
//            if (endComma != -1 && endComma < endSpace && endComma < endBrace) {
//                end = endComma;
//            } else if (endSpace != -1 && endSpace < endBrace) end = endSpace;
//            else end = endBrace;
            punctuation.add(strQuery.substring(pos).indexOf(','));
            punctuation.add(strQuery.substring(pos).indexOf(' '));
            punctuation.add(strQuery.substring(pos).indexOf(')'));
            while (punctuation.remove(Integer.valueOf(-1))) ;
            if (punctuation.size() != 0) {
                punctuation.sort(Integer::compareTo);
                end = punctuation.get(0);
            } else end = -1;
            if (end == -1)
                end = strQuery.length();
            else
                end += pos;
            fields.add(strQuery.substring(pos + 1, end));
            strQuery = strQuery.substring(0, pos) + "?" + strQuery.substring(end);
            punctuation.clear();
        }
    }

    public ResultSet getGenerationKeys() throws SQLException {
        return preparedStatement.getGeneratedKeys();
    }

    @Override
    public void close() throws SQLException {
        preparedStatement.close();
    }

    /**
     * {@link PreparedStatement#executeQuery()}}
     */
    public ResultSet executeQuery() throws SQLException {
        return preparedStatement.executeQuery();
    }

    /**
     * {@link PreparedStatement#executeUpdate()}
     */
    public int executeUpdate() throws SQLException {
        return preparedStatement.executeUpdate();
    }

    /**
     * {@link PreparedStatement#execute()}
     */
    public boolean execute() throws SQLException {
        return preparedStatement.execute();
    }

    public void setStatement(String name, String value) throws SQLException {
        int index = getIndex(name);
        if (index != 0)
            preparedStatement.setString(index, value);
    }

    public void setStatement(String name, int value) throws SQLException {
        int index = getIndex(name);
        if (index != 0)
            preparedStatement.setInt(index, value);
    }

    public void setStatement(String name, long value) throws SQLException {
        int index = getIndex(name);
        if (index != 0)
            preparedStatement.setLong(index, value);
    }

    public void setStatement(String name, boolean value) throws SQLException {
        int index = getIndex(name);
        if (index != 0)
            preparedStatement.setBoolean(index, value);
    }

    public void setStatement(String name, Date value) throws SQLException {
        int index = getIndex(name);
        if (index != 0)
            preparedStatement.setDate(index, value);
    }

    private int getIndex(String name) {
        return fields.indexOf(name) + 1;
    }


    public void setStatement(String name, Timestamp value) throws SQLException {
        int index = getIndex(name);
        if (index != 0)
            preparedStatement.setTimestamp(index, value);
    }
}
