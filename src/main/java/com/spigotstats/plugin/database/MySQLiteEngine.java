package com.spigotstats.plugin.database;

import com.google.common.collect.Lists;
import com.spigotstats.plugin.SpigotStats;
import com.spigotstats.plugin.database.utils.Table;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

public class MySQLiteEngine implements DatabaseEngine {

    public Connection connection = null;

    @Override
    public void closeConnection() {
        if (connection == null)
            return;

        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void openConnection() {
        closeConnection();

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + SpigotStats.getInstance().getDataFolder() + "/storage.sql");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void terminateResultSet(ResultSet resultSet) {
        if (resultSet == null)
            return;

        try {
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void terminateStatement(Statement statement) {
        if (statement == null)
            return;

        try {
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void terminatePreparedStatement(PreparedStatement preparedStatement) {
        if (preparedStatement == null)
            return;

        try {
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createTable(Table table) {
        Statement statement = null;

        try {
            if (this.connection == null || this.connection.isClosed()) {
                this.openConnection();
            }

            statement = this.connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + table + " (" + table.getNamesWithTypes() + ")");
            Iterator var4 = table.getStats().keySet().iterator();

            while(var4.hasNext()) {
                String nameWithType = (String)var4.next();

                try {
                    statement.executeUpdate("ALTER TABLE " + table + " ADD " + nameWithType);
                } catch (SQLException var15) {
                    if (var15.getErrorCode() != 1060) {
                        statement.close();
                        throw var15;
                    }
                }
            }
        } catch (SQLException var16) {
            Bukkit.getLogger().log(Level.SEVERE, "Error with MySQL: " + var16.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException var14) {
                Bukkit.getLogger().log(Level.SEVERE, "Error with MySQL: " + var14.getMessage());
            }

        }
    }

    @Override
    public List<Object> getFromQuery(String query) {
        List<List<Object>> results = Lists.newArrayList();
        ResultSet resultSet = null;
        Statement statement = null;

        try {
            if (connection == null || connection.isClosed())
                openConnection();

            statement = connection.createStatement();
            boolean executed = statement.execute(query);

            if (executed) {
                resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    List<Object> row = Lists.newArrayList();
                    for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); ++i) {
                        row.add(resultSet.getObject(i));
                    }
                    results.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            terminateResultSet(resultSet);
            terminateStatement(statement);
        }
        return results.get(0);
    }

    @Override
    public void setFromQuery(String query) {
        PreparedStatement preparedStatement = null;

        try {
            if (connection == null || connection.isClosed())
                openConnection();

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            terminatePreparedStatement(preparedStatement);
        }
    }

    @Override
    public void makeNewRow(Table table, String key, String keyValue, String... values) {
        Statement statement = null;
        ResultSet resultSet = null;
        StringBuilder serializedValues = new StringBuilder();
        serializedValues.append("'").append(keyValue).append("',");

        for (String value : values) {
            serializedValues.append("'").append(value).append("',");
        }

        serializedValues.deleteCharAt(serializedValues.length() - 1);

        try {
            if (connection == null || connection.isClosed())
                openConnection();

            statement = connection.createStatement();

            System.out.println("SELECT * FROM " + table.name() + " WHERE " + key + " = '" + keyValue + "' LIMIT 1");
            System.out.println("INSERT INTO " + table.name() + " (" + table.getStatNames() + ") VALUES (" + serializedValues + ")");

            resultSet = statement.executeQuery("SELECT * FROM " + table.name() + " WHERE " + key + " = '" + keyValue + "' LIMIT 1");
            if (!resultSet.next())
                statement.execute("INSERT INTO " + table.name() + " (" + table.getStatNames() + ") VALUES (" + serializedValues + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }  finally {
            terminateResultSet(resultSet);
            terminateStatement(statement);
        }
    }

    @Override
    public void deleteRow(Table table, String key, String keyValue) {
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            if (connection == null || connection.isClosed())
                openConnection();

            statement = connection.createStatement();
            resultSet = statement.executeQuery("DELETE FROM " + table + " WHERE " + key + "='" + keyValue + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            terminateResultSet(resultSet);
            terminateStatement(statement);
        }
    }
}
