package domain;

import java.util.List;

public class Publication {
    private String title;
    private Long year;
    private String url;
    private List<Person> authors;

    public Publication(String title, Long year, String url, List<Person> authors) {
        this.title = title;
        this.year = year;
        this.url = url;
        this.authors = authors;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Person> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Person> authors) {
        this.authors = authors;
    }
}
