package ieiWarehousePopulator.domain;

import ieiWarehousePopulator.persistence.MySQLConnection;
import ieiWarehousePopulator.persistence.Persistable;

public class Book extends Publication implements Persistable {
    private String editorial;

    public Book(String title, Long year, String url, String editorial) {
        super(title, year, url);
        this.editorial = editorial;
    }

    @Override
    public void persist() {
        Integer retrievedPublicationId = super.retrievePublicationDatabaseId();

        if (!doesBookAlreadyExistInDatabase(retrievedPublicationId)) {
            super.insertNewPublicationIntoDatabase();
            this.insertNewBookIntoDatabase();

            retrievedPublicationId = super.retrievePublicationDatabaseId();
            Person.persistAuthors(this.getAuthors(), retrievedPublicationId);
        } else {
            // TODO: Notify that the book already exists in database.
        }
    }

    private boolean doesBookAlreadyExistInDatabase(Integer retrievedId) {
        return retrievedId != null;
    }

    private void insertNewBookIntoDatabase() {
        Integer retrievedPublicationId = super.retrievePublicationDatabaseId();

        String addBookSqlUpdate =
                "INSERT INTO libro (publicacion_id, editorial) " +
                        "VALUES (" + retrievedPublicationId + ", " +
                        "\"" + this.getEditorial() + "\");";

        MySQLConnection.performUpdate(addBookSqlUpdate);
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    @Override
    public String toString() {
        return "Book{" +
                "editorial='" + editorial + '\'' +
                "} " + super.toString();
    }
}
