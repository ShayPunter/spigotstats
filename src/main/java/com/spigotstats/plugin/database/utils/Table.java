package com.spigotstats.plugin.database.utils;

import com.google.common.collect.Lists;
import com.spigotstats.plugin.SpigotStats;

import java.util.List;
import java.util.Map;

public class Table {
    private String name;
    private String statNames;
    private String namesWithTypes;
    private String delimitedValues;
    private boolean playerSpecific;
    private Map<String, String> stats;

    public Table(String name, Map<String, String> stats) {
        this(name, stats, true);
    }

    public Table(String name, Map<String, String> stats, boolean playerSpecific) {
        this.statNames = "";
        this.namesWithTypes = "";
        this.delimitedValues = "";
        this.name = name;
        this.stats = stats;
        this.playerSpecific = playerSpecific;
        List<String> keys = Lists.newArrayList(stats.keySet());
        List<String> values = Lists.newArrayList(stats.values());

        for(int i = 0; i < keys.size(); ++i) {
            if (!this.namesWithTypes.equals("")) {
                this.namesWithTypes = this.namesWithTypes + ", ";
            }

            this.namesWithTypes = this.namesWithTypes + keys.get(i);
            if (!(values.get(i)).equals("")) {
                if (!this.statNames.equals("")) {
                    this.statNames = this.statNames + ", ";
                }

                this.statNames = this.statNames + keys.get(i).split(" ")[0];
                if (!this.delimitedValues.equals("")) {
                    this.delimitedValues = this.delimitedValues + ", ";
                }

                this.delimitedValues = this.delimitedValues + values.get(i);
            }
        }

    }

    public String toString() {
        return this.name;
    }

    public String name() {
        return this.name;
    }

    public Map<String, String> getStats() {
        return this.stats;
    }

    public String getStatNames() {
        return this.statNames;
    }

    public String getNamesWithTypes() {
        return this.namesWithTypes;
    }

    public String getDelimitedValues() {
        return this.delimitedValues;
    }

    public boolean isPlayerSpecific() {
        return this.playerSpecific;
    }

    public static Table getTable(String name) {
        return SpigotStats.getInstance().getDatabaseManager().getTable(name);
    }
}