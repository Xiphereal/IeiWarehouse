import domain.Article;
import domain.Copy;
import domain.Person;
import domain.Publication;

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

//            Integer retrievedCopyId = persistMagazineAndRelatedCopy(article);

            persistAuthors(article.getAuthors());

//            insertNewPublicationIntoDatabase(publication);
//            insertNewArticleIntoDatabase(article, publication, retrievedCopyId);
        }
    }

    private static Integer retrievePublicationDatabaseId(Publication publication) {
        String retrievePublicationIdSqlQuery =
                "SELECT id FROM publicacion " +
                        "WHERE titulo = " + "\"" + publication.getTitle() + "\"" + " AND " +
                        "anyo =" + publication.getYear() + ";";

        Optional<Integer> retrievedPublicationId =
                MySQLConnection.performQuery(retrievePublicationIdSqlQuery).stream().findFirst();

        return retrievedPublicationId.orElse(null);
    }

    private static boolean doesArticleAlreadyExistInDatabase(Integer retrievedId) {
        return retrievedId != null;
    }

    /**
     * @return The Copy id retrieved from the database.
     * In case it already exist, it returns the existing Copy id.
     * If not, returns the newly inserted Copy.
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
                    String addCopySqlUpdate =
                            "INSERT INTO ejemplar (revista_id, volumen, numero, mes) " +
                                    "VALUES (" + retrievedMagazineId + ", " +
                                    copy.getVolume() + ", " +
                                    copy.getNumber() + ", " +
                                    copy.getMonth() + ");";

                    MySQLConnection.performUpdate(addCopySqlUpdate);

                    retrievedCopyId = retrieveCopyDatabaseId(copy);
                }
            }
        }

        return retrievedCopyId;
    }

    private static Integer retrieveCopyDatabaseId(Copy copy) {
        String retrieveCopyIdSqlQuery =
                "SELECT id FROM ejemplar " +
                        "WHERE volumen = " + copy.getVolume() + " AND " +
                        "numero = " + copy.getNumber() + " AND " +
                        "mes = " + copy.getMonth() + ";";

        Optional<Integer> retrievedCopyId =
                MySQLConnection.performQuery(retrieveCopyIdSqlQuery).stream().findFirst();

        return retrievedCopyId.orElse(null);
    }

    private static Integer retrieveMagazineDatabaseId(String magazineName) {
        String retrieveMagazineIdSqlQuery =
                "SELECT id FROM revista " +
                        "WHERE nombre = " + "\"" + magazineName + "\"" + ";";

        Optional<Integer> retrievedMagazineId =
                MySQLConnection.performQuery(retrieveMagazineIdSqlQuery).stream().findFirst();

        return retrievedMagazineId.orElse(null);
    }

    private static boolean doesCopyAlreadyExistInDatabase(Integer retrievedCopyId) {
        return retrievedCopyId != null;
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

    private static void persistAuthors(List<Person> authors) {
        List<Integer> foundAuthorsInDatabase = retrieveAuthorsDatabaseIds(authors);

        // Retrieve (name, surnames) from the publication authors present in the database.

        // {authors} - {foreach((name, surnames))}.

        // foreach NonInDbAuthor
            // Insert new author to DB.
            // Insert the 'publicacion_has_persona' tuple.

        // foreach Author in DB.
            // Update relationships: Insert the 'publicacion_has_persona' tuple.

        System.out.println(foundAuthorsInDatabase);
    }

    // TODO: The method should return a List<Tuple<String name, String surnames>> for the authors found
    //  in the database.
    private static List<Integer> retrieveAuthorsDatabaseIds(List<Person> authors) {
        StringBuilder retrieveAuthorsIdsSqlQuery = new StringBuilder("SELECT id FROM persona WHERE ");

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

        return MySQLConnection.performQuery(retrieveAuthorsIdsSqlQuery.toString());
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
