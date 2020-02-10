package com.spigotstats.plugin;

import com.spigotstats.plugin.database.DatabaseManager;
import com.spigotstats.plugin.database.MCDatabase;
import com.spigotstats.plugin.database.MySQLEngine;
import com.spigotstats.plugin.database.MySQLiteEngine;
import com.spigotstats.plugin.database.tables.GlobalStats;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class SpigotStats extends JavaPlugin {

    public UUID serverID;
    private DatabaseManager databaseManager = null;
    private MCDatabase mcDatabase = null;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        if (getConfig().getString("serverid") == null) {
            getConfig().set("serverid", String.valueOf(UUID.randomUUID()));
            saveConfig();
        }

        serverID = UUID.fromString(getConfig().getString("serverid"));

        switch (getConfig().getString("storage")) {
            case "sqlite":
                databaseManager = new DatabaseManager(new MySQLiteEngine());
                Bukkit.getLogger().info("Hooked into SQLite database engine.");
                break;
            case "mysql":
                databaseManager = new DatabaseManager(new MySQLEngine());
                Bukkit.getLogger().info("Hooked into MySQL database engine.");
                break;
            default:
                Bukkit.getLogger().severe("There is no valid database driver defined in the config");
                Bukkit.getLogger().severe("Please choose from the options 'sqlite' or 'mysql'");
                Bukkit.getLogger().severe("Plugin will now disable.");
                this.setEnabled(false);
                return;
        }

        mcDatabase = new MCDatabase();

        databaseManager.registerTable(GlobalStats.constructTable());
    }

    @Override
    public void onDisable() {

    }

    /**
     * Get the class instance
     * @return class instance
     */
    public static SpigotStats getInstance() {
        return getPlugin(SpigotStats.class);
    }

    /**
     * Gets the database manager
     * @return database manager
     */
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    /**
     * Get the database class created for MC
     * @return MC database class
     */
    public MCDatabase getMcDatabase() {
        return mcDatabase;
    }
}
