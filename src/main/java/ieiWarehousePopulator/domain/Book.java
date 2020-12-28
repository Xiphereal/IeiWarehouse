package ieiWarehousePopulator.domain;

import ieiWarehousePopulator.persistence.Persistable;
import ieiWarehousePopulator.persistence.dataAccessObjects.BookDAO;

public class Book extends Publication implements Persistable {
    private String editorial;

    public Book(String title, Long year, String url, String editorial) {
        super(title, year, url);
        this.editorial = editorial;
    }

    @Override
    public void persist() {
        BookDAO.persist(this);
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    @Override
    public String toString() {
        return "Book{" +
                "editorial='" + editorial + '\'' +
                "} " + super.toString();
    }
}
