package ieiWarehousePopulator.domain;

import ieiWarehousePopulator.persistence.MySQLConnection;
import ieiWarehousePopulator.persistence.dataAccessObjects.PublicationDAO;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Publication {
    private String title;
    private Long year;
    private String url;
    private Set<Person> authors;

    public Publication(String title, Long year, String url) {
        this.title = title;
        this.year = year;
        this.url = url;
    }

    protected Integer retrievePublicationDatabaseId() {
        return PublicationDAO.retrievePublicationDatabaseId(this.getTitle());
    }

    protected void insertNewPublicationIntoDatabase() {
        PublicationDAO.insertNewPublicationIntoDatabase(this.getTitle(), this.getYear(),this.getUrl());
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

    public Set<Person> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Person> authors) {
        if (authors != null)
            this.authors = new HashSet<>(authors);
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
