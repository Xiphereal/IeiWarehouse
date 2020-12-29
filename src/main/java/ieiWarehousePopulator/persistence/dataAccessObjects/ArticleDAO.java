package ieiWarehousePopulator.persistence.dataAccessObjects;

import ieiWarehousePopulator.domain.Article;
import ieiWarehousePopulator.domain.Person;
import ieiWarehousePopulator.extractors.utils.YearRange;
import ieiWarehousePopulator.persistence.MySQLConnection;

import java.util.List;

public class ArticleDAO {
    public static List<Article> retrieveArticles(YearRange yearRange, String author) {
        // TODO: Populate the result list with the filtered Articles from the Warehouse.
        // TODO: Retrieve the list of the article's authors.
        String sqlQuery =
                "SELECT DISTINCT titulo, anyo, url, " +
                        "pagina_inicio, pagina_fin, " +
                        "volumen, numero, mes, " +
                        "nombre " +
                        "FROM publicacion " +
                        "LEFT JOIN articulo ON articulo.publicacion_id=publicacion.id " +
                        "LEFT JOIN ejemplar ON articulo.ejemplar_id=ejemplar.id " +
                        "LEFT JOIN revista ON ejemplar.revista_id=revista.id " +
                        "WHERE anyo >= " + yearRange.getStartYear() + " " +
                        "AND anyo <= " + yearRange.getEndYear() + " " +
                        "GROUP BY titulo;";

        return MySQLConnection.performQueryToRetrieveArticles(sqlQuery);
    }

    public static void persist(Article article) {
        Integer retrievedPublicationId = PublicationDAO.retrievePublicationDatabaseId(article);

        if (retrievedPublicationId == null) {
            Integer retrievedCopyId = article.getCopyPublishedBy()
                    .getMagazinePublishBy()
                    .persistMagazineAndRelatedCopy(article.getCopyPublishedBy());

            if (!doesArticleHaveCopy(retrievedCopyId))
                return;

            PublicationDAO.insertNewPublicationIntoDatabase(article);
            insertNewArticleIntoDatabase(retrievedCopyId, article);

            retrievedPublicationId = PublicationDAO.retrievePublicationDatabaseId(article);

            Person.persistAuthors(article.getAuthors(), retrievedPublicationId);
        } else {
            // TODO: Notify that the magazine already exists in database.
        }
    }

    private static boolean doesArticleHaveCopy(Integer retrievedCopyId) {
        return retrievedCopyId != null;
    }

    public static void insertNewArticleIntoDatabase(Integer retrievedCopyId, Article article) {
        Integer retrievedPublicationId = PublicationDAO.retrievePublicationDatabaseId(article);

        String addArticleSqlUpdate =
                "INSERT INTO articulo (publicacion_id, ejemplar_id, pagina_inicio, pagina_fin) " +
                        "VALUES (" + retrievedPublicationId + ", " +
                        retrievedCopyId + ", " +
                        article.getInitialPage() + ", " +
                        article.getFinalPage() + ");";

        MySQLConnection.performUpdate(addArticleSqlUpdate);
    }
}
