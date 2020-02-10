package com.spigotstats.plugin.database;

import com.spigotstats.plugin.SpigotStats;
import com.spigotstats.plugin.database.utils.Table;

import java.util.List;

public class MCDatabase {

    private static MCDatabase instance;

    public MCDatabase() {
        if (instance != null)
            throw new AssertionError("MCDatabase Class has already been initialized.");
        else
            instance = this;
    }

    public MCDatabase getMCDatabase() { return instance; }

    /**
     * Get an object from a table based on a UUID
     * @param uuid Player's UUID
     * @param table Table to get from
     * @return Object
     */
    public Object getFromUUID(String uuid, Table table) {
        return SpigotStats.getInstance().getDatabaseManager().getFromQuery("SELECT * FROM " + table.name() + " WHERE uuid = '" + uuid + "' LIMIT 1");
    }

    /**
     * Gets a columns value from a UUID
     * @param uuid Player's UUID
     * @param column Column to get the value from
     * @param table Table to select from
     * @return Object
     */
    public Object getColumnValueFromUUID(String uuid, String column, Table table) {
        List<Object> objectList = SpigotStats.getInstance().getDatabaseManager().getFromQuery("SELECT " + column + " FROM " + table.name() + " WHERE uuid = '" + uuid + "'");
        return objectList.get(0);
    }

    /**
     * Insert a new player row into the table
     * @param uuid Player's uuid
     * @param columnsValue column values
     * @param table table to insert into
     */
    public void makeNewPlayerRow(String uuid, Table table, String... columnsValue) {
        SpigotStats.getInstance().getDatabaseManager().makeNewRow(table, "uuid", uuid, columnsValue );
    }

    /**
     * Set a column value from a players UUID
     * @param uuid Player's UUID
     * @param column Column to edit
     * @param columnValue New value to enter into the column
     * @param table Table to enter the edit into
     */
    public void setFromUUID(String uuid, String column, String columnValue, Table table) {
        SpigotStats.getInstance().getDatabaseManager().setFromQuery("UPDATE " + table.name() + " SET " + column + " = '" + columnValue + "' WHERE uuid = '" + uuid + "'");
    }

    /**
     * Check to see if a user has an existing row in a table
     * @param uuid Player's UUID
     * @param table Table to check
     * @return Boolean
     */
    public boolean playerRowExists(String uuid, Table table) {
        String result = String.valueOf(SpigotStats.getInstance().getDatabaseManager().getFromQuery("SELECT EXISTS(SELECT * FROM " + table.name() + " WHERE uuid = '" + uuid + "')"));
        return result.equals("1");
    }

}
