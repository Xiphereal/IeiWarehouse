package ieiWarehousePopulator.utils;

import ieiWarehousePopulator.MySQLConnection;

public class DatabasePurge {

    public static void purgeAllTables() {

        // Note that the table deletion order do matters.
        purgeTable("articulo");
        purgeTable("comunicacioncongreso");
        purgeTable("ejemplar");
        purgeTable("libro");
        purgeTable("publicacion_has_persona");
        purgeTable("revista");
        purgeTable("persona");
        purgeTable("publicacion");

    }

    public static void purgeTable(String tableName) {
        MySQLConnection.performUpdate("DELETE FROM " + tableName + ";");
    }
}