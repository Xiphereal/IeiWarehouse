package warehouse.persistence.dataAccessObjects;

import domainModel.Copy;
import warehouse.persistence.MySQLConnection;

import java.util.Optional;

public class CopyDAO {
    public static Integer persist(Copy copy, Integer retrievedMagazineId) {
        if (!doesArticleHaveCopy(copy)) {
            return null;
        }

        Integer retrievedCopyId = retrieveCopyDatabaseId();

        if (!doesCopyAlreadyExistInDatabase(retrievedCopyId)) {
            insertNewCopyIntoDatabase(copy, retrievedMagazineId);
            retrievedCopyId = retrieveCopyDatabaseId(copy);
        } else {
            // TODO: Notify that the Copy already exists in database.
        }

        return retrievedCopyId;
    }

    private static boolean doesArticleHaveCopy(Copy copy) {
        return copy.getVolume() != null || copy.getNumber() != null || copy.getMonth() != null;
    }

    private static Integer retrieveCopyDatabaseId(Copy copy) {
        String formattedVolume = copy.getVolume() != null ? "= " + copy.getVolume() : "IS NULL";
        String formattedNumber = copy.getNumber() != null ? "= " + copy.getNumber() : "IS NULL";
        String formattedMonth = copy.getMonth() != null ? "= " + copy.getMonth() : "IS NULL";

        String retrieveCopyIdSqlQuery =
                "SELECT id FROM ejemplar " +
                        "WHERE volumen " + formattedVolume + " AND " +
                        "numero " + formattedNumber + " AND " +
                        "mes " + formattedMonth + ";";

        Optional<Integer> retrievedCopyId =
                MySQLConnection.performQueryToRetrieveIds(retrieveCopyIdSqlQuery).stream().findFirst();

        return retrievedCopyId.orElse(null);
    }

    private static boolean doesCopyAlreadyExistInDatabase(Integer retrievedCopyId) {
        return retrievedCopyId != null;
    }

    private static void insertNewCopyIntoDatabase(Copy copy, Integer retrievedMagazineId) {
        String addCopySqlUpdate =
                "INSERT INTO ejemplar (revista_id, volumen, numero, mes) " +
                        "VALUES (" + retrievedMagazineId + ", " +
                        copy.getVolume() + ", " +
                        copy.getNumber() + ", " +
                        copy.getMonth() + ");";

        MySQLConnection.performUpdate(addCopySqlUpdate);
    }
}
