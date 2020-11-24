package ieiWarehousePopulator.domain;

import ieiWarehousePopulator.persistence.MySQLConnection;

import java.util.List;
import java.util.Optional;

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

    protected Integer retrievePublicationDatabaseId() {
        String formattedTitle = this.getTitle() != null ? "= " + "\"" + this.getTitle() + "\"" : "IS NULL";
        String formattedYear = this.getYear() != null ? "= " + this.getYear() : "IS NULL";

        String retrievePublicationIdSqlQuery =
                "SELECT id FROM publicacion " +
                        "WHERE titulo " + formattedTitle + " AND " +
                        "anyo " + formattedYear + ";";

        Optional<Integer> retrievedPublicationId =
                MySQLConnection.performQueryToRetrieveIds(retrievePublicationIdSqlQuery).stream().findFirst();

        return retrievedPublicationId.orElse(null);
    }

    protected void insertNewPublicationIntoDatabase() {
        String addPublicationSqlUpdate =
                "INSERT INTO publicacion (titulo, anyo, URL) " +
                        "VALUES (" + "\"" + this.getTitle() + "\", " +
                        this.getYear() + ", " +
                        "\"" + this.getUrl() + "\");";

        MySQLConnection.performUpdate(addPublicationSqlUpdate);
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
