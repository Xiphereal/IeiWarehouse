package warehouse.persistence.dataAccessObjects;

import domainModel.Book;
import domainModel.Person;
import domainModel.utils.YearRange;
import warehouse.persistence.MySQLConnection;

import java.util.List;

public class BookDAO extends PublicationDAO {
    public static List<Book> retrieveBooks(YearRange yearRange, Person requestedAuthor) {
        String sqlQuery =
                "SELECT titulo, anyo, url, " +
                        "editorial, " +
                        "publicacion.id, " +
                        "persona.nombre, persona.apellidos " +
                        "FROM publicacion " +
                        "JOIN libro ON libro.publicacion_id=publicacion.id " +
                        "LEFT JOIN publicacion_has_persona ON publicacion.id=publicacion_has_persona.publicacion_id " +
                        "LEFT JOIN persona ON publicacion_has_persona.persona_id=persona.id " +
                        "WHERE anyo >= " + yearRange.getStartYear() + " " +
                        "AND anyo <= " + yearRange.getEndYear() + " ";

        if (requestedAuthor != null)
            sqlQuery +=
                    "AND persona.nombre = '" + requestedAuthor.getName() + "' " +
                            "AND persona.apellidos = '" + requestedAuthor.getSurnames() + "' ";

        sqlQuery += "GROUP BY titulo, persona.id;";

        return MySQLConnection.performQueryToRetrieveBooks(sqlQuery);
    }

    public static void persist(Book book) {
        Integer retrievedPublicationId = retrievePublicationDatabaseId(book);

        if (!doesBookAlreadyExistInDatabase(retrievedPublicationId)) {
            insertNewPublicationIntoDatabase(book);
            insertNewBookIntoDatabase(book);

            retrievedPublicationId = retrievePublicationDatabaseId(book);
            PersonDAO.persistAuthors(book.getAuthors(), retrievedPublicationId);
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
                        "\"" + book.getPublisher() + "\");";

        MySQLConnection.performUpdate(addBookSqlUpdate);
    }
}
