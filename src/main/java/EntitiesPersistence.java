import domain.Article;
import domain.Copy;
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
                    Article article = (Article) publication;

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
                                                copy.getMonth() + ");" ;

                                MySQLConnection.performUpdate(addCopySqlUpdate);

                                retrievedCopyId = retrieveCopyDatabaseId(copy);
                            }
                        }
                    }

                    insertNewPublicationIntoDatabase(publication);
                    insertNewArticleIntoDatabase(article, retrievePublicationIdSqlQuery, retrievedCopyId);

                    //Resolve all of the objects dependencies
                }
            }

    }

    private static Integer retrieveCopyDatabaseId(Copy copy) {
        Integer retrievedCopyId;
        String retrieveCopyIdSqlQuery =
                "SELECT id FROM ejemplar " +
                        "WHERE volumen = " + copy.getVolume() + " AND " +
                        "numero = " + copy.getNumber() + " AND " +
                        "mes = " + copy.getMonth() + ";";

        retrievedCopyId = MySQLConnection.performQuery(retrieveCopyIdSqlQuery);
        return retrievedCopyId;
    }

    private static Integer retrieveMagazineDatabaseId(String magazineName) {
        String retrieveMagazineIdSqlQuery =
                "SELECT id FROM revista " +
                        "WHERE nombre = " + "\"" + magazineName + "\"" + ";";

        Integer retrievedMagazineId = MySQLConnection.performQuery(retrieveMagazineIdSqlQuery);
        return retrievedMagazineId;
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
                        "VALUES (" + "\"" + magazineName + "\"" + ");" ;

        MySQLConnection.performUpdate(addMagazineSqlUpdate);
    }

    private static boolean doesMagazineAlreadyExistInDatabase(Integer retrievedMagazineId) {
        return retrievedMagazineId != null;
    }

    private static boolean doesArticleHaveMagazine(String magazineName) {
        return magazineName != null;
    }

    private static void insertNewPublicationIntoDatabase(Publication publication) {
        String addPublicationSqlUpdate =
                "INSERT INTO publicacion (titulo, anyo, URL) " +
                        "VALUES (" + "\"" + publication.getTitle() + "\", " +
                        publication.getYear() + ", " +
                        "\"" + publication.getUrl() +  "\");" ;

        MySQLConnection.performUpdate(addPublicationSqlUpdate);
    }

    private static void insertNewArticleIntoDatabase(Article article, String retrievePublicationIdSqlQuery, Integer retrievedCopyId) {
        Integer retrievedPublicationId = MySQLConnection.performQuery(retrievePublicationIdSqlQuery);

        String addArticleSqlUpdate =
                "INSERT INTO articulo (publicacion_id, ejemplar_id, pagina_inicio, pagina_fin) " +
                        "VALUES (" + retrievedPublicationId + ", " +
                        retrievedCopyId + ", " +
                        article.getInitialPage() + ", " +
                        article.getFinalPage() + ");" ;

        MySQLConnection.performUpdate(addArticleSqlUpdate);
    }

    private static boolean doesArticleAlreadyExistInDatabase(Integer retrievedId) {
        return retrievedId != null;
    }
}
