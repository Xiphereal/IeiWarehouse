package domain;

import java.util.List;

public class Person {
    private String name;
    private String surnames;
    private List<String> authoredPublications;

    public Person(String name, String surnames, List<String> authoredPublications) {
        this.name = name;
        this.surnames = surnames;
        this.authoredPublications = authoredPublications;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurnames() {
        return surnames;
    }

    public void setSurnames(String surnames) {
        this.surnames = surnames;
    }

    public List<String> getAuthoredPublications() {
        return authoredPublications;
    }

    public void setAuthoredPublications(List<String> authoredPublications) {
        this.authoredPublications = authoredPublications;
    }
}
