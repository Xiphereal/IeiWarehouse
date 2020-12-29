package ieiWarehousePopulator.persistence.dataAccessObjects;

import ieiWarehousePopulator.domain.CongressCommunication;
import ieiWarehousePopulator.domain.Person;
import ieiWarehousePopulator.persistence.MySQLConnection;
import ieiWarehousePopulator.restService.utils.YearRange;

import java.util.List;

public class CongressCommunicationDAO extends PublicationDAO {
    public static List<CongressCommunication> retrieveCongressCommunications(YearRange yearRange, String author) {
        // TODO: Populate the result list with the filtered CongressCommunications from the Warehouse.
        return null;
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
