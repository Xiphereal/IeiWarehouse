package ieiWarehousePopulator.domain;

import ieiWarehousePopulator.persistence.MySQLConnection;

import java.util.Optional;

public class Magazine {
    private String name;

    public Magazine(String name) {
        this.name = name;
    }

    /**
     * @param copyPublishedBy The specific copy for in which the article has been published in. Since the current
     *                        database scheme is being mirrored, there's no way of knowing which copies does a
     *                        Magazine have, so the reference to the published copy is needed.
     * @return The Copy id retrieved from the database.
     * In case it already exist, it returns the existing Copy id.
     * If not, returns the newly inserted Copy.
     * If the Publication doesn't have either Magazine or Copy, will return null.
     */
    public Integer persistMagazineAndRelatedCopy(Copy copyPublishedBy) {
        if (!doesArticleHaveMagazine()) {
            return null;
        }

        Integer retrievedMagazineId = retrieveMagazineDatabaseId();

        if (doesMagazineAlreadyExistInDatabase(retrievedMagazineId)) {
            //Update relations
        } else {
            insertNewMagazineIntoDatabase();
            retrievedMagazineId = retrieveMagazineDatabaseId();
        }

        if (!doesArticleHaveCopy(copyPublishedBy)) {
            return null;
        }

        Integer retrievedCopyId = retrieveCopyDatabaseId(copyPublishedBy);

        if (doesCopyAlreadyExistInDatabase(retrievedCopyId)) {
            //Update relations
        } else {
            insertNewCopyIntoDatabase(retrievedMagazineId, copyPublishedBy);
            retrievedCopyId = retrieveCopyDatabaseId(copyPublishedBy);
        }

        return retrievedCopyId;
    }

    /**
     * If the Article doesn't have a related Magazine, the Magazine instance is created with all its fields to null.
     */
    private boolean doesArticleHaveMagazine() {
        return this.name != null;
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

    private Integer retrieveMagazineDatabaseId() {
        String retrieveMagazineIdSqlQuery =
                "SELECT id FROM revista " +
                        "WHERE nombre = " + "\"" + this.name + "\"" + ";";

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

    private void insertNewMagazineIntoDatabase() {
        String addMagazineSqlUpdate =
                "INSERT INTO revista (nombre) " +
                        "VALUES (" + "\"" + this.name + "\"" + ");";

        MySQLConnection.performUpdate(addMagazineSqlUpdate);
    }

    private static boolean doesMagazineAlreadyExistInDatabase(Integer retrievedMagazineId) {
        return retrievedMagazineId != null;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Magazine{" +
                "name='" + name + '\'' +
                '}';
    }
}
