package domain;

import java.util.List;

public class Magazine {
    private String name;
    private List<Copy> publishedCopies;

    public Magazine(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Copy> getPublishedCopies() {
        return publishedCopies;
    }

    public void setPublishedCopies(List<Copy> publishedCopies) {
        this.publishedCopies = publishedCopies;
    }

    @Override
    public String toString() {
        return "Magazine{" +
                "name='" + name + '\'' +
                ", publishedCopies=" + publishedCopies +
                '}';
    }
}
