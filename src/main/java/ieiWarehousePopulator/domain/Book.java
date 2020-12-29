package ieiWarehousePopulator.domain;

import ieiWarehousePopulator.persistence.Persistable;
import ieiWarehousePopulator.persistence.dataAccessObjects.BookDAO;

public class Book extends Publication implements Persistable {
    private String publisher;

    public Book(String title, Long year, String url, String editorial) {
        super(title, year, url);
        this.publisher = editorial;
    }

    @Override
    public void persist() {
        BookDAO.persist(this);
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    @Override
    public String toString() {
        return "Book{" +
                "editorial='" + publisher + '\'' +
                "} " + super.toString();
    }
}
