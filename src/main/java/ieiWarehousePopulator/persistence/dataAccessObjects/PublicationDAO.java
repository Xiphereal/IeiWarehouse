package ieiWarehousePopulator.persistence.dataAccessObjects;

import ieiWarehousePopulator.persistence.MySQLConnection;

import java.util.Optional;

public class PublicationDAO {
    public static Integer retrievePublicationDatabaseId(String title) {
        String formattedTitle = title != null ? "= " + "\"" + title + "\"" : "IS NULL";
        String formattedYear = title != null ? "= " + title : "IS NULL";

        String retrievePublicationIdSqlQuery =
                "SELECT id FROM publicacion " +
                        "WHERE titulo " + formattedTitle + " AND " +
                        "anyo " + formattedYear + ";";

        Optional<Integer> retrievedPublicationId =
                MySQLConnection.performQueryToRetrieveIds(retrievePublicationIdSqlQuery).stream().findFirst();

        return retrievedPublicationId.orElse(null);
    }

    public static void insertNewPublicationIntoDatabase(String title, long year, String url) {
        String addPublicationSqlUpdate =
                "INSERT INTO publicacion (titulo, anyo, URL) " +
                        "VALUES (" + "\"" + title + "\", " +
                        year + ", " +
                        "\"" + url + "\");";

        MySQLConnection.performUpdate(addPublicationSqlUpdate);
    }
}
