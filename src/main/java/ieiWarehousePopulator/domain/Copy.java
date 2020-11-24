package ieiWarehousePopulator.domain;

import ieiWarehousePopulator.persistence.MySQLConnection;

import java.util.Optional;

public class Copy {
    private Integer volume;
    private Integer number;
    private Integer month;
    private Magazine magazinePublishBy;

    public Copy(Integer volume, Integer number, Integer month) {
        this.volume = volume;
        this.number = number;
        this.month = month;
    }

    public Integer persistCopy(Integer retrievedMagazineId) {
        if (!doesArticleHaveCopy(this)) {
            return null;
        }

        Integer retrievedCopyId = retrieveCopyDatabaseId(this);

        if (doesCopyAlreadyExistInDatabase(retrievedCopyId)) {
            //Update relations
        } else {
            insertNewCopyIntoDatabase(retrievedMagazineId, this);
            retrievedCopyId = retrieveCopyDatabaseId(this);
        }

        return retrievedCopyId;
    }

    public static Integer retrieveCopyDatabaseId(Copy copy) {
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

    public static boolean doesCopyAlreadyExistInDatabase(Integer retrievedCopyId) {
        return retrievedCopyId != null;
    }

    public static void insertNewCopyIntoDatabase(Integer retrievedMagazineId, Copy copy) {
        String addCopySqlUpdate =
                "INSERT INTO ejemplar (revista_id, volumen, numero, mes) " +
                        "VALUES (" + retrievedMagazineId + ", " +
                        copy.getVolume() + ", " +
                        copy.getNumber() + ", " +
                        copy.getMonth() + ");";

        MySQLConnection.performUpdate(addCopySqlUpdate);
    }

    public static boolean doesArticleHaveCopy(Copy copy) {
        return copy.getVolume() != null || copy.getNumber() != null || copy.getMonth() != null;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Magazine getMagazinePublishBy() {
        return magazinePublishBy;
    }

    public void setMagazinePublishBy(Magazine magazinePublishBy) {
        this.magazinePublishBy = magazinePublishBy;
    }

    @Override
    public String toString() {
        return "Copy{" +
                "volume=" + volume +
                ", number=" + number +
                ", month=" + month +
                ", magazinePublishBy=" + magazinePublishBy +
                '}';
    }
}
