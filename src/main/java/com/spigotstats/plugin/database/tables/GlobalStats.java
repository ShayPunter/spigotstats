package com.spigotstats.plugin.database.tables;

import com.google.common.collect.Maps;
import com.spigotstats.plugin.database.utils.DataTypes;
import com.spigotstats.plugin.database.utils.Table;

import java.util.Map;

public enum GlobalStats {
    BLOCKS_PLACED("blocks_placed", DataTypes.BIGINT, "blocks_placed");

    public static final String TABLE_NAME = "spigotstats_global_stats";
    public final String name;
    public final String type;
    public final String def;

    private GlobalStats(String columnName, String defType) { this(columnName, defType, ""); }

    private GlobalStats(String columnName, String dataType, String def) {
        name = columnName;
        type = dataType;
        this.def = def;
    }

    public String toString() { return name; }

    public static Table getTable() { return Table.getTable(TABLE_NAME); }

    public static final Table constructTable() {
        Map<String, String> columns = Maps.newLinkedHashMap();
        GlobalStats[] statsTables;
        int length = (statsTables = values()).length;

        for (int i = 0; i < length; ++i ) {
            GlobalStats column = statsTables[i];
            columns.put(column.name() + " " + column.type, column.def);
        }
        return new Table(TABLE_NAME, columns);
    }
}
