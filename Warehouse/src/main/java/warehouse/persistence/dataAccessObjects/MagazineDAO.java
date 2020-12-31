package warehouse.persistence.dataAccessObjects;

import domainModel.Copy;
import domainModel.Magazine;
import warehouse.persistence.MySQLConnection;

import java.util.Optional;

public class MagazineDAO {

    /**
     * @param copyPublishedBy The specific copy for in which the article has been published in. Since the current
     *                        database scheme is being mirrored, there's no way of knowing which copies does a
     *                        Magazine have, so the reference to the published copy is needed.
     * @return The Copy id retrieved from the database.
     * In case it already exist, it returns the existing Copy id.
     * If not, returns the newly inserted Copy.
     * If the Publication doesn't have either Magazine or Copy, will return null.
     */
    // TODO: Encapsulate the logic for persistence to the correspondent DAO class,
    //  substituting it with a call to that class.
    public static Integer persistMagazineAndRelatedCopy(Magazine magazine, Copy copyPublishedBy) {
        if (!doesArticleHaveMagazine(magazine)) {
            return null;
        }

        Integer retrievedMagazineId = retrieveMagazineDatabaseId(magazine);

        if (!doesMagazineAlreadyExistInDatabase(retrievedMagazineId)) {
            insertNewMagazineIntoDatabase(magazine);
            retrievedMagazineId = retrieveMagazineDatabaseId(magazine);
        } else {
            // TODO: Notify that the Magazine already exists in database.
        }

        Integer retrievedCopyId = CopyDAO.persist(copyPublishedBy, retrievedMagazineId);

        return retrievedCopyId;
    }

    /**
     * If the Article doesn't have a related Magazine, the Magazine instance is created with all its fields to null.
     */
    private static boolean doesArticleHaveMagazine(Magazine magazine) {
        return magazine.getName() != null;
    }

    private static Integer retrieveMagazineDatabaseId(Magazine magazine) {
        String retrieveMagazineIdSqlQuery =
                "SELECT id FROM revista " +
                        "WHERE nombre = " + "\"" + magazine.getName() + "\"" + ";";

        Optional<Integer> retrievedMagazineId =
                MySQLConnection.performQueryToRetrieveIds(retrieveMagazineIdSqlQuery).stream().findFirst();

        return retrievedMagazineId.orElse(null);
    }

    private static void insertNewMagazineIntoDatabase(Magazine magazine) {
        String addMagazineSqlUpdate =
                "INSERT INTO revista (nombre) " +
                        "VALUES (" + "\"" + magazine.getName() + "\"" + ");";

        MySQLConnection.performUpdate(addMagazineSqlUpdate);
    }

    private static boolean doesMagazineAlreadyExistInDatabase(Integer retrievedMagazineId) {
        return retrievedMagazineId != null;
    }
}
