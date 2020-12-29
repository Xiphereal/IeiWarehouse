package ieiWarehousePopulator.persistence.dataAccessObjects;

import ieiWarehousePopulator.domain.Book;
import ieiWarehousePopulator.domain.Person;
import ieiWarehousePopulator.extractors.utils.YearRange;
import ieiWarehousePopulator.persistence.MySQLConnection;

import java.util.List;

public class BookDAO extends PublicationDAO {
    public static List<Book> retrieveBooks(YearRange yearRange, String author) {
        // TODO: Populate the result list with the filtered Articles from the Warehouse.
        // TODO: Retrieve the list of the article's authors.
        String sqlQuery =
                "SELECT DISTINCT titulo, anyo, url, " +
                        "editorial " +
                        "FROM publicacion " +
                        "LEFT JOIN articulo ON libro.publicacion_id=publicacion.id " +
                        "WHERE anyo >= " + yearRange.getStartYear() + " " +
                        "AND anyo <= " + yearRange.getEndYear() + " " +
                        "GROUP BY titulo;";
        return MySQLConnection.performQueryToRetrieveBooks(sqlQuery);
    }

    public static void persist(Book book) {
        Integer retrievedPublicationId = retrievePublicationDatabaseId(book);

        if (!doesBookAlreadyExistInDatabase(retrievedPublicationId)) {
            insertNewPublicationIntoDatabase(book);
            insertNewBookIntoDatabase(book);

            retrievedPublicationId = retrievePublicationDatabaseId(book);
            Person.persistAuthors(book.getAuthors(), retrievedPublicationId);
        } else {
            // TODO: Notify that the book already exists in database.
        }
    }

    private static boolean doesBookAlreadyExistInDatabase(Integer retrievedId) {
        return retrievedId != null;
    }

    private static void insertNewBookIntoDatabase(Book book) {
        Integer retrievedPublicationId = retrievePublicationDatabaseId(book);

        String addBookSqlUpdate =
                "INSERT INTO libro (publicacion_id, editorial) " +
                        "VALUES (" + retrievedPublicationId + ", " +
                        "\"" + book.getEditorial() + "\");";

        MySQLConnection.performUpdate(addBookSqlUpdate);
    }
}
