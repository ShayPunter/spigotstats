package com.spigotstats.plugin.database;

import com.spigotstats.plugin.database.utils.Table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class DatabaseManager {

    private List<Table> tables = new ArrayList<>();
    private DatabaseEngine databaseEngine;

    public DatabaseManager(DatabaseEngine engine) {
        databaseEngine = engine;
    }

    /**
     * Close the connection to the database
     */
    public void closeConnection() {
        databaseEngine.closeConnection();
    }

    /**
     * Open a connection to the database
     */
    public void openConnection() {
        databaseEngine.openConnection();
    }

    /**
     * Terminate the resultset from a database query
     * @param resultSet resultset to terminate
     */
    public void terminateResultSet(ResultSet resultSet) {
        databaseEngine.terminateResultSet(resultSet);
    }

    /**
     * Terminate a statement from a query
     * @param statement statement to terminate
     */
    public void terminateStatement(Statement statement) {
        databaseEngine.terminateStatement(statement);
    }

    /**
     * Terminate a prepared statement from a query
     * @param preparedStatement preparedstatement to terminate
     */
    public void terminatePreparedStatement(PreparedStatement preparedStatement) {
        databaseEngine.terminatePreparedStatement(preparedStatement);
    }

    /**
     * Create a table in the database
     * @param table table to create
     */
    public void createTable(Table table) {
        databaseEngine.createTable(table);
    }

    /**
     * Sends a get query to the database
     * @param query query to execute
     * @return data from the database
     */
    public List<Object> getFromQuery(String query) {
        return databaseEngine.getFromQuery(query);
    }

    /**
     * Set values from a query to the database
     * @param query query to execute
     */
    public void setFromQuery(String query) {
        databaseEngine.setFromQuery(query);
    }

    /**
     * Make a new row in the database
     * @param table table to make the row in
     * @param key key column name
     * @param keyValue value to place as the key
     * @param values values to place in other columns
     */
    public void makeNewRow(Table table, String key, String keyValue, String... values) {
        databaseEngine.makeNewRow(table, key, keyValue, values);
    }

    /**
     * Deletes a row from the database
     * @param table table to delete row from
     * @param key key column name
     * @param keyValue key to find and delete
     */
    public void deleteRow(Table table, String key, String keyValue) {
        databaseEngine.deleteRow(table, key, keyValue);
    }

    /**
     * Gets the tables created
     * @param name Name of the table to get
     * @return Table
     */
    public Table getTable(String name) {

        for (Table t : tables) {
            if (t.name().equalsIgnoreCase(name)) {
                return t;
            }
        }

        return null;
    }

    /**
     * Register an SQL table to the plugin
     * @param table table to register
     */
    public final void registerTable(final Table table) {
        tables.removeIf(o -> ((Table) o).name().equals(table.name()));

        tables.add(table);

        databaseEngine.createTable(table);
    }

}
