package domain;

import java.util.List;

public class Copy {
    private Integer volume;
    private Integer number;
    private Integer month;
    private Magazine magazinePublishBy;
    private List<Article> articles;

    public Copy(Integer volume, Integer number, Integer month, Magazine magazinePublishBy, List<Article> articles) {
        this.volume = volume;
        this.number = number;
        this.month = month;
        this.magazinePublishBy = magazinePublishBy;
        this.articles = articles;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Magazine getMagazinePublishBy() {
        return magazinePublishBy;
    }

    public void setMagazinePublishBy(Magazine magazinePublishBy) {
        this.magazinePublishBy = magazinePublishBy;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    @Override
    public String toString() {
        return "Copy{" +
                "volume=" + volume +
                ", number=" + number +
                ", month=" + month +
                ", magazinePublishBy=" + magazinePublishBy +
                ", articles=" + articles +
                '}';
    }
}
