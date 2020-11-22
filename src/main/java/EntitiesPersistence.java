import domain.Article;
import domain.Publication;

public class EntitiesPersistence {
    public static void persist(Publication publication) {

            if (publication instanceof Article) {
                String retrievePublicationIdSqlQuery =
                        "SELECT id FROM publicacion " +
                                "WHERE titulo = " + "\"" + publication.getTitle() + "\"" + " AND " +
                               "anyo =" + publication.getYear() + ";";

                Integer retrievedPublicationId = MySQLConnection.performQuery(retrievePublicationIdSqlQuery);

                if (doesArticleAlreadyExistInDatabase(retrievedPublicationId)) {
                    //Update relations
                } else {
                    insertNewPublicationIntoDatabase(publication);
                    insertNewArticleIntoDatabase(publication, retrievePublicationIdSqlQuery);

                    //Resolve all of the objects dependencies
                }
            }

    }

    private static void insertNewPublicationIntoDatabase(Publication publication) {
        String addPublicationSqlUpdate =
                "INSERT INTO publicacion (titulo, anyo, URL) " +
                        "VALUES (" + "\"" + publication.getTitle() + "\", " +
                        publication.getYear() + ", " +
                        "\"" + publication.getUrl() +  "\");" ;

        MySQLConnection.performUpdate(addPublicationSqlUpdate);
    }

    private static void insertNewArticleIntoDatabase(Publication publication, String retrievePublicationIdSqlQuery) {
        Integer retrievedPublicationId = MySQLConnection.performQuery(retrievePublicationIdSqlQuery);

        Article article = (Article) publication;

        String addArticleSqlUpdate =
                "INSERT INTO articulo (publicacion_id, pagina_inicio, pagina_fin) " +
                        "VALUES (" + retrievedPublicationId + ", " +
                        article.getInitialPage() + ", " +
                        article.getFinalPage() + ");" ;

        MySQLConnection.performUpdate(addArticleSqlUpdate);
    }

    private static boolean doesArticleAlreadyExistInDatabase(Integer retrievedId) {
        return retrievedId != null;
    }
}
