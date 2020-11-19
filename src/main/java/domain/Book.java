package domain;

import java.util.List;

public class Book extends Publication {
    private String editorial;

    public Book(String title, Long year, String url, List<Person> authors, String editorial) {
        super(title, year, url, authors);
        this.editorial = editorial;
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
