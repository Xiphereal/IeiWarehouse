package domainModel;

import warehouse.persistence.MySQLConnection;

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
    // TODO: Encapsulate the logic for persistence to the correspondent DAO class,
    //  substituting it with a call to that class.
    public Integer persistMagazineAndRelatedCopy(Copy copyPublishedBy) {
        if (!doesArticleHaveMagazine()) {
            return null;
        }

        Integer retrievedMagazineId = retrieveMagazineDatabaseId();

        if (!doesMagazineAlreadyExistInDatabase(retrievedMagazineId)) {
            insertNewMagazineIntoDatabase();
            retrievedMagazineId = retrieveMagazineDatabaseId();
        } else {
            // TODO: Notify that the Magazine already exists in database.
        }

        Integer retrievedCopyId = copyPublishedBy.persistCopy(retrievedMagazineId);

        return retrievedCopyId;
    }

    /**
     * If the Article doesn't have a related Magazine, the Magazine instance is created with all its fields to null.
     */
    private boolean doesArticleHaveMagazine() {
        return this.name != null;
    }

    private Integer retrieveMagazineDatabaseId() {
        String retrieveMagazineIdSqlQuery =
                "SELECT id FROM revista " +
                        "WHERE nombre = " + "\"" + this.name + "\"" + ";";

        Optional<Integer> retrievedMagazineId =
                MySQLConnection.performQueryToRetrieveIds(retrieveMagazineIdSqlQuery).stream().findFirst();

        return retrievedMagazineId.orElse(null);
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
