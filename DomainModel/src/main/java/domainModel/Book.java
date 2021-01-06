package domainModel;

public class Book extends Publication {
    private String publisher;

    public Book() {
    }

    public Book(String title, Long year, String url, String editorial) {
        super(title, year, url);
        this.publisher = editorial;
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
