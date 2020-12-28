package ieiWarehousePopulator.persistence.dataAccessObjects;

import ieiWarehousePopulator.domain.Book;
import ieiWarehousePopulator.domain.Person;
import ieiWarehousePopulator.extractors.utils.YearRange;
import ieiWarehousePopulator.persistence.MySQLConnection;

import java.util.List;

public class BookDAO extends PublicationDAO {
    public static List<Book> retrieveBooks(YearRange yearRange, String author) {
        // TODO: Populate the result list with the filtered Books from the Warehouse.
        return null;
    }

    public static void persist(Book book) {
        Integer retrievedPublicationId = retrievePublicationDatabaseId(book.getTitle());

        if (!doesBookAlreadyExistInDatabase(retrievedPublicationId)) {
            insertNewPublicationIntoDatabase(book.getTitle(), book.getYear(), book.getUrl());
            insertNewBookIntoDatabase(book);

            retrievedPublicationId = retrievePublicationDatabaseId(book.getTitle());
            Person.persistAuthors(book.getAuthors(), retrievedPublicationId);
        } else {
            // TODO: Notify that the book already exists in database.
        }
    }

    private static boolean doesBookAlreadyExistInDatabase(Integer retrievedId) {
        return retrievedId != null;
    }

    private static void insertNewBookIntoDatabase(Book book) {
        Integer retrievedPublicationId = retrievePublicationDatabaseId(book.getTitle());

        String addBookSqlUpdate =
                "INSERT INTO libro (publicacion_id, editorial) " +
                        "VALUES (" + retrievedPublicationId + ", " +
                        "\"" + book.getEditorial() + "\");";

        MySQLConnection.performUpdate(addBookSqlUpdate);
    }
}
