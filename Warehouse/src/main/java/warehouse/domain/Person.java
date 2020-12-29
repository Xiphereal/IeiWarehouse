package warehouse.domain;

import warehouse.domain.utils.Tuple;
import warehouse.persistence.MySQLConnection;

import java.util.*;

public class Person {
    private String name;
    private String surnames;
    private String authoredPublication;

    public Person(String name, String surnames) {
        this.name = name;
        this.surnames = surnames;
    }

    public static List<Person> convertToPerson(List<Tuple<String, String>> tupleCollection) {
        List<Person> personList = new ArrayList<>();

        for (Tuple<String, String> tuple : tupleCollection)
            personList.add(new Person(tuple.getFirstElement(), tuple.getSecondElement()));

        return personList;
    }

    // TODO: Encapsulate the logic for persistence to the correspondent DAO class,
    //  substituting it with a call to that class.
    public static void persistAuthors(Set<Person> authors, Integer publicationId) {

        if (!doesArticleHaveAuthors(authors)) {
            return;
        }

        List<Person> foundAuthorsInDatabase = convertToPerson(retrieveAuthorsInDatabase(authors));

        authors.removeAll(foundAuthorsInDatabase);

        Set<Person> newlyFoundAuthors = authors;

        insertNewAuthorsIntoDatabase(publicationId, newlyFoundAuthors);

        // TODO: Update the relationships of every author that is already in the database.

        System.out.println(foundAuthorsInDatabase);
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

    /**
     * @param author The name is the first encountered word, the surname the second.
     */
    public static Person extractPersonAttributes(String author) {
        // Split the string using spaces as separators.
        String[] splitAuthor = author.split(" ");

        String name = splitAuthor[0];
        String surname = splitAuthor.length > 1 ? splitAuthor[1] : null;

        return new Person(name, surname);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurnames() {
        return surnames;
    }

    public void setSurnames(String surnames) {
        this.surnames = surnames;
    }

    public String getAuthoredPublication() {
        return authoredPublication;
    }

    public void setAuthoredPublication(String authoredPublication) {
        this.authoredPublication = authoredPublication;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", surnames='" + surnames + '\'' +
                ", authoredPublication=" + authoredPublication +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surnames);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        Person person = (Person) o;

        return Objects.equals(name, person.name) &&
                Objects.equals(surnames, person.surnames);
    }
}