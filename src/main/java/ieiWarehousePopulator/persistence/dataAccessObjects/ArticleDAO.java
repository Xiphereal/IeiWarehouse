package ieiWarehousePopulator.persistence.dataAccessObjects;

import ieiWarehousePopulator.domain.Article;
import ieiWarehousePopulator.domain.Person;
import ieiWarehousePopulator.persistence.MySQLConnection;
import ieiWarehousePopulator.restService.utils.YearRange;

import java.util.List;

public class ArticleDAO {
    public static List<Article> retrieveArticles(YearRange yearRange, Person requestedAuthor) {
        // TODO: Populate the result list with the filtered Articles from the Warehouse.
        String sqlQuery =
                "SELECT titulo, anyo, url, " +
                        "pagina_inicio, pagina_fin, " +
                        "volumen, numero, mes, " +
                        "revista.nombre, " +
                        "publicacion.id, " +
                        "persona.nombre, persona.apellidos " +
                        "FROM publicacion " +
                        "LEFT JOIN articulo ON articulo.publicacion_id=publicacion.id " +
                        "LEFT JOIN ejemplar ON articulo.ejemplar_id=ejemplar.id " +
                        "LEFT JOIN revista ON ejemplar.revista_id=revista.id " +
                        "LEFT JOIN publicacion_has_persona ON publicacion.id=publicacion_has_persona.publicacion_id " +
                        "LEFT JOIN persona ON publicacion_has_persona.persona_id=persona.id " +
                        "WHERE anyo >= " + yearRange.getStartYear() + " " +
                        "AND anyo <= " + yearRange.getEndYear() + " " +
                        "GROUP BY titulo, persona.id;";

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
