package ieiWarehousePopulator.domain;

import ieiWarehousePopulator.EntitiesPersistence;
import ieiWarehousePopulator.MySQLConnection;

public class Article extends Publication {
    private Integer initialPage;
    private Integer finalPage;
    private Copy copyPublishedBy;

    public Article(String title,
                   Long year,
                   String url,
                   Integer initialPage,
                   Integer finalPage) {
        super(title, year, url);
        this.initialPage = initialPage;
        this.finalPage = finalPage;
    }

    public static void persistArticle(Publication publication) {
        Integer retrievedPublicationId = EntitiesPersistence.retrievePublicationDatabaseId(publication);

        if (doesArticleAlreadyExistInDatabase(retrievedPublicationId)) {
            //Update relations
        } else {
            Article article = (Article) publication;

            Integer retrievedCopyId = EntitiesPersistence.persistMagazineAndRelatedCopy(article);

            EntitiesPersistence.insertNewPublicationIntoDatabase(publication);
            insertNewArticleIntoDatabase(article, publication, retrievedCopyId);

            retrievedPublicationId = EntitiesPersistence.retrievePublicationDatabaseId(publication);

            EntitiesPersistence.persistAuthors(article.getAuthors(), retrievedPublicationId);
        }
    }

    private static void insertNewArticleIntoDatabase(Article article, Publication publication, Integer retrievedCopyId) {
        Integer retrievedPublicationId = EntitiesPersistence.retrievePublicationDatabaseId(publication);

        String addArticleSqlUpdate =
                "INSERT INTO articulo (publicacion_id, ejemplar_id, pagina_inicio, pagina_fin) " +
                        "VALUES (" + retrievedPublicationId + ", " +
                        retrievedCopyId + ", " +
                        article.getInitialPage() + ", " +
                        article.getFinalPage() + ");";

        MySQLConnection.performUpdate(addArticleSqlUpdate);
    }

    private static boolean doesArticleAlreadyExistInDatabase(Integer retrievedId) {
        return retrievedId != null;
    }

    public Integer getInitialPage() {
        return initialPage;
    }

    public void setInitialPage(int initialPage) {
        this.initialPage = initialPage;
    }

    public Integer getFinalPage() {
        return finalPage;
    }

    public void setFinalPage(int finalPage) {
        this.finalPage = finalPage;
    }

    public Copy getCopyPublishedBy() {
        return copyPublishedBy;
    }

    public void setCopyPublishedBy(Copy copyPublishedBy) {
        this.copyPublishedBy = copyPublishedBy;
    }

    @Override
    public String toString() {
        return "Article{" +
                "initialPage=" + initialPage +
                ", finalPage=" + finalPage +
                ", copyPublishedBy=" + copyPublishedBy +
                "} " + super.toString();
    }
}
