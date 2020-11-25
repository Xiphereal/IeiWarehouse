package ieiWarehousePopulator.domain;

import ieiWarehousePopulator.persistence.MySQLConnection;
import ieiWarehousePopulator.persistence.Persistable;

public class Article extends Publication implements Persistable {
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

    public void persist() {
        Integer retrievedPublicationId = super.retrievePublicationDatabaseId();

        if (!doesArticleAlreadyExistInDatabase(retrievedPublicationId)) {
            Integer retrievedCopyId = this.getCopyPublishedBy()
                    .getMagazinePublishBy()
                    .persistMagazineAndRelatedCopy(this.getCopyPublishedBy());

            super.insertNewPublicationIntoDatabase();
            this.insertNewArticleIntoDatabase(retrievedCopyId);

            retrievedPublicationId = super.retrievePublicationDatabaseId();

            Person.persistAuthors(this.getAuthors(), retrievedPublicationId);
        } else {
            // TODO: Notify that the magazine already exists in database.
        }
    }

    private static boolean doesArticleAlreadyExistInDatabase(Integer retrievedId) {
        return retrievedId != null;
    }

    private void insertNewArticleIntoDatabase(Integer retrievedCopyId) {
        Integer retrievedPublicationId = super.retrievePublicationDatabaseId();

        String addArticleSqlUpdate =
                "INSERT INTO articulo (publicacion_id, ejemplar_id, pagina_inicio, pagina_fin) " +
                        "VALUES (" + retrievedPublicationId + ", " +
                        retrievedCopyId + ", " +
                        this.getInitialPage() + ", " +
                        this.getFinalPage() + ");";

        MySQLConnection.performUpdate(addArticleSqlUpdate);
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
