package com.spigotstats.plugin.database;

import com.spigotstats.plugin.database.utils.Table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public interface DatabaseEngine {

    /**
     * Close an active connection to a database
     */
    void closeConnection();

    /**
     * Open a connection to the database
     */
    void openConnection();

    /**
     * Terminate a result set
     * @param resultSet resultset to terminate
     */
    void terminateResultSet(ResultSet resultSet);

    /**
     * Terminate a statement
     * @param statement statement to terminate
     */
    void terminateStatement(Statement statement);

    /**
     * Terminate a prepared statement
     * @param preparedStatement preparedstatement to terminate
     */
    void terminatePreparedStatement(PreparedStatement preparedStatement);

    /**
     * Creates a table
     * @param table table to create
     */
    void createTable(Table table);

    /**
     * Get an item from the database based on an SQL query
     * @param query query to execute
     * @return result from the database
     */
    List<Object> getFromQuery(String query);

    /**
     * Set a value in the database based on a query
     * @param query query to execute
     */
    void setFromQuery(String query);

    /**
     * Makes a new row in the database
     * @param table table to make the row in
     * @param key key column
     * @param keyValue value to put as the key
     * @param values values to put in the database
     */
    void makeNewRow(Table table, String key, String keyValue, String... values);

    /**
     * Delete a row from the database
     * @param table table to delete row from
     * @param key key column to search
     * @param keyValue value to find
     */
    void deleteRow(Table table, String key, String keyValue);

}
