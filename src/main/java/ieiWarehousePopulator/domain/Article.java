package ieiWarehousePopulator.domain;

import ieiWarehousePopulator.persistence.MySQLConnection;
import ieiWarehousePopulator.persistence.Persistable;
import ieiWarehousePopulator.persistence.dataAccessObjects.ArticleDAO;
import ieiWarehousePopulator.persistence.dataAccessObjects.PublicationDAO;

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

    // TODO: Encapsulate the logic for persistence to the correspondent DAO class,
    //  substituting it with a call to that class.
    @Override
    public void persist() {
        ArticleDAO.persist(this);
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
