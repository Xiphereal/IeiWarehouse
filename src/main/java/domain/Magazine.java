package domain;

import java.util.List;

public class Magazine {
    private String name;
    private List<Copy> publishedCopies;

    public Magazine(String name, List<Copy> publishedCopies) {
        this.name = name;
        this.publishedCopies = publishedCopies;
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
}
