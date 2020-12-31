package warehouse.persistence.dataAccessObjects;

import domainModel.Person;
import domainModel.utils.Tuple;
import warehouse.persistence.MySQLConnection;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class PersonDAO {

    public static void persistAuthors(Set<Person> authors, Integer publicationId) {

        if (!doesArticleHaveAuthors(authors)) {
            return;
        }

        List<Person> foundAuthorsInDatabase = Person.convertToPerson(retrieveAuthorsInDatabase(authors));

        authors.removeAll(foundAuthorsInDatabase);

        Set<Person> newlyFoundAuthors = authors;

        insertNewAuthorsIntoDatabase(publicationId, newlyFoundAuthors);

        // TODO: Update the relationships of every author that is already in the database.
    }

    private static boolean doesArticleHaveAuthors(Set<Person> authors) {
        return authors != null;
    }

    private static List<Tuple<String, String>> retrieveAuthorsInDatabase(Set<Person> authors) {
        StringBuilder retrieveAuthorsIdsSqlQuery = new StringBuilder("SELECT nombre, apellidos FROM persona WHERE ");

        for (Iterator<Person> iterator = authors.iterator(); iterator.hasNext(); ) {
            Person author = iterator.next();

            retrieveAuthorsIdsSqlQuery.append("(");

            retrieveAuthorsIdsSqlQuery.append("nombre = ").append("\"").append(author.getName()).append("\"");
            retrieveAuthorsIdsSqlQuery.append(" AND ");
            retrieveAuthorsIdsSqlQuery.append("apellidos =").append("\"").append(author.getSurnames()).append("\"");

            retrieveAuthorsIdsSqlQuery.append(")");

            if (iterator.hasNext())
                retrieveAuthorsIdsSqlQuery.append(" OR ");
            else
                retrieveAuthorsIdsSqlQuery.append(";");
        }

        return MySQLConnection.performQueryToRetrieveAuthors(retrieveAuthorsIdsSqlQuery.toString());
    }

    private static void insertNewAuthorsIntoDatabase(Integer publicationId, Set<Person> newlyFoundAuthors) {
        for (Person author : newlyFoundAuthors) {
            insertNewAuthorIntoDatabase(author);

            Integer authorId = retrieveAuthorIdInDatabase(author);

            insertNewPublicationHasPerson(publicationId, authorId);
        }
    }

    private static void insertNewAuthorIntoDatabase(Person author) {
        String formattedName = author.getName() != null ? "\"" + author.getName() + "\"" : "NULL";
        String formattedSurnames = author.getSurnames() != null ? "\"" + author.getSurnames() + "\"" : "NULL";

        String addAuthorSqlUpdate =
                "INSERT INTO persona (nombre, apellidos) " +
                        "VALUES (" + formattedName + ", " +
                        formattedSurnames + ");";

        MySQLConnection.performUpdate(addAuthorSqlUpdate);
    }

    private static Integer retrieveAuthorIdInDatabase(Person author) {
        String formattedName = author.getName() != null ? "= " + "\"" + author.getName() + "\"" : "IS NULL";
        String formattedSurnames = author.getSurnames() != null ? "= " + "\"" + author.getSurnames() + "\"" : "IS NULL";

        String retrieveAuthorIdSqlQuery =
                "SELECT id FROM persona " +
                        "WHERE nombre " + formattedName + " AND " +
                        "apellidos " + formattedSurnames + ";";

        Optional<Integer> retrievedAuthorId =
                MySQLConnection.performQueryToRetrieveIds(retrieveAuthorIdSqlQuery).stream().findFirst();

        return retrievedAuthorId.orElse(null);
    }

    private static void insertNewPublicationHasPerson(Integer publicationId, Integer authorId) {
        String addNewPublicationHasPersonSqlUpdate =
                "INSERT INTO publicacion_has_persona (publicacion_id, persona_id) " +
                        "VALUES (" + "\"" + publicationId + "\", " +
                        "\"" + authorId + "\");";

        MySQLConnection.performUpdate(addNewPublicationHasPersonSqlUpdate);
    }
}
