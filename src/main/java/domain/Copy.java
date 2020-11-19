package domain;

import java.util.List;

public class Copy {
    private int volume;
    private int number;
    private int month;
    private Magazine magazinePublishBy;
    private List<Article> articles;

    public Copy(int volume, int number, int month, Magazine magazinePublishBy, List<Article> articles) {
        this.volume = volume;
        this.number = number;
        this.month = month;
        this.magazinePublishBy = magazinePublishBy;
        this.articles = articles;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
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
