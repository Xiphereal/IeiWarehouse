package ieiWarehousePopulator;

import ieiWarehousePopulator.domain.Article;
import ieiWarehousePopulator.domain.Copy;
import ieiWarehousePopulator.domain.Person;
import ieiWarehousePopulator.domain.Publication;
import ieiWarehousePopulator.domain.utils.Tuple;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class EntitiesPersistence {
    public static void persist(Publication publication) {

        if (publication instanceof Article)
            persistArticle(publication);

    }

    private static void persistArticle(Publication publication) {
        Integer retrievedPublicationId = retrievePublicationDatabaseId(publication);

        if (doesArticleAlreadyExistInDatabase(retrievedPublicationId)) {
            //Update relations
        } else {
            Article article = (Article) publication;

            Integer retrievedCopyId = persistMagazineAndRelatedCopy(article);

            insertNewPublicationIntoDatabase(publication);
            insertNewArticleIntoDatabase(article, publication, retrievedCopyId);

            retrievedPublicationId = retrievePublicationDatabaseId(publication);

            persistAuthors(article.getAuthors(), retrievedPublicationId);
        }
    }

    private static Integer retrievePublicationDatabaseId(Publication publication) {
        String retrievePublicationIdSqlQuery =
                "SELECT id FROM publicacion " +
                        "WHERE titulo = " + "\"" + publication.getTitle() + "\"" + " AND " +
                        "anyo =" + publication.getYear() + ";";

        Optional<Integer> retrievedPublicationId =
                MySQLConnection.performQueryToRetrieveIds(retrievePublicationIdSqlQuery).stream().findFirst();

        return retrievedPublicationId.orElse(null);
    }

    private static boolean doesArticleAlreadyExistInDatabase(Integer retrievedId) {
        return retrievedId != null;
    }

    /**
     * @return The Copy id retrieved from the database.
     * In case it already exist, it returns the existing Copy id.
     * If not, returns the newly inserted Copy.
     * If the Publication doesn't have either Magazine or Copy, will return null.
     */
    private static Integer persistMagazineAndRelatedCopy(Article article) {
        String magazineName = article.getCopyPublishedBy().getMagazinePublishBy().getName();

        Integer retrievedCopyId = null;

        if (doesArticleHaveMagazine(magazineName)) {

            Integer retrievedMagazineId = retrieveMagazineDatabaseId(magazineName);

            if (doesMagazineAlreadyExistInDatabase(retrievedMagazineId)) {
                //Update relations
            } else {
                insertNewMagazineIntoDatabase(magazineName);
                retrievedMagazineId = retrieveMagazineDatabaseId(magazineName);
            }

            Copy copy = article.getCopyPublishedBy();
            if (doesArticleHaveCopy(copy)) {

                retrievedCopyId = retrieveCopyDatabaseId(copy);

                if (doesCopyAlreadyExistInDatabase(retrievedCopyId)) {
                    //Update relations
                } else {
                    insertNewCopyIntoDatabase(retrievedMagazineId, copy);
                    retrievedCopyId = retrieveCopyDatabaseId(copy);
                }
            }
        }

        return retrievedCopyId;
    }

    private static Integer retrieveCopyDatabaseId(Copy copy) {
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

    private static Integer retrieveMagazineDatabaseId(String magazineName) {

        String retrieveMagazineIdSqlQuery =
                "SELECT id FROM revista " +
                        "WHERE nombre = " + "\"" + magazineName + "\"" + ";";

        Optional<Integer> retrievedMagazineId =
                MySQLConnection.performQueryToRetrieveIds(retrieveMagazineIdSqlQuery).stream().findFirst();

        return retrievedMagazineId.orElse(null);
    }

    private static boolean doesCopyAlreadyExistInDatabase(Integer retrievedCopyId) {
        return retrievedCopyId != null;
    }

    private static void insertNewCopyIntoDatabase(Integer retrievedMagazineId, Copy copy) {
        Integer retrievedCopyId;
        String addCopySqlUpdate =
                "INSERT INTO ejemplar (revista_id, volumen, numero, mes) " +
                        "VALUES (" + retrievedMagazineId + ", " +
                        copy.getVolume() + ", " +
                        copy.getNumber() + ", " +
                        copy.getMonth() + ");";

        MySQLConnection.performUpdate(addCopySqlUpdate);
    }

    private static boolean doesArticleHaveCopy(Copy copy) {
        return copy.getVolume() != null || copy.getNumber() != null || copy.getMonth() != null;
    }

    private static void insertNewMagazineIntoDatabase(String magazineName) {
        String addMagazineSqlUpdate =
                "INSERT INTO revista (nombre) " +
                        "VALUES (" + "\"" + magazineName + "\"" + ");";

        MySQLConnection.performUpdate(addMagazineSqlUpdate);
    }

    private static boolean doesMagazineAlreadyExistInDatabase(Integer retrievedMagazineId) {
        return retrievedMagazineId != null;
    }

    private static boolean doesArticleHaveMagazine(String magazineName) {
        return magazineName != null;
    }

    private static void persistAuthors(List<Person> authors, Integer publicationId) {

        if (!doesArticleHaveAuthors(authors)) {
            return;
        }

        List<Person> foundAuthorsInDatabase = Person.convertToPerson(retrieveAuthorsInDatabase(authors));

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

            insertNewPublicationHasPerson(publicationId, authorId);
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

    private static void insertNewPublicationHasPerson(Integer publicationId, Integer authorId) {
        String addNewPublicationHasPersonSqlUpdate =
                "INSERT INTO publicacion_has_persona (publicacion_id, persona_id) " +
                        "VALUES (" + "\"" + publicationId + "\", " +
                        "\"" + authorId + "\");";

        MySQLConnection.performUpdate(addNewPublicationHasPersonSqlUpdate);
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

    private static void insertNewPublicationIntoDatabase(Publication publication) {
        String addPublicationSqlUpdate =
                "INSERT INTO publicacion (titulo, anyo, URL) " +
                        "VALUES (" + "\"" + publication.getTitle() + "\", " +
                        publication.getYear() + ", " +
                        "\"" + publication.getUrl() + "\");";

        MySQLConnection.performUpdate(addPublicationSqlUpdate);
    }

    private static void insertNewArticleIntoDatabase(Article article, Publication publication, Integer retrievedCopyId) {
        Integer retrievedPublicationId = retrievePublicationDatabaseId(publication);

        String addArticleSqlUpdate =
                "INSERT INTO articulo (publicacion_id, ejemplar_id, pagina_inicio, pagina_fin) " +
                        "VALUES (" + retrievedPublicationId + ", " +
                        retrievedCopyId + ", " +
                        article.getInitialPage() + ", " +
                        article.getFinalPage() + ");";

        MySQLConnection.performUpdate(addArticleSqlUpdate);
    }
}
