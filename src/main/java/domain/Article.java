package domain;

import java.util.List;

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
