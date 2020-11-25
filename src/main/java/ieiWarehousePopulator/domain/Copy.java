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

        Integer retrievedCopyId = this.retrieveCopyDatabaseId();

        if (!doesCopyAlreadyExistInDatabase(retrievedCopyId)) {
            this.insertNewCopyIntoDatabase(retrievedMagazineId);
            retrievedCopyId = this.retrieveCopyDatabaseId();
        } else {
            // TODO: Notify that the Copy already exists in database.
        }

        return retrievedCopyId;
    }

    private static boolean doesArticleHaveCopy(Copy copy) {
        return copy.getVolume() != null || copy.getNumber() != null || copy.getMonth() != null;
    }

    private Integer retrieveCopyDatabaseId() {
        String formattedVolume = this.getVolume() != null ? "= " + this.getVolume() : "IS NULL";
        String formattedNumber = this.getNumber() != null ? "= " + this.getNumber() : "IS NULL";
        String formattedMonth = this.getMonth() != null ? "= " + this.getMonth() : "IS NULL";

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

    private void insertNewCopyIntoDatabase(Integer retrievedMagazineId) {
        String addCopySqlUpdate =
                "INSERT INTO ejemplar (revista_id, volumen, numero, mes) " +
                        "VALUES (" + retrievedMagazineId + ", " +
                        this.getVolume() + ", " +
                        this.getNumber() + ", " +
                        this.getMonth() + ");";

        MySQLConnection.performUpdate(addCopySqlUpdate);
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
