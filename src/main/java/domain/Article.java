package domain;

import java.util.List;

public class Article extends Publication {
    private int initialPage;
    private int finalPage;
    private Copy copyPublishedBy;

    public Article(String title, Long year, String url, List<Person> authors, int initialPage, int finalPage, Copy copyPublishedBy) {
        super(title, year, url, authors);
        this.initialPage = initialPage;
        this.finalPage = finalPage;
        this.copyPublishedBy = copyPublishedBy;
    }

    public int getInitialPage() {
        return initialPage;
    }

    public void setInitialPage(int initialPage) {
        this.initialPage = initialPage;
    }

    public int getFinalPage() {
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
}
