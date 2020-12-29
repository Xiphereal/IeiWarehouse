package warehouse.persistence.dataAccessObjects;

import warehouse.domain.Publication;
import warehouse.persistence.MySQLConnection;

import java.util.Optional;

public class PublicationDAO {
    public static Integer retrievePublicationDatabaseId(Publication publication) {
        String formattedTitle = publication.getTitle() != null ?
                "= " + "\"" + publication.getTitle() + "\"" :
                "IS NULL";
        String formattedYear = publication.getYear() != null ?
                "= " + publication.getYear() :
                "IS NULL";

        String retrievePublicationIdSqlQuery =
                "SELECT id FROM publicacion " +
                        "WHERE titulo " + formattedTitle + " AND " +
                        "anyo " + formattedYear + ";";

        Optional<Integer> retrievedPublicationId =
                MySQLConnection.performQueryToRetrieveIds(retrievePublicationIdSqlQuery).stream().findFirst();

        return retrievedPublicationId.orElse(null);
    }

    public static void insertNewPublicationIntoDatabase(Publication publication) {
        String addPublicationSqlUpdate =
                "INSERT INTO publicacion (titulo, anyo, URL) " +
                        "VALUES (" + "\"" + publication.getTitle() + "\", " +
                        publication.getYear() + ", " +
                        "\"" + publication.getUrl() + "\");";

        MySQLConnection.performUpdate(addPublicationSqlUpdate);
    }
}
