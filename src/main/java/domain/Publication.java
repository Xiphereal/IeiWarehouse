package domain;

import java.util.List;

public class Publication {
    private String title;
    private Long year;
    private String url;
    private List<Person> authors;

    public Publication(String title, Long year, String url) {
        this.title = title;
        this.year = year;
        this.url = url;
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

    @Override
    public String toString() {
        return "Publication{" +
                "title='" + title + '\'' +
                ", year=" + year +
                ", url='" + url + '\'' +
                ", authors=" + authors +
                '}';
    }
}
