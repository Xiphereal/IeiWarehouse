package ieiWarehousePopulator.domain;

import ieiWarehousePopulator.domain.utils.Tuple;
import ieiWarehousePopulator.persistence.EntitiesPersistence;
import ieiWarehousePopulator.persistence.MySQLConnection;

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

    public static void persistAuthors(List<Person> authors, Integer publicationId) {

        if (!doesArticleHaveAuthors(authors)) {
            return;
        }

        List<Person> foundAuthorsInDatabase = convertToPerson(retrieveAuthorsInDatabase(authors));

        authors.removeAll(foundAuthorsInDatabase);

        List<Person> newlyFoundAuthors = authors;

        insertNewAuthorsIntoDatabase(publicationId, newlyFoundAuthors);

        // TODO: Update the relationships of every author that is already in the database.

        System.out.println(foundAuthorsInDatabase);
    }

    private static boolean doesArticleHaveAuthors(List<Person> authors) {
        return authors != null;
    }

    private static void insertNewAuthorsIntoDatabase(Integer publicationId, List<Person> newlyFoundAuthors) {
        for (Person author : newlyFoundAuthors) {
            insertNewAuthorIntoDatabase(author);

            Integer authorId = retrieveAuthorIdInDatabase(author);

            EntitiesPersistence.insertNewPublicationHasPerson(publicationId, authorId);
        }
    }

    private static void insertNewAuthorIntoDatabase(Person author) {
        String addAuthorSqlUpdate =
                "INSERT INTO persona (nombre, apellidos) " +
                        "VALUES (" + "\"" + author.getName() + "\", " +
                        "\"" + author.getSurnames() + "\");";

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

    private static List<Tuple<String, String>> retrieveAuthorsInDatabase(List<Person> authors) {
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
