package warehouse.persistence.dataAccessObjects;

import warehouse.domain.CongressCommunication;
import warehouse.domain.Person;
import warehouse.persistence.MySQLConnection;
import warehouse.restService.utils.YearRange;

import java.util.List;

public class CongressCommunicationDAO extends PublicationDAO {
    public static List<CongressCommunication> retrieveCongressCommunications(YearRange yearRange, Person requestedAuthor) {
        String sqlQuery =
                "SELECT titulo, anyo, url, " +
                        "pagina_inicio, pagina_fin, " +
                        "congreso, edicion, lugar, " +
                        "publicacion.id, " +
                        "persona.nombre, persona.apellidos " +
                        "FROM publicacion " +
                        "LEFT JOIN comunicacioncongreso ON comunicacioncongreso.publicacion_id=publicacion.id " +
                        "LEFT JOIN publicacion_has_persona ON publicacion.id=publicacion_has_persona.publicacion_id " +
                        "LEFT JOIN persona ON publicacion_has_persona.persona_id=persona.id " +
                        "WHERE anyo >= " + yearRange.getStartYear() + " " +
                        "AND anyo <= " + yearRange.getEndYear() + " ";

        if (requestedAuthor != null)
            sqlQuery +=
                    "AND persona.nombre = '" + requestedAuthor.getName() + "' " +
                            "AND persona.apellidos = '" + requestedAuthor.getSurnames() + "' ";

        sqlQuery += "GROUP BY titulo, persona.id;";

        return MySQLConnection.performQueryToRetrieveCongressCommunications(sqlQuery);
    }

    public static void persist(CongressCommunication congressCommunication) {
        Integer retrievedPublicationId = retrievePublicationDatabaseId(congressCommunication);

        if (!doesCongressCommunicationAlreadyExistInDatabase(retrievedPublicationId)) {
            insertNewPublicationIntoDatabase(congressCommunication);
            insertNewCongressCommunicationIntoDatabase(congressCommunication);

            retrievedPublicationId = retrievePublicationDatabaseId(congressCommunication);
            Person.persistAuthors(congressCommunication.getAuthors(), retrievedPublicationId);
        } else {
            // TODO: Notify that the Congress Communication already exists in database.
        }
    }

    private static boolean doesCongressCommunicationAlreadyExistInDatabase(Integer retrievedId) {
        return retrievedId != null;
    }

    private static void insertNewCongressCommunicationIntoDatabase(CongressCommunication congressCommunication) {
        Integer retrievedPublicationId = retrievePublicationDatabaseId(congressCommunication);

        String addCongressCommunicationSqlUpdate =
                "INSERT INTO comunicacioncongreso (publicacion_id, congreso, edicion, lugar, pagina_inicio, pagina_fin) " +
                        "VALUES (" + retrievedPublicationId + ", " +
                        "\"" + congressCommunication.getCongress() + "\"" + ", " +
                        "\"" + congressCommunication.getEdition() + "\"" + ", " +
                        "\"" + congressCommunication.getPlace() + "\"" + ", " +
                        congressCommunication.getInitialPage() + ", " +
                        congressCommunication.getFinalPage() + ");";

        MySQLConnection.performUpdate(addCongressCommunicationSqlUpdate);
    }
}
