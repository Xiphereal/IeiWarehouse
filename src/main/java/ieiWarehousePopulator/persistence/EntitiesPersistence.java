package ieiWarehousePopulator.persistence;

import ieiWarehousePopulator.domain.Article;
import ieiWarehousePopulator.domain.Copy;
import ieiWarehousePopulator.domain.Person;
import ieiWarehousePopulator.domain.utils.Tuple;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class EntitiesPersistence {

    public static void persist(Persistable persistable) {
        persistable.persist();
    }

    /**
     * @return The Copy id retrieved from the database.
     * In case it already exist, it returns the existing Copy id.
     * If not, returns the newly inserted Copy.
     * If the Publication doesn't have either Magazine or Copy, will return null.
     */
    public static Integer persistMagazineAndRelatedCopy(Article article) {
        String magazineName = article.getCopyPublishedBy().getMagazinePublishBy().getName();

        if (!doesArticleHaveMagazine(magazineName)) {
            return null;
        }

        Integer retrievedCopyId = null;

        Integer retrievedMagazineId = retrieveMagazineDatabaseId(magazineName);

        if (doesMagazineAlreadyExistInDatabase(retrievedMagazineId)) {
            //Update relations
        } else {
            insertNewMagazineIntoDatabase(magazineName);
            retrievedMagazineId = retrieveMagazineDatabaseId(magazineName);
        }

        Copy copy = article.getCopyPublishedBy();

        if (!doesArticleHaveCopy(copy)) {
            return null;
        }

        retrievedCopyId = retrieveCopyDatabaseId(copy);

        if (doesCopyAlreadyExistInDatabase(retrievedCopyId)) {
            //Update relations
        } else {
            insertNewCopyIntoDatabase(retrievedMagazineId, copy);
            retrievedCopyId = retrieveCopyDatabaseId(copy);
        }

        return retrievedCopyId;
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

    private static Integer retrieveMagazineDatabaseId(String magazineName) {

        String retrieveMagazineIdSqlQuery =
                "SELECT id FROM revista " +
                        "WHERE nombre = " + "\"" + magazineName + "\"" + ";";

        Optional<Integer> retrievedMagazineId =
                MySQLConnection.performQueryToRetrieveIds(retrieveMagazineIdSqlQuery).stream().findFirst();

        return retrievedMagazineId.orElse(null);
    }

    private static boolean doesCopyAlreadyExistInDatabase(Integer retrievedCopyId) {
        return retrievedCopyId != null;
    }

    private static void insertNewCopyIntoDatabase(Integer retrievedMagazineId, Copy copy) {
        String addCopySqlUpdate =
                "INSERT INTO ejemplar (revista_id, volumen, numero, mes) " +
                        "VALUES (" + retrievedMagazineId + ", " +
                        copy.getVolume() + ", " +
                        copy.getNumber() + ", " +
                        copy.getMonth() + ");";

        MySQLConnection.performUpdate(addCopySqlUpdate);
    }

    private static boolean doesArticleHaveCopy(Copy copy) {
        return copy.getVolume() != null || copy.getNumber() != null || copy.getMonth() != null;
    }

    private static void insertNewMagazineIntoDatabase(String magazineName) {
        String addMagazineSqlUpdate =
                "INSERT INTO revista (nombre) " +
                        "VALUES (" + "\"" + magazineName + "\"" + ");";

        MySQLConnection.performUpdate(addMagazineSqlUpdate);
    }

    private static boolean doesMagazineAlreadyExistInDatabase(Integer retrievedMagazineId) {
        return retrievedMagazineId != null;
    }

    private static boolean doesArticleHaveMagazine(String magazineName) {
        return magazineName != null;
    }

    public static void insertNewPublicationHasPerson(Integer publicationId, Integer authorId) {
        String addNewPublicationHasPersonSqlUpdate =
                "INSERT INTO publicacion_has_persona (publicacion_id, persona_id) " +
                        "VALUES (" + "\"" + publicationId + "\", " +
                        "\"" + authorId + "\");";

        MySQLConnection.performUpdate(addNewPublicationHasPersonSqlUpdate);
    }

}
